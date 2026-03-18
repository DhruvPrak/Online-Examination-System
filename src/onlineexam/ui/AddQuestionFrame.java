package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddQuestionFrame extends JFrame {

    JTextField questionField;
    JTextField optionA;
    JTextField optionB;
    JTextField optionC;
    JTextField optionD;
    JTextField answerField;

    public AddQuestionFrame() {

        setTitle("Add Question");
        setSize(500,400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7,2,10,10));

        questionField = new JTextField();
        optionA = new JTextField();
        optionB = new JTextField();
        optionC = new JTextField();
        optionD = new JTextField();
        answerField = new JTextField();

        JButton addBtn = new JButton("Add Question");

        add(new JLabel("Question:"));
        add(questionField);

        add(new JLabel("Option A:"));
        add(optionA);

        add(new JLabel("Option B:"));
        add(optionB);

        add(new JLabel("Option C:"));
        add(optionC);

        add(new JLabel("Option D:"));
        add(optionD);

        add(new JLabel("Correct Answer:"));
        add(answerField);

        add(new JLabel());
        add(addBtn);

        addBtn.addActionListener(e -> addQuestion());

        setVisible(true);
    }

    private void addQuestion() {

        String sql = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES (?,?,?,?,?,?)";

        try(Connection conn = DBConnection.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, questionField.getText());
            stmt.setString(2, optionA.getText());
            stmt.setString(3, optionB.getText());
            stmt.setString(4, optionC.getText());
            stmt.setString(5, optionD.getText());
            stmt.setString(6, answerField.getText());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,"Question Added Successfully!");

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}