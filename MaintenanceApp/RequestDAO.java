import java.sql.*;

public class RequestDAO {

    /* =====================================================
       ================= CREATE REQUEST ====================
       ===================================================== */

    public static void createSiteTypeRequest(int ownerId, int siteId, String newType) {

        String validateSql = """
            SELECT 1 FROM sites
            WHERE site_id = ? AND owner_id = ?
        """;

        String insertSql = """
            INSERT INTO site_update_request (owner_id, site_id, requested_type)
            VALUES (?, ?, ?)
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement validatePs = con.prepareStatement(validateSql)) {

            validatePs.setInt(1, siteId);
            validatePs.setInt(2, ownerId);

            ResultSet rs = validatePs.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid site or not owned by you.");
                return;
            }

            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                insertPs.setInt(1, ownerId);
                insertPs.setInt(2, siteId);
                insertPs.setString(3, newType);
                insertPs.executeUpdate();
            }

            System.out.println("Site type update request submitted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ================= VIEW OWNER REQUESTS ================
       ===================================================== */

    public static void viewRequestsByOwner(int ownerId) {

        String sql = """
            SELECT request_id, site_id, requested_type, status, request_date
            FROM site_update_request
            WHERE owner_id = ?
            ORDER BY request_date DESC
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("----------------------------");
                System.out.println("Request ID     : " + rs.getInt("request_id"));
                System.out.println("Site ID        : " + rs.getInt("site_id"));
                System.out.println("Requested Type : " + rs.getString("requested_type"));
                System.out.println("Status         : " + rs.getString("status"));
                System.out.println("Date           : " + rs.getTimestamp("request_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ================= VIEW ALL PENDING ==================
       ===================================================== */

    public static void viewAllPendingRequests() {

        String sql = """
            SELECT request_id, owner_id, site_id, requested_type, status, request_date
            FROM site_update_request
            WHERE status = 'PENDING'
            ORDER BY request_date ASC
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println("----------------------------");
                System.out.println("Request ID     : " + rs.getInt("request_id"));
                System.out.println("Owner ID       : " + rs.getInt("owner_id"));
                System.out.println("Site ID        : " + rs.getInt("site_id"));
                System.out.println("Requested Type : " + rs.getString("requested_type"));
                System.out.println("Status         : " + rs.getString("status"));
                System.out.println("Date           : " + rs.getTimestamp("request_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ================= APPROVE REQUEST ===================
       ===================================================== */

    public static void approveRequest(int requestId) {

        String getRequestSql = """
            SELECT site_id, requested_type
            FROM site_update_request
            WHERE request_id = ? AND status = 'PENDING'
        """;

        String updateSiteSql = """
            UPDATE sites
            SET site_type = ?
            WHERE site_id = ?
        """;

        String updateRequestSql = """
            UPDATE site_update_request
            SET status = 'APPROVED'
            WHERE request_id = ?
        """;

        try (Connection con = DBConfig.getConnection()) {

            con.setAutoCommit(false);

            int siteId;
            String newType;

            try (PreparedStatement ps = con.prepareStatement(getRequestSql)) {
                ps.setInt(1, requestId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Request not found or already processed.");
                    return;
                }

                siteId = rs.getInt("site_id");
                newType = rs.getString("requested_type");
            }

            try (PreparedStatement ps1 = con.prepareStatement(updateSiteSql);
                 PreparedStatement ps2 = con.prepareStatement(updateRequestSql)) {

                ps1.setString(1, newType);
                ps1.setInt(2, siteId);
                ps1.executeUpdate();

                ps2.setInt(1, requestId);
                ps2.executeUpdate();

                con.commit();
                System.out.println("Request approved successfully.");

            } catch (Exception e) {
                con.rollback();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ================= REJECT REQUEST ====================
       ===================================================== */

    public static void rejectRequest(int requestId) {

        String sql = """
            UPDATE site_update_request
            SET status = 'REJECTED'
            WHERE request_id = ? AND status = 'PENDING'
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("Request not found or already processed.");
            } else {
                System.out.println("Request rejected successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
