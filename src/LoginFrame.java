import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Library Management System");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel(
                "LIBRARY MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton userButton =
                new JButton("USER LOGIN");

        JButton librarianButton =
                new JButton("LIBRARIAN LOGIN");

        JButton exitButton =
                new JButton("EXIT");

        panel.add(title);
        panel.add(userButton);
        panel.add(librarianButton);
        panel.add(exitButton);

        add(panel);

        userButton.addActionListener(e -> {

            dispose();

            new UserLogin();

        });

        librarianButton.addActionListener(e -> {

            dispose();

            new LibrarianLogin();

        });

        exitButton.addActionListener(e -> {

            System.exit(0);

        });

        setVisible(true);
    }
}