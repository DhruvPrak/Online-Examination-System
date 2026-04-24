package onlineexam.ui.admin;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class AdminFrame extends JFrame {

    private JLabel totalStudentsLabel;
    private JLabel totalExamsLabel;
    private JLabel totalResultsLabel;
    private JLabel passedStudentsLabel;
    private JLabel failedStudentsLabel;

    public AdminFrame() {

        setTitle("Admin Dashboard");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        loadDashboardStats();

        setVisible(true);
    }

    private void initUI() {

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel(
                "ADMIN DASHBOARD",
                JLabel.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutBtn);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        statsPanel.setBorder(
                BorderFactory.createTitledBorder("System Statistics")
        );

        totalStudentsLabel = new JLabel("Total Students: Loading...");
        totalExamsLabel = new JLabel("Total Exams: Loading...");
        totalResultsLabel = new JLabel("Total Results: Loading...");
        passedStudentsLabel = new JLabel("Passed Students: Loading...");
        failedStudentsLabel = new JLabel("Failed Students: Loading...");

        totalStudentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalExamsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalResultsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passedStudentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        failedStudentsLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        statsPanel.add(totalStudentsLabel);
        statsPanel.add(totalExamsLabel);
        statsPanel.add(totalResultsLabel);
        statsPanel.add(passedStudentsLabel);
        statsPanel.add(failedStudentsLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBorder(
                BorderFactory.createTitledBorder("Admin Actions")
        );

        JButton createExamBtn = new JButton("Create Exam");
        JButton viewExamBtn = new JButton("View Exams");
        JButton viewResultsBtn = new JButton("View Results");

        createExamBtn.setFont(new Font("Arial", Font.BOLD, 15));
        viewExamBtn.setFont(new Font("Arial", Font.BOLD, 15));
        viewResultsBtn.setFont(new Font("Arial", Font.BOLD, 15));

        createExamBtn.addActionListener(e -> new CreateExamFrame());
        viewExamBtn.addActionListener(e -> new ViewExamFrame());
        viewResultsBtn.addActionListener(e -> new ViewResultsFrame());

        buttonPanel.add(createExamBtn);
        buttonPanel.add(viewExamBtn);
        buttonPanel.add(viewResultsBtn);

        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadDashboardStats() {

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE role='STUDENT'"
            );

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                totalStudentsLabel.setText(
                        "Total Students: " + rs1.getInt(1)
                );
            }

            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM exams"
            );

            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                totalExamsLabel.setText(
                        "Total Exams: " + rs2.getInt(1)
                );
            }

            PreparedStatement ps3 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM results"
            );

            ResultSet rs3 = ps3.executeQuery();

            if (rs3.next()) {
                totalResultsLabel.setText(
                        "Total Results: " + rs3.getInt(1)
                );
            }

            PreparedStatement ps4 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM results WHERE result_status='PASS'"
            );

            ResultSet rs4 = ps4.executeQuery();

            if (rs4.next()) {
                passedStudentsLabel.setText(
                        "Passed Students: " + rs4.getInt(1)
                );
            }

            PreparedStatement ps5 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM results WHERE result_status='FAIL'"
            );

            ResultSet rs5 = ps5.executeQuery();

            if (rs5.next()) {
                failedStudentsLabel.setText(
                        "Failed Students: " + rs5.getInt(1)
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading dashboard statistics."
            );
            e.printStackTrace();
        }
    }
}