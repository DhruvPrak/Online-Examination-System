package onlineexam.ui.student;

import java.awt.*;
import javax.swing.*;
import onlineexam.ui.LoginFrame;

public class StudentFrame extends JFrame {

    private int sId;

    public StudentFrame(int sID) {

        this.sId = sID;

        setTitle("Student Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();

        setVisible(true);
    }

    private void initUI() {

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 10, 20)
        );

        JLabel titleLabel = new JLabel(
                "STUDENT DASHBOARD",
                JLabel.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutBtn);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        actionPanel.setBorder(
                BorderFactory.createTitledBorder("Student Actions")
        );
        actionPanel.setPreferredSize(new Dimension(350, 200));

        JButton startExamBtn = new JButton("Start Exam");
        JButton resultBtn = new JButton("View Results");
        JButton exitBtn = new JButton("Exit");

        startExamBtn.setFont(new Font("Arial", Font.BOLD, 16));
        resultBtn.setFont(new Font("Arial", Font.BOLD, 16));
        exitBtn.setFont(new Font("Arial", Font.BOLD, 16));

        startExamBtn.addActionListener(e -> {
            new StartExamFrame(sId);
            dispose();
        });

        resultBtn.addActionListener(e -> {
            new StudentResultsFrame(sId);
            dispose();
        });

        exitBtn.addActionListener(e -> System.exit(0));

        actionPanel.add(startExamBtn);
        actionPanel.add(resultBtn);
        actionPanel.add(exitBtn);

        centerPanel.add(actionPanel);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JLabel footerLabel = new JLabel(
                "Welcome to the Online Examination System"
        );
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 13));

        bottomPanel.add(footerLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}