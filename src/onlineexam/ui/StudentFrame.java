package onlineexam.ui;

import java.awt.*;
import javax.swing.*;

public class StudentFrame extends JFrame {

    int sId;
    JButton startExamButton;

    public StudentFrame(int sID) {
        this.sId = sID;
        setTitle("Student Dashboard");
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startExamButton = new JButton("Start Exam");

        startExamButton.addActionListener(e -> {
            new StartExamFrame(sId);
            dispose();
        });

        setLayout(new FlowLayout());

        add(startExamButton);

        setVisible(true);
    }
}