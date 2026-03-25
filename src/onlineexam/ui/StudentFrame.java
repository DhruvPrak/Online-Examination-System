package onlineexam.ui;

import java.awt.*;
import javax.swing.*;

public class StudentFrame extends JFrame {

    int sId;
    JButton startExamButton;
    JButton viewResultsButton;

    public StudentFrame(int sID) {
        this.sId = sID;
        setTitle("Student Dashboard");
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startExamButton = new JButton("Start Exam");
        viewResultsButton = new JButton("View Results");

        startExamButton.addActionListener(e -> {
            new StartExamFrame(sId);
            dispose();
        });

        viewResultsButton.addActionListener(e -> {
            new StudentResultsFrame(sId);
            dispose();
        });

        setLayout(new FlowLayout());

        add(startExamButton);
        add(viewResultsButton);

        setVisible(true);
    }
}