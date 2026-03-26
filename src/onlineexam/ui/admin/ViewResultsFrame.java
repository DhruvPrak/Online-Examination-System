package onlineexam.ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import onlineexam.util.DBConnection;

public class ViewResultsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewResultsFrame() {

        setTitle("All Student Results");
        setSize(900,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadResults();

        setVisible(true);
    }

    private void initUI() {

        String[] columns = {
                "Student",
                "Exam",
                "Score",
                "Total Marks",
                "Percentage",
                "Grade",
                "Status",
                "Submitted At"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadResults() {

        try {

            Connection conn = DBConnection.getConnection();

            String query =
                    "SELECT u.username, e.exam_title, r.score, r.total_marks, r.status, r.submitted_at " +
                    "FROM results r " +
                    "JOIN users u ON r.student_id = u.id " +
                    "JOIN exams e ON r.exam_id = e.id";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String student = rs.getString("username");
                String exam = rs.getString("exam_title");

                int score = rs.getInt("score");
                int totalMarks = rs.getInt("total_marks");

                double percentage = ((double) score / totalMarks) * 100;

                String grade = calculateGrade(percentage);

                String status = rs.getString("status");
                String submittedAt = rs.getString("submitted_at");

                Object[] row = {
                        student,
                        exam,
                        score,
                        totalMarks,
                        String.format("%.2f", percentage) + "%",
                        grade,
                        status,
                        submittedAt
                };

                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String calculateGrade(double percentage) {

        if (percentage >= 90)
            return "A";
        else if (percentage >= 75)
            return "B";
        else if (percentage >= 60)
            return "C";
        else if (percentage >= 40)
            return "D";
        else
            return "F";
    }
}