import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void setupDatabase() {

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            Statement statement =
                    connection.createStatement();

            // Users table
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "course TEXT," +
                "contact TEXT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL" +
                ")"
            );

            // Books table
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "quantity INTEGER NOT NULL" +
                ")"
            );

            // Issues table
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS issues (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "book_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "issue_date TEXT NOT NULL," +
                "due_date TEXT NOT NULL," +
                "return_date TEXT," +
                "fine INTEGER DEFAULT 0" +
                ")"
            );

            // Default librarian account
            statement.executeUpdate(
                "INSERT OR IGNORE INTO users " +
                "(name, course, contact, username, password, role) " +
                "VALUES " +
                "('Administrator', 'Library', '0000000000', " +
                "'admin', '1234', 'LIBRARIAN')"
            );

            // Default user account
            statement.executeUpdate(
                "INSERT OR IGNORE INTO users " +
                "(name, course, contact, username, password, role) " +
                "VALUES " +
                "('Demo User', 'BTech CSE', '9999999999', " +
                "'user', '1234', 'USER')"
            );

            connection.close();

            System.out.println("Database setup completed.");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}