import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerDAO {

    /* ================= View all oWners ================= */
    public static void viewAllOwners() {

        String sql = "SELECT owner_id, name, phone, email FROM owners ORDER BY owner_id";

        try (Connection con = DBConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- ALL OWNERS ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("---------------------------");
                System.out.println("ID    : " + rs.getInt("owner_id"));
                System.out.println("Name  : " + rs.getString("name"));
                System.out.println("Phone : " + rs.getString("phone"));
                System.out.println("Email : " + rs.getString("email"));
            }

            if (!found) {
                System.out.println("No owners found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= ADD OWNER ================= */

    public static boolean addOwner(
            int ownerId,
            String name,
            String phone,
            String email,
            String password) {

        String ownerSql =
                "INSERT INTO owners (owner_id, name, phone, email) VALUES (?, ?, ?, ?)";
        String authSql =
                "INSERT INTO auth (user_id, role, password) VALUES (?, 'OWNER', ?)";

        try (Connection con = DBConfig.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(ownerSql);
                 PreparedStatement ps2 = con.prepareStatement(authSql)) {

                ps1.setInt(1, ownerId);
                ps1.setString(2, name);
                ps1.setString(3, phone);
                ps1.setString(4, email);
                ps1.executeUpdate();

                ps2.setInt(1, ownerId);
                ps2.setString(2, password);
                ps2.executeUpdate();

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= VIEW OWNER PROFILE ================= */

    public static void viewOwnerProfile(int ownerId) {
        String sql =
                "SELECT owner_id, name, phone, email FROM owners WHERE owner_id = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- OWNER PROFILE ---");
                System.out.println("ID    : " + rs.getInt("owner_id"));
                System.out.println("Name  : " + rs.getString("name"));
                System.out.println("Phone : " + rs.getString("phone"));
                System.out.println("Email : " + rs.getString("email"));
            } else {
                System.out.println("Owner not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= EDIT OWNER ================= */

    public static boolean editOwner(
            int ownerId,
            String name,
            String phone,
            String email) {

        String sql =
                "UPDATE owners SET name = ?, phone = ?, email = ? WHERE owner_id = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setInt(4, ownerId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= GET OWNER BY ID ================= */

    public static Owner getOwnerById(int ownerId) {

        String sql =
                "SELECT owner_id, name, phone, email FROM owners WHERE owner_id = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Owner(
                        rs.getInt("owner_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ================= REMOVE OWNER ================= */

    public static boolean removeOwner(int ownerId) {

        String authSql = "DELETE FROM auth WHERE user_id = ? AND role = 'OWNER'";
        String ownerSql = "DELETE FROM owners WHERE owner_id = ?";

        try (Connection con = DBConfig.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(authSql);
                 PreparedStatement ps2 = con.prepareStatement(ownerSql)) {

                ps1.setInt(1, ownerId);
                ps2.setInt(1, ownerId);

                ps1.executeUpdate();
                int ownerDeleted = ps2.executeUpdate();

                con.commit();
                return ownerDeleted == 1;

            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
