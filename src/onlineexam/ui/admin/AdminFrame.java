package onlineexam.ui.admin;

import javax.swing.*;
import java.awt.*;


public class AdminFrame extends JFrame {

    public AdminFrame() {

        setTitle("Admin Dashboard");
        setSize(400,250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,1,10,10));

        JLabel title = new JLabel("Admin Dashboard", JLabel.CENTER);

        JButton createExamBtn = new JButton("Create Exam");
        JButton viewExamBtn = new JButton("View Exam");
        JButton viewResultsBtn = new JButton("View Results");

        createExamBtn.addActionListener(e -> new CreateExamFrame());
        viewExamBtn.addActionListener(e -> new ViewExamFrame());
        viewResultsBtn.addActionListener(e -> new ViewResultsFrame());

        add(title);
        add(createExamBtn);
        add(viewExamBtn);
        add(viewResultsBtn);

        setVisible(true);
    }
}