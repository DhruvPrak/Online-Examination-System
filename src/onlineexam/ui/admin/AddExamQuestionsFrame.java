package onlineexam.ui.admin;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddExamQuestionsFrame extends JFrame {

    JTextField questionField, aField, bField, cField, dField, answerField;
    int examId;

    public AddExamQuestionsFrame(int examId) {

        this.examId = examId;

        setTitle("Add Questions to Exam " + examId);
        setSize(500,400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7,2,10,10));

        questionField = new JTextField();
        aField = new JTextField();
        bField = new JTextField();
        cField = new JTextField();
        dField = new JTextField();
        answerField = new JTextField();

        JButton addBtn = new JButton("Add Question");

        add(new JLabel("Question:"));
        add(questionField);

        add(new JLabel("Option A:"));
        add(aField);

        add(new JLabel("Option B:"));
        add(bField);

        add(new JLabel("Option C:"));
        add(cField);

        add(new JLabel("Option D:"));
        add(dField);

        add(new JLabel("Correct Answer:"));
        add(answerField);

        add(new JLabel());
        add(addBtn);

        addBtn.addActionListener(e -> addQuestion());

        setVisible(true);
    }

    private void addQuestion() {

        String sql = "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES (?,?,?,?,?,?,?)";

        try(Connection conn = DBConnection.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, examId);
            stmt.setString(2, questionField.getText());
            stmt.setString(3, aField.getText());
            stmt.setString(4, bField.getText());
            stmt.setString(5, cField.getText());
            stmt.setString(6, dField.getText());
            stmt.setString(7, answerField.getText());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Question Added!");

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}