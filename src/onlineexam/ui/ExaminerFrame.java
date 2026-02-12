package onlineexam.ui;

import javax.swing.*;

public class ExaminerFrame extends JFrame {

    public ExaminerFrame() {
        setTitle("Examiner Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Examiner Dashboard (Phase 2)", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}
