import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class DatabaseManager {

    // =========================
    // LOGIN
    // =========================

    public static ResultSet login(
            String username,
            String password,
            String role) throws SQLException {

        String sql =
                "SELECT * FROM users " +
                "WHERE username = ? " +
                "AND password = ? " +
                "AND role = ?";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    connection.prepareStatement(sql)
        ) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            try (ResultSet rs = ps.executeQuery()) {

                CachedRowSet cached =
                        RowSetProvider.newFactory()
                                .createCachedRowSet();

                cached.populate(rs);

                return cached;
            }

        }
    }


    // =========================
    // ADD BOOK
    // =========================

    public static boolean addBook(
            String title,
            String author,
            int quantity) {

        String sql =
                "INSERT INTO books " +
                "(title, author, quantity) " +
                "VALUES (?, ?, ?)";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    connection.prepareStatement(sql)
        ) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================
    // DELETE BOOK
    // =========================

    public static boolean deleteBook(int id) {

        String sql =
                "DELETE FROM books WHERE id = ?";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    connection.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================
    // ADD USER
    // =========================

    public static boolean addUser(
            String name,
            String course,
            String contact,
            String username,
            String password) {

        String sql =
                "INSERT INTO users " +
                "(name, course, contact, username, password, role) " +
                "VALUES (?, ?, ?, ?, ?, 'USER')";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    connection.prepareStatement(sql)
        ) {

            ps.setString(1, name);
            ps.setString(2, course);
            ps.setString(3, contact);
            ps.setString(4, username);
            ps.setString(5, password);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================
    // DELETE USER
    // =========================

    public static boolean deleteUser(int id) {

        String sql =
                "DELETE FROM users " +
                "WHERE id = ? AND role = 'USER'";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement ps =
                    connection.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================
    // ISSUE BOOK
    // =========================

    public static boolean issueBook(
            int bookId,
            int userId) {

        String checkSql =
                "SELECT quantity FROM books WHERE id = ?";

        String insertSql =
                "INSERT INTO issues " +
                "(book_id, user_id, issue_date, due_date) " +
                "VALUES (?, ?, ?, ?)";

        String updateSql =
                "UPDATE books " +
                "SET quantity = quantity - 1 " +
                "WHERE id = ?";

        try (
            Connection connection =
                    DatabaseConnection.getConnection()
        ) {

            connection.setAutoCommit(false);

            try (
                PreparedStatement check =
                        connection.prepareStatement(checkSql)
            ) {

                check.setInt(1, bookId);

                try (ResultSet rs =
                        check.executeQuery()) {

                    if (!rs.next() ||
                        rs.getInt("quantity") <= 0) {

                        connection.rollback();
                        return false;
                    }
                }
            }

            LocalDate issueDate =
                    LocalDate.now();

            LocalDate dueDate =
                    issueDate.plusDays(14);

            try (
                PreparedStatement insert =
                        connection.prepareStatement(insertSql)
            ) {

                insert.setInt(1, bookId);
                insert.setInt(2, userId);
                insert.setString(
                        3,
                        issueDate.toString()
                );
                insert.setString(
                        4,
                        dueDate.toString()
                );

                insert.executeUpdate();
            }

            try (
                PreparedStatement update =
                        connection.prepareStatement(updateSql)
            ) {

                update.setInt(1, bookId);
                update.executeUpdate();
            }

            connection.commit();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // =========================
    // RETURN BOOK
    // =========================

    public static int returnBook(
            int issueId) {

        String selectSql =
                "SELECT * FROM issues " +
                "WHERE id = ? " +
                "AND return_date IS NULL";

        String updateIssueSql =
                "UPDATE issues SET " +
                "return_date = ?, fine = ? " +
                "WHERE id = ?";

        String updateBookSql =
                "UPDATE books " +
                "SET quantity = quantity + 1 " +
                "WHERE id = ?";

        try (
            Connection connection =
                    DatabaseConnection.getConnection()
        ) {

            connection.setAutoCommit(false);

            int bookId;
            LocalDate dueDate;

            try (
                PreparedStatement ps =
                        connection.prepareStatement(selectSql)
            ) {

                ps.setInt(1, issueId);

                try (ResultSet rs =
                        ps.executeQuery()) {

                    if (!rs.next()) {

                        connection.rollback();
                        return -1;
                    }

                    bookId =
                            rs.getInt("book_id");

                    dueDate =
                            LocalDate.parse(
                                    rs.getString(
                                            "due_date"
                                    )
                            );
                }
            }

            LocalDate returnDate =
                    LocalDate.now();

            long lateDays =
                    ChronoUnit.DAYS.between(
                            dueDate,
                            returnDate
                    );

            int fine = 0;

            if (lateDays > 0) {
                fine = (int) lateDays * 5;
            }

            try (
                PreparedStatement updateIssue =
                        connection.prepareStatement(
                                updateIssueSql
                        )
            ) {

                updateIssue.setString(
                        1,
                        returnDate.toString()
                );

                updateIssue.setInt(
                        2,
                        fine
                );

                updateIssue.setInt(
                        3,
                        issueId
                );

                updateIssue.executeUpdate();
            }

            try (
                PreparedStatement updateBook =
                        connection.prepareStatement(
                                updateBookSql
                        )
            ) {

                updateBook.setInt(
                        1,
                        bookId
                );

                updateBook.executeUpdate();
            }

            connection.commit();

            return fine;

        } catch (SQLException e) {

            e.printStackTrace();
            return -1;
        }
    }


    // =========================
    // GET BOOKS
    // =========================

    public static ResultSet getBooks() {

        String sql =
                "SELECT * FROM books";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            Statement statement =
                    connection.createStatement();

            ResultSet rs =
                    statement.executeQuery(sql)
        ) {

            CachedRowSet cached =
                    RowSetProvider.newFactory()
                            .createCachedRowSet();

            cached.populate(rs);

            return cached;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }


    // =========================
    // GET USERS
    // =========================

    public static ResultSet getUsers() {

        String sql =
                "SELECT id, name, course, contact, username " +
                "FROM users WHERE role = 'USER'";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            Statement statement =
                    connection.createStatement();

            ResultSet rs =
                    statement.executeQuery(sql)
        ) {

            CachedRowSet cached =
                    RowSetProvider.newFactory()
                            .createCachedRowSet();

            cached.populate(rs);

            return cached;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }


    // =========================
    // GET ISSUES
    // =========================

    public static ResultSet getIssues() {

        String sql =
                "SELECT * FROM issues";

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            Statement statement =
                    connection.createStatement();

            ResultSet rs =
                    statement.executeQuery(sql)
        ) {

            CachedRowSet cached =
                    RowSetProvider.newFactory()
                            .createCachedRowSet();

            cached.populate(rs);

            return cached;

        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }
} 