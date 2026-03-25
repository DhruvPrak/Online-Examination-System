package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateExamFrame extends JFrame {

    JTextField examIdField;
    JTextField titleField;
    JTextField questionCountField;

    public CreateExamFrame() {

        setTitle("Create Exam");
        setSize(400,250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4,2,10,10));

        examIdField = new JTextField();
        titleField = new JTextField();
        questionCountField = new JTextField();

        JButton createBtn = new JButton("Create Exam");

        add(new JLabel("Exam ID:"));
        add(examIdField);

        add(new JLabel("Exam Title:"));
        add(titleField);

        add(new JLabel("Questions in Exam:"));
        add(questionCountField);

        add(new JLabel());
        add(createBtn);

        createBtn.addActionListener(e -> createExam());

        setVisible(true);
    }

    private void createExam() {

    String sql = "INSERT INTO exams (id, exam_title, total_questions, questions_to_display) VALUES (?,?,?,?)";

    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement stmt = conn.prepareStatement(sql);

        int examId = Integer.parseInt(examIdField.getText());
        int questionCount = Integer.parseInt(questionCountField.getText());

        stmt.setInt(1, examId);
        stmt.setString(2, titleField.getText());
        stmt.setInt(3, questionCount);
        stmt.setInt(4, questionCount);

        stmt.executeUpdate();

        JOptionPane.showMessageDialog(this,"Exam Created!");

        new AddExamQuestionsFrame(examId);

        dispose();

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
}