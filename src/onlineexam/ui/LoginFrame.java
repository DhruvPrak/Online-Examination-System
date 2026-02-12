package onlineexam.ui;

import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Online Examination System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("Select Login Role", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JButton adminBtn = new JButton("Admin Login");
        JButton examinerBtn = new JButton("Examiner Login");
        JButton studentBtn = new JButton("Student Login");

        /*adminBtn.addActionListener(e -> new AdminFrame());
        examinerBtn.addActionListener(e -> new ExaminerFrame());
        studentBtn.addActionListener(e -> new StudentFrame());*/

        adminBtn.addActionListener(e->{
            new AdminFrame();
            dispose();
        });
        examinerBtn.addActionListener(e->{
            new ExaminerFrame();
            dispose();
        });
        studentBtn.addActionListener(e->{
            new StudentFrame();
            dispose();
        });

        panel.add(title);
        panel.add(adminBtn);
        panel.add(examinerBtn);
        panel.add(studentBtn);

        add(panel);
        setVisible(true);
    }
}
