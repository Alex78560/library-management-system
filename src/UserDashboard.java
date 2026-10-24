import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class UserDashboard extends JFrame {

    private int userId;

    public UserDashboard(int userId) {

        this.userId = userId;

        setTitle("User Dashboard");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));

        JLabel title = new JLabel(
                "USER DASHBOARD",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton viewBooksButton =
                new JButton("VIEW BOOKS");

        JButton searchBookButton =
                new JButton("SEARCH BOOK");

        JButton borrowBookButton =
                new JButton("BORROW BOOK");

        JButton returnBookButton =
                new JButton("RETURN BOOK");

        JButton myBooksButton =
                new JButton("MY ISSUED BOOKS");

        JButton logoutButton =
                new JButton("LOGOUT");

        panel.add(title);
        panel.add(viewBooksButton);
        panel.add(searchBookButton);
        panel.add(borrowBookButton);
        panel.add(returnBookButton);
        panel.add(myBooksButton);
        panel.add(logoutButton);

        add(panel);

        // View Books
        viewBooksButton.addActionListener(e -> viewBooks());

        // Search Book
        searchBookButton.addActionListener(e -> searchBook());

        // Borrow Book
        borrowBookButton.addActionListener(e -> borrowBook());

        // Return Book
        returnBookButton.addActionListener(e -> returnBook());

        // My Issued Books
        myBooksButton.addActionListener(e -> viewMyBooks());

        // Logout
        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame();

        });

        setVisible(true);
    }

    private void viewBooks() {

        try {

            ResultSet rs =
                    DatabaseManager.getBooks();

            StringBuilder books =
                    new StringBuilder();

            while (rs != null && rs.next()) {

                books.append("ID: ")
                     .append(rs.getInt("id"))
                     .append("\n");

                books.append("Title: ")
                     .append(rs.getString("title"))
                     .append("\n");

                books.append("Author: ")
                     .append(rs.getString("author"))
                     .append("\n");

                books.append("Available: ")
                     .append(rs.getInt("quantity"))
                     .append("\n");

                books.append("----------------------\n");
            }

            if (books.length() == 0) {
                books.append("No books available.");
            }

            JTextArea area =
                    new JTextArea(books.toString());

            area.setEditable(false);

            JScrollPane scrollPane =
                    new JScrollPane(area);

            scrollPane.setPreferredSize(
                    new Dimension(400, 300)
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Available Books",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load books."
            );
        }
    }

    private void searchBook() {

        String search =
                JOptionPane.showInputDialog(
                        this,
                        "Enter book title:"
                );

        if (search == null || search.trim().isEmpty()) {
            return;
        }

        try {

            ResultSet rs =
                    DatabaseManager.getBooks();

            StringBuilder result =
                    new StringBuilder();

            while (rs != null && rs.next()) {

                String title =
                        rs.getString("title");

                if (title.toLowerCase()
                        .contains(search.toLowerCase())) {

                    result.append("ID: ")
                          .append(rs.getInt("id"))
                          .append("\n");

                    result.append("Title: ")
                          .append(title)
                          .append("\n");

                    result.append("Author: ")
                          .append(rs.getString("author"))
                          .append("\n");

                    result.append("Available: ")
                          .append(rs.getInt("quantity"))
                          .append("\n");

                    result.append("----------------------\n");
                }
            }

            if (result.length() == 0) {
                result.append("Book not found.");
            }

            JOptionPane.showMessageDialog(
                    this,
                    result.toString(),
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed."
            );
        }
    }

    private void borrowBook() {

        String bookIdText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Book ID:"
                );

        if (bookIdText == null) {
            return;
        }

        try {

            int bookId =
                    Integer.parseInt(bookIdText);

            boolean success =
                    DatabaseManager.issueBook(
                            bookId,
                            userId
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book borrowed successfully.\n" +
                        "Due date is 14 days from today."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Book unavailable or invalid Book ID."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Book ID."
            );
        }
    }

    private void returnBook() {

        String issueIdText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Issue ID:"
                );

        if (issueIdText == null) {
            return;
        }

        try {

            int issueId =
                    Integer.parseInt(issueIdText);

            int fine =
                    DatabaseManager.returnBook(issueId);

            if (fine >= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book returned successfully.\n" +
                        "Fine: ₹" + fine
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Issue ID or book already returned."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Issue ID."
            );
        }
    }

    private void viewMyBooks() {

        try {

            ResultSet rs =
                    DatabaseManager.getIssues();

            StringBuilder result =
                    new StringBuilder();

            while (rs != null && rs.next()) {

                if (rs.getInt("user_id") == userId) {

                    result.append("Issue ID: ")
                          .append(rs.getInt("id"))
                          .append("\n");

                    result.append("Book ID: ")
                          .append(rs.getInt("book_id"))
                          .append("\n");

                    result.append("Issue Date: ")
                          .append(rs.getString("issue_date"))
                          .append("\n");

                    result.append("Due Date: ")
                          .append(rs.getString("due_date"))
                          .append("\n");

                    result.append("Return Date: ")
                          .append(rs.getString("return_date"))
                          .append("\n");

                    result.append("Fine: ₹")
                          .append(rs.getInt("fine"))
                          .append("\n");

                    result.append("----------------------\n");
                }
            }

            if (result.length() == 0) {
                result.append("You have no issued books.");
            }

            JOptionPane.showMessageDialog(
                    this,
                    result.toString(),
                    "My Issued Books",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load your books."
            );
        }
    }
}