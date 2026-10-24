import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class LibrarianDashboard extends JFrame {

    public LibrarianDashboard() {

        setTitle("Librarian Dashboard");
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1, 10, 10));

        JLabel title = new JLabel(
                "LIBRARIAN DASHBOARD",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton addBookButton =
                new JButton("ADD BOOK");

        JButton removeBookButton =
                new JButton("REMOVE BOOK");

        JButton viewBooksButton =
                new JButton("VIEW BOOKS");

        JButton addUserButton =
                new JButton("ADD USER");

        JButton viewUsersButton =
                new JButton("VIEW USERS");

        JButton deleteUserButton =
                new JButton("DELETE USER");

        JButton viewIssuesButton =
                new JButton("VIEW ISSUES");

        JButton logoutButton =
                new JButton("LOGOUT");

        panel.add(title);
        panel.add(addBookButton);
        panel.add(removeBookButton);
        panel.add(viewBooksButton);
        panel.add(addUserButton);
        panel.add(viewUsersButton);
        panel.add(deleteUserButton);
        panel.add(viewIssuesButton);
        panel.add(logoutButton);

        add(panel);

        addBookButton.addActionListener(e -> addBook());

        removeBookButton.addActionListener(e -> removeBook());

        viewBooksButton.addActionListener(e -> viewBooks());

        addUserButton.addActionListener(e -> addUser());

        viewUsersButton.addActionListener(e -> viewUsers());

        deleteUserButton.addActionListener(e -> deleteUser());

        viewIssuesButton.addActionListener(e -> viewIssues());

        logoutButton.addActionListener(e -> {

            dispose();

            new LoginFrame();

        });

        setVisible(true);
    }

    private void addBook() {

        JTextField titleField =
                new JTextField();

        JTextField authorField =
                new JTextField();

        JTextField quantityField =
                new JTextField();

        Object[] fields = {

                "Book Title:",
                titleField,

                "Author:",
                authorField,

                "Quantity:",
                quantityField
        };

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        fields,
                        "Add Book",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result == JOptionPane.OK_OPTION) {

            try {

                String title =
                        titleField.getText();

                String author =
                        authorField.getText();

                int quantity =
                        Integer.parseInt(
                                quantityField.getText()
                        );

                if (title.isEmpty() ||
                    author.isEmpty() ||
                    quantity <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please enter valid details."
                    );

                    return;
                }

                boolean success =
                        DatabaseManager.addBook(
                                title,
                                author,
                                quantity
                        );

                if (success) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Book added successfully."
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Unable to add book."
                    );
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be a number."
                );
            }
        }
    }

    private void removeBook() {

        String idText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter Book ID to remove:"
                );

        if (idText == null) {
            return;
        }

        try {

            int id =
                    Integer.parseInt(idText);

            boolean success =
                    DatabaseManager.deleteBook(id);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book removed successfully."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Book ID not found."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Book ID."
            );
        }
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

                books.append("Quantity: ")
                     .append(rs.getInt("quantity"))
                     .append("\n");

                books.append("----------------------\n");
            }

            if (books.length() == 0) {
                books.append("No books available.");
            }

            JTextArea area =
                    new JTextArea(
                            books.toString()
                    );

            area.setEditable(false);

            JScrollPane scrollPane =
                    new JScrollPane(area);

            scrollPane.setPreferredSize(
                    new Dimension(450, 300)
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Books",
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

    private void addUser() {

        JTextField nameField =
                new JTextField();

        JTextField courseField =
                new JTextField();

        JTextField contactField =
                new JTextField();

        JTextField usernameField =
                new JTextField();

        JPasswordField passwordField =
                new JPasswordField();

        Object[] fields = {

                "Name:",
                nameField,

                "Course:",
                courseField,

                "Contact:",
                contactField,

                "Username:",
                usernameField,

                "Password:",
                passwordField
        };

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        fields,
                        "Add User",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result == JOptionPane.OK_OPTION) {

            try {

                String name =
                        nameField.getText();

                String course =
                        courseField.getText();

                String contact =
                        contactField.getText();

                String username =
                        usernameField.getText();

                String password =
                        new String(
                                passwordField.getPassword()
                        );

                if (name.isEmpty() ||
                    username.isEmpty() ||
                    password.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Name, username and password are required."
                    );

                    return;
                }

                boolean success =
                        DatabaseManager.addUser(
                                name,
                                course,
                                contact,
                                username,
                                password
                        );

                if (success) {

                    JOptionPane.showMessageDialog(
                            this,
                            "User added successfully."
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Unable to add user.\n" +
                            "Username may already exist."
                    );
                }

            } catch (Exception e) {

                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to add user."
                );
            }
        }
    }

    private void viewUsers() {

        try {

            ResultSet rs =
                    DatabaseManager.getUsers();

            StringBuilder users =
                    new StringBuilder();

            while (rs != null && rs.next()) {

                users.append("ID: ")
                     .append(rs.getInt("id"))
                     .append("\n");

                users.append("Name: ")
                     .append(rs.getString("name"))
                     .append("\n");

                users.append("Course: ")
                     .append(rs.getString("course"))
                     .append("\n");

                users.append("Contact: ")
                     .append(rs.getString("contact"))
                     .append("\n");

                users.append("Username: ")
                     .append(rs.getString("username"))
                     .append("\n");

                users.append("----------------------\n");
            }

            if (users.length() == 0) {
                users.append("No users found.");
            }

            JTextArea area =
                    new JTextArea(
                            users.toString()
                    );

            area.setEditable(false);

            JScrollPane scrollPane =
                    new JScrollPane(area);

            scrollPane.setPreferredSize(
                    new Dimension(450, 300)
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Users",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load users."
            );
        }
    }

    private void deleteUser() {

        String idText =
                JOptionPane.showInputDialog(
                        this,
                        "Enter User ID to delete:"
                );

        if (idText == null) {
            return;
        }

        try {

            int id =
                    Integer.parseInt(idText);

            boolean success =
                    DatabaseManager.deleteUser(id);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "User deleted successfully."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "User ID not found."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid User ID."
            );
        }
    }

    private void viewIssues() {

        try {

            ResultSet rs =
                    DatabaseManager.getIssues();

            StringBuilder issues =
                    new StringBuilder();

            while (rs != null && rs.next()) {

                issues.append("Issue ID: ")
                      .append(rs.getInt("id"))
                      .append("\n");

                issues.append("Book ID: ")
                      .append(rs.getInt("book_id"))
                      .append("\n");

                issues.append("User ID: ")
                      .append(rs.getInt("user_id"))
                      .append("\n");

                issues.append("Issue Date: ")
                      .append(rs.getString("issue_date"))
                      .append("\n");

                issues.append("Due Date: ")
                      .append(rs.getString("due_date"))
                      .append("\n");

                issues.append("Return Date: ")
                      .append(rs.getString("return_date"))
                      .append("\n");

                issues.append("Fine: ₹")
                      .append(rs.getInt("fine"))
                      .append("\n");

                issues.append("----------------------\n");
            }

            if (issues.length() == 0) {
                issues.append("No issue records found.");
            }

            JTextArea area =
                    new JTextArea(
                            issues.toString()
                    );

            area.setEditable(false);

            JScrollPane scrollPane =
                    new JScrollPane(area);

            scrollPane.setPreferredSize(
                    new Dimension(450, 350)
            );

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Book Issues",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load issue records."
            );
        }
    }
}