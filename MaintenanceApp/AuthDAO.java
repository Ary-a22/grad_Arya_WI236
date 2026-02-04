import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    public static boolean validateAdmin(int adminId, String password) {
        String sql = """
            SELECT 1 FROM auth
            WHERE user_id = ? AND role = 'ADMIN'
              AND password = ? AND is_active = TRUE
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateOwner(int ownerId, String password) {
        String sql = """
            SELECT 1 FROM auth
            WHERE user_id = ? AND role = 'OWNER'
              AND password = ? AND is_active = TRUE
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean changePassword(
            int ownerId,
            String currentPassword,
            String newPassword) {

        String sql = """
            UPDATE auth
            SET password = ?
            WHERE user_id = ? AND role = 'OWNER'
              AND password = ?
        """;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, ownerId);
            ps.setString(3, currentPassword);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
