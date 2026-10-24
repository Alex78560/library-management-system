import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

public class UserLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public UserLogin() {

        setTitle("User Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JLabel title =
                new JLabel(
                        "USER LOGIN",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        usernameField =
                new JTextField();

        passwordField =
                new JPasswordField();

        loginButton =
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

        char[] passwordCharacters = passwordField.getPassword();
        String password = new String(passwordCharacters);
        java.util.Arrays.fill(passwordCharacters, '\0');

        if (username.isEmpty() || password.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            return;
        }

        loginButton.setEnabled(false);
        new SwingWorker<ResultSet, Void>() {
            @Override
            protected ResultSet doInBackground() throws Exception {
                return DatabaseManager.login(username, password, "USER");
            }

            @Override
            protected void done() {
                try (ResultSet rs = get()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        JOptionPane.showMessageDialog(
                                UserLogin.this,
                                "Login successful!"
                        );
                        dispose();
                        new UserDashboard(userId);
                    } else {
                        JOptionPane.showMessageDialog(
                                UserLogin.this,
                                "Invalid username or password."
                        );
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(
                            UserLogin.this,
                            "Login was interrupted."
                    );
                } catch (ExecutionException | java.sql.SQLException e) {
                    Throwable cause = e instanceof ExecutionException
                            ? e.getCause()
                            : e;
                    cause.printStackTrace();
                    JOptionPane.showMessageDialog(
                            UserLogin.this,
                            "Unable to log in: " + cause.getMessage()
                    );
                } finally {
                    loginButton.setEnabled(true);
                }
            }
        }.execute();
    }
}