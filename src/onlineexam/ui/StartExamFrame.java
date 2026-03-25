package onlineexam.ui;

import java.awt.*;
import javax.swing.*;

public class StartExamFrame extends JFrame {

    int sId;
    JTextField examIdField;
    JButton startButton;

    public StartExamFrame(int sId) {
        this.sId = sId;
        setTitle("Start Exam");
        setSize(400,200);
        setLocationRelativeTo(null);

        JLabel examLabel = new JLabel("Enter Exam ID:");

        examIdField = new JTextField(10);
        startButton = new JButton("Start");

        startButton.addActionListener(e -> {

            int examId = Integer.parseInt(examIdField.getText());

            new ExamFrame(sId, examId);
            dispose();
        });

        setLayout(new FlowLayout());
        add(examLabel);
        add(examIdField);
        add(startButton);

        setVisible(true);
    }
}