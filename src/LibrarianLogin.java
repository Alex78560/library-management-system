import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class LibrarianLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LibrarianLogin() {

        setTitle("Librarian Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JLabel title =
                new JLabel(
                        "LIBRARIAN LOGIN",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        usernameField =
                new JTextField();

        passwordField =
                new JPasswordField();

        JButton loginButton =
                new JButton("LOGIN");

        JButton backButton =
                new JButton("BACK");

        panel.add(title);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        add(panel);

        loginButton.addActionListener(e -> login());

        backButton.addActionListener(e -> {

            dispose();

            new LoginFrame();

        });

        setVisible(true);
    }

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        try {

            ResultSet rs =
                    DatabaseManager.login(
                            username,
                            password,
                            "LIBRARIAN"
                    );

            if (rs != null && rs.next()) {

                int userId =
                        rs.getInt("id");

                dispose();

                new LibrarianDashboard();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Login error: " +
                    e.getMessage()
            );
        }
    }
}