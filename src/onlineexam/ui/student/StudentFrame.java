package onlineexam.ui.student;

import java.awt.*;
import javax.swing.*;
import onlineexam.ui.LoginFrame;

public class StudentFrame extends JFrame {


int sId;

public StudentFrame(int sID) {

    this.sId = sID;

    setTitle("Student Dashboard");
    setSize(400,200);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton logout = new JButton("Logout");

    logout.addActionListener(e -> {
        dispose();
        new LoginFrame();
    });

    top.add(logout);
    add(top, BorderLayout.NORTH);

    JPanel panel = new JPanel();

    JButton start = new JButton("Start Exam");
    JButton result = new JButton("View Results");

    start.addActionListener(e -> {
        new StartExamFrame(sId);
        dispose();
    });

    result.addActionListener(e -> {
        new StudentResultsFrame(sId);
        dispose();
    });

    panel.add(start);
    panel.add(result);

    add(panel, BorderLayout.CENTER);

    setVisible(true);
}


}
