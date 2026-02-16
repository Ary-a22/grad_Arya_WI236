import java.sql.*;
import java.math.BigDecimal;

public class MaintenanceDAO {

    /* =====================================================
       ================= BILL ENSURE =======================
       ===================================================== */

    private static int ensureBillExists(Connection con, int siteId) throws Exception {

        String checkSql = "SELECT bill_id FROM maintenance_bill WHERE site_id = ?";

        try (PreparedStatement ps = con.prepareStatement(checkSql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("bill_id");
            }
        }

        // Create bill if not exists
        String createSql = """
            INSERT INTO maintenance_bill (site_id, total_amount, balance_amount, status)
            SELECT 
                s.site_id,
                (s.area * mt.rate_per_sq_meter),
                (s.area * mt.rate_per_sq_meter),
                'PENDING'
            FROM sites s
            JOIN maintenance_type mt
                ON s.maintenance_type_id = mt.maintenance_type_id
            WHERE s.site_id = ?
            RETURNING bill_id
        """;

        try (PreparedStatement ps = con.prepareStatement(createSql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("bill_id");
        }
    }

    /* =====================================================
       ================= GET BALANCE =======================
       ===================================================== */

    private static BigDecimal getBalance(Connection con, int siteId) throws Exception {

        String sql = """
            SELECT balance_amount
            FROM maintenance_bill
            WHERE site_id = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("balance_amount");
            }
        }

        return BigDecimal.ZERO;
    }

    /* =====================================================
       ================= PAYMENT ENGINE ====================
       ===================================================== */

    private static void payInternal(int siteId, BigDecimal amount) {

        try (Connection con = DBConfig.getConnection()) {

            con.setAutoCommit(false);

            int billId = ensureBillExists(con, siteId);

            String insertPayment = """
                INSERT INTO payment_transaction (bill_id, paid_amount)
                VALUES (?, ?)
            """;

            String updateBill = """
                UPDATE maintenance_bill
                SET balance_amount = balance_amount - ?,
                    status = CASE
                        WHEN balance_amount - ? <= 0 THEN 'PAID'
                        ELSE 'PENDING'
                    END
                WHERE bill_id = ?
            """;

            try (PreparedStatement ps1 = con.prepareStatement(insertPayment);
                 PreparedStatement ps2 = con.prepareStatement(updateBill)) {

                ps1.setInt(1, billId);
                ps1.setBigDecimal(2, amount);
                ps1.executeUpdate();

                ps2.setBigDecimal(1, amount);
                ps2.setBigDecimal(2, amount);
                ps2.setInt(3, billId);
                ps2.executeUpdate();

                con.commit();

            } catch (Exception e) {
                con.rollback();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ================= OWNER PAYMENT =====================
       ===================================================== */

    public static void payFullAmount(int ownerId, int billId) {

        String sql = """
            SELECT s.site_id
            FROM maintenance_bill mb
            JOIN sites s ON mb.site_id = s.site_id
            WHERE mb.bill_id = ? AND s.owner_id = ?
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ps.setInt(2, ownerId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid bill or unauthorized.");
                return;
            }

            int siteId = rs.getInt("site_id");

            BigDecimal balance = getBalance(con, siteId);

            if (balance.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Bill already paid.");
                return;
            }

            payInternal(siteId, balance);
            System.out.println("Full payment successful.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void payPartialAmount(int ownerId, int billId, BigDecimal amount) {

        String sql = """
            SELECT s.site_id
            FROM maintenance_bill mb
            JOIN sites s ON mb.site_id = s.site_id
            WHERE mb.bill_id = ? AND s.owner_id = ?
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ps.setInt(2, ownerId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid bill or unauthorized.");
                return;
            }

            int siteId = rs.getInt("site_id");

            BigDecimal balance = getBalance(con, siteId);

            if (amount.compareTo(BigDecimal.ZERO) <= 0 ||
                amount.compareTo(balance) > 0) {
                System.out.println("Invalid payment amount.");
                return;
            }

            payInternal(siteId, amount);
            System.out.println("Partial payment successful.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ================= VIEW PENDING ======================
       ===================================================== */

    public static void viewPendingAll() {
        executePrint("""
            SELECT * FROM maintenance_bill
            WHERE balance_amount > 0
        """);
    }

    public static void viewPendingByType(String type) {
        executePrint("""
            SELECT mb.*
            FROM maintenance_bill mb
            JOIN sites s ON mb.site_id = s.site_id
            WHERE s.site_type = ? AND mb.balance_amount > 0
        """, type);
    }

    public static void viewPendingByOwner(int ownerId) {
        executePrint("""
            SELECT mb.*
            FROM maintenance_bill mb
            JOIN sites s ON mb.site_id = s.site_id
            WHERE s.owner_id = ? AND mb.balance_amount > 0
        """, ownerId);
    }

    public static void viewPendingBySize(double minArea) {
        executePrint("""
            SELECT mb.*
            FROM maintenance_bill mb
            JOIN sites s ON mb.site_id = s.site_id
            WHERE s.area >= ? AND mb.balance_amount > 0
        """, minArea);
    }

    /* =====================================================
       ================= ADMIN COLLECT =====================
       ===================================================== */

    public static void collectFromAll() {
        viewPendingAll();
    }

    public static void collectFromType(String type) {
        viewPendingByType(type);
    }

    public static void collectFromOwner(int ownerId) {
        viewPendingByOwner(ownerId);
    }

    public static void collectFromSize(double area) {
        viewPendingBySize(area);
    }

    /* =====================================================
       ================= TRANSACTIONS ======================
       ===================================================== */

    public static void viewAllTransactions() {
        executePrint("SELECT * FROM payment_transaction ORDER BY payment_date DESC");
    }

    public static void viewTransactionsByOwner(int ownerId) {
        executePrint("""
            SELECT pt.*
            FROM payment_transaction pt
            JOIN maintenance_bill mb ON pt.bill_id = mb.bill_id
            JOIN sites s ON mb.site_id = s.site_id
            WHERE s.owner_id = ?
            ORDER BY pt.payment_date DESC
        """, ownerId);
    }

    /* =====================================================
       ================= PRINT UTILITY =====================
       ===================================================== */

    private static void executePrint(String sql, Object... params) {

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            while (rs.next()) {
                System.out.println("----------------------------");
                for (int i = 1; i <= cols; i++) {
                    System.out.println(meta.getColumnName(i) + " : " + rs.getString(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
