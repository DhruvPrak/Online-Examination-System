package onlineexam.ui.student;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class StudentResultsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public StudentResultsFrame(int studentId) {

        setTitle("My Results");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();
        loadResults(studentId);

        setVisible(true);
    }

    private void initUI() {

        /*
         * TOP PANEL
         */
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        JLabel titleLabel = new JLabel(
                "STUDENT RESULT DASHBOARD",
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

        /*
         * TABLE PANEL
         */
        String[] columns = {
                "Exam Name",
                "Score",
                "Total Marks",
                "Percentage",
                "Grade",
                "Result Status",
                "Submitted Time"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("My Exam Results")
        );

        add(scrollPane, BorderLayout.CENTER);

        /*
         * FOOTER PANEL
         */
        JPanel bottomPanel = new JPanel();

        JLabel footerLabel = new JLabel(
                "View your complete exam performance here"
        );

        footerLabel.setFont(new Font("Arial", Font.ITALIC, 13));

        bottomPanel.add(footerLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadResults(int studentId) {

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT e.exam_title, " +
                    "r.score, " +
                    "r.total_marks, " +
                    "r.percentage, " +
                    "r.grade, " +
                    "r.result_status, " +
                    "r.submitted_at " +
                    "FROM results r " +
                    "JOIN exams e ON r.exam_id = e.id " +
                    "WHERE r.student_id=?"
            );

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            boolean hasData = false;

            while (rs.next()) {

                hasData = true;

                model.addRow(new Object[]{
                        rs.getString("exam_title"),
                        rs.getInt("score"),
                        rs.getInt("total_marks"),
                        String.format("%.2f%%",
                                rs.getDouble("percentage")),
                        rs.getString("grade"),
                        rs.getString("result_status"),
                        rs.getString("submitted_at")
                });
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(
                        this,
                        "No results found yet."
                );
            }

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading student results."
            );
        }
    }
}