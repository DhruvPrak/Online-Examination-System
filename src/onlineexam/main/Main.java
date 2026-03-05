package onlineexam.main;
import javax.swing.SwingUtilities;
import onlineexam.ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });

    }
}