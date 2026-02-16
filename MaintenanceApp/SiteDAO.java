import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SiteDAO {

    public static void viewSiteById(int siteId) {

        String sql = "SELECT * FROM sites WHERE site_id = ?";

        try (Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Site not found.");
                return;
            }

            System.out.println("----------------------------");
            System.out.println("Site ID : " + rs.getInt("site_id"));
            System.out.println("Type    : " + rs.getString("site_type"));
            System.out.println("Length  : " + rs.getBigDecimal("length"));
            System.out.println("Breadth : " + rs.getBigDecimal("breadth"));
            System.out.println("Area    : " + rs.getBigDecimal("area"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* ================= ASSIGN SITE ================= */

    public static boolean assignSite(int siteId, int ownerId) {

        String checkSiteSql =
                "SELECT owner_id FROM sites WHERE site_id = ?";
        String checkOwnerSql =
                "SELECT owner_id FROM owners WHERE owner_id = ?";
        String assignSql =
                "UPDATE sites SET owner_id = ? WHERE site_id = ? AND owner_id IS NULL";

        try (Connection con = DBConfig.getConnection()) {

            // 1️⃣ Check site exists
            try (PreparedStatement ps = con.prepareStatement(checkSiteSql)) {
                ps.setInt(1, siteId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Site does not exist");
                    return false;
                }

                if (rs.getInt("owner_id") != 0) {
                    System.out.println("Site is already assigned to an owner");
                    return false;
                }
            }

            // 2️⃣ Check owner exists
            try (PreparedStatement ps = con.prepareStatement(checkOwnerSql)) {
                ps.setInt(1, ownerId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Owner does not exist");
                    return false;
                }
            }

            // 3️⃣ Assign site
            try (PreparedStatement ps = con.prepareStatement(assignSql)) {
                ps.setInt(1, ownerId);
                ps.setInt(2, siteId);

                int rows = ps.executeUpdate();
                return rows == 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /* ================= UNASSIGN SITE ================= */

    public static boolean unassignSite(int siteId) {

        String checkSql =
                "SELECT owner_id FROM sites WHERE site_id = ?";
        String unassignSql =
                "UPDATE sites SET owner_id = NULL WHERE site_id = ? AND owner_id IS NOT NULL";

        try (Connection con = DBConfig.getConnection()) {

            // 1️⃣ Check site exists & is owned
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, siteId);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Site does not exist");
                    return false;
                }

                if (rs.getInt("owner_id") == 0) {
                    System.out.println("Site is already unassigned");
                    return false;
                }
            }

            // 2️⃣ Unassign
            try (PreparedStatement ps = con.prepareStatement(unassignSql)) {
                ps.setInt(1, siteId);
                int rows = ps.executeUpdate();
                return rows == 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /* ================= VIEW ALL SITES ================= */

    public static void viewAllSites() {

        String sql = """
            SELECT site_id, site_type, length, breadth, area, owner_id
            FROM sites
            ORDER BY site_id
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- ALL SITES ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                printSite(rs);
            }

            if (!found) {
                System.out.println("No sites found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= FILTER BY TYPE ================= */

    public static void viewSitesByType(String type) {

        String sql = """
            SELECT site_id, site_type, length, breadth, area, owner_id
            FROM sites
            WHERE LOWER(site_type) = LOWER(?)
            ORDER BY site_id
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- SITES BY TYPE ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                printSite(rs);
            }

            if (!found) {
                System.out.println("No sites found for type: " + type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= FILTER BY OWNERSHIP ================= */

    public static void viewSitesByOwnership(boolean owned) {

        String sql = owned
                ? "SELECT site_id, site_type, length, breadth, area, owner_id FROM sites WHERE owner_id IS NOT NULL ORDER BY site_id"
                : "SELECT site_id, site_type, length, breadth, area, owner_id FROM sites WHERE owner_id IS NULL ORDER BY site_id";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(owned
                    ? "\n--- OWNED SITES ---"
                    : "\n--- UNOWNED SITES ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                printSite(rs);
            }

            if (!found) {
                System.out.println("No matching sites found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= COMMON PRINT ================= */

    private static void printSite(ResultSet rs) throws SQLException {
        System.out.println("----------------------------");
        System.out.println("Site ID   : " + rs.getInt("site_id"));
        System.out.println("Type      : " + rs.getString("site_type"));
        System.out.println("Length    : " + rs.getBigDecimal("length"));
        System.out.println("Breadth   : " + rs.getBigDecimal("breadth"));
        System.out.println("Area      : " + rs.getBigDecimal("area"));

        int ownerId = rs.getInt("owner_id");
        if (rs.wasNull()) {
            System.out.println("Owner     : Unowned");
        } else {
            System.out.println("Owner ID  : " + ownerId);
        }
    }

    /* ================= ASSIGN / UNASSIGN (ALREADY DONE) ================= */
    // keep your assignSite() and unassignSite() methods here

        public static void viewSitesByOwner(int ownerId) {

        String sql = """
            SELECT site_id, site_type, length, breadth, area
            FROM sites
            WHERE owner_id = ?
            ORDER BY site_id
        """;

        try (Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- MY SITES ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("----------------------------");
                System.out.println("Site ID  : " + rs.getInt("site_id"));
                System.out.println("Type     : " + rs.getString("site_type"));
                System.out.println("Length   : " + rs.getBigDecimal("length"));
                System.out.println("Breadth  : " + rs.getBigDecimal("breadth"));
                System.out.println("Area     : " + rs.getBigDecimal("area"));
            }

            if (!found) {
                System.out.println("No sites assigned to you");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
