package onlineexam.ui;

import javax.swing.*;

public class AdminFrame extends JFrame {

    public AdminFrame() {
        setTitle("Admin Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Admin Dashboard (Phase 2)", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}
