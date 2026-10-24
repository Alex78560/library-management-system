public class Main {

    public static void main(String[] args) {

        DatabaseSetup.setupDatabase();

        javax.swing.SwingUtilities.invokeLater(() -> {

            new LoginFrame();

        });
    }
}