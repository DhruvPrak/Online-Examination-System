package onlineexam.ui;

import javax.swing.*;

public class StudentFrame extends JFrame {

    public StudentFrame() {
        setTitle("Student Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Student Dashboard (Phase 2)", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}
