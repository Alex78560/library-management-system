import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:database/library.db";

    public static Connection getConnection() {

        try {

            File folder = new File("database");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            Connection connection =
                    DriverManager.getConnection(URL);

            return connection;

        } catch (SQLException e) {

            System.out.println("Database connection failed.");
            e.printStackTrace();

            return null;
        }
    }
}