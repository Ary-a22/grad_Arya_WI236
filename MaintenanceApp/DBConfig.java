import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/LayoutManagementSystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Zry@2004";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
