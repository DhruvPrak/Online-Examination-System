package onlineexam.ui.admin;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class CreateExamFrame extends JFrame {

    JTextField titleField, questionCountField, durationField;

    public CreateExamFrame() {

        setTitle("Create Exam");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        topPanel.add(logoutBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        titleField = new JTextField();
        questionCountField = new JTextField();
        durationField = new JTextField();

        JButton createBtn = new JButton("Create Exam");

        panel.add(new JLabel("Exam Title:"));
        panel.add(titleField);

        panel.add(new JLabel("Questions Student Will Attempt:"));
        panel.add(questionCountField);

        panel.add(new JLabel("Duration (minutes):"));
        panel.add(durationField);

        panel.add(new JLabel());
        panel.add(createBtn);

        add(panel, BorderLayout.CENTER);

        createBtn.addActionListener(e -> createExam());

        setVisible(true);
    }

    private void createExam() {

        try (Connection conn = DBConnection.getConnection()) {

            String title = titleField.getText().trim();
            int questionsToDisplay = Integer.parseInt(questionCountField.getText().trim());
            int totalQuestions = questionsToDisplay * 2; // Randomization logic
            int duration = Integer.parseInt(durationField.getText().trim()) * 60;

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter exam title.");
                return;
            }

            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO exams (exam_title, total_questions, questions_to_display, duration, status) VALUES (?, ?, ?, ?, 'NOT_STARTED')",
                PreparedStatement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, title);
            stmt.setInt(2, totalQuestions);          // Example: 20
            stmt.setInt(3, questionsToDisplay);      // Example: 10
            stmt.setInt(4, duration);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int examId = 0;

            if (rs.next()) {
                examId = rs.getInt(1);
            }

            JOptionPane.showMessageDialog(this,
                "Exam Created Successfully!\n" +
                "Students will attempt: " + questionsToDisplay + " questions\n" +
                "Admin must add: " + totalQuestions + " questions"
            );

            new AddExamQuestionsFrame(examId, totalQuestions);
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
