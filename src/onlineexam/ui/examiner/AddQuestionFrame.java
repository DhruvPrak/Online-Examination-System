package onlineexam.ui.examiner;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class AddQuestionFrame extends JFrame {

    private int examinerId;

    private JComboBox<String> examComboBox;
    private JTextField questionField;
    private JTextField optionA;
    private JTextField optionB;
    private JTextField optionC;
    private JTextField optionD;
    private JTextField correctAnswer;

    public AddQuestionFrame(int examinerId) {
        this.examinerId = examinerId;

        setTitle("Add Question");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel with logout button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        topPanel.add(logoutBtn);
        add(topPanel, BorderLayout.NORTH);

        // Main form panel
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        examComboBox = new JComboBox<>();
        loadExams();

        questionField = new JTextField();
        optionA = new JTextField();
        optionB = new JTextField();
        optionC = new JTextField();
        optionD = new JTextField();
        correctAnswer = new JTextField();

        JButton saveBtn = new JButton("Save Question");

        panel.add(new JLabel("Select Exam"));
        panel.add(examComboBox);

        panel.add(new JLabel("Question"));
        panel.add(questionField);

        panel.add(new JLabel("Option A"));
        panel.add(optionA);

        panel.add(new JLabel("Option B"));
        panel.add(optionB);

        panel.add(new JLabel("Option C"));
        panel.add(optionC);

        panel.add(new JLabel("Option D"));
        panel.add(optionD);

        panel.add(new JLabel("Correct Answer (A/B/C/D)"));
        panel.add(correctAnswer);

        panel.add(new JLabel());
        panel.add(saveBtn);

        add(panel, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> saveQuestion());

        setVisible(true);
    }

    // Load all available exams into dropdown
    private void loadExams() {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, exam_title FROM exams WHERE status != 'ENDED'"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int examId = rs.getInt("id");
                String examTitle = rs.getString("exam_title");

                // Format: 1 - Java Basics Test
                examComboBox.addItem(examId + " - " + examTitle);
            }

            if (examComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "No exams found. Please ask Admin to create an exam first.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to load exams.");
        }
    }

    private void saveQuestion() {
        try {
            if (examComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select an exam.");
                return;
            }

            if (questionField.getText().trim().isEmpty() ||
                optionA.getText().trim().isEmpty() ||
                optionB.getText().trim().isEmpty() ||
                optionC.getText().trim().isEmpty() ||
                optionD.getText().trim().isEmpty() ||
                correctAnswer.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                    "Please fill all fields.");
                return;
            }

            // Extract exam_id from dropdown text
            String selectedExam = examComboBox.getSelectedItem().toString();
            int examId = Integer.parseInt(selectedExam.split(" - ")[0]);

            try (Connection conn = DBConnection.getConnection()) {

                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO questions " +
                    "(exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer, created_by) " +
                    "VALUES (?,?,?,?,?,?,?,?)"
                );

                ps.setInt(1, examId);
                ps.setString(2, questionField.getText().trim());
                ps.setString(3, optionA.getText().trim());
                ps.setString(4, optionB.getText().trim());
                ps.setString(5, optionC.getText().trim());
                ps.setString(6, optionD.getText().trim());
                ps.setString(7, correctAnswer.getText().trim().toUpperCase());
                ps.setInt(8, examinerId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Question added successfully to selected exam!");

                clearFields();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error while saving question.");
        }
    }

    private void clearFields() {
        questionField.setText("");
        optionA.setText("");
        optionB.setText("");
        optionC.setText("");
        optionD.setText("");
        correctAnswer.setText("");
    }
}
