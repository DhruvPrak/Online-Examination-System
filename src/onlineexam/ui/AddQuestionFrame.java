package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddQuestionFrame extends JFrame {

    int examinerId;

    JTextField questionField, optionA, optionB, optionC, optionD, correctAnswer;

    JButton saveBtn;

    public AddQuestionFrame(int examinerId){

        this.examinerId = examinerId;

        setTitle("Add Question");
        setSize(400,400);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(7,2));

        questionField = new JTextField();
        optionA = new JTextField();
        optionB = new JTextField();
        optionC = new JTextField();
        optionD = new JTextField();
        correctAnswer = new JTextField();

        saveBtn = new JButton("Save Question");

        add(new JLabel("Question"));
        add(questionField);

        add(new JLabel("Option A"));
        add(optionA);

        add(new JLabel("Option B"));
        add(optionB);

        add(new JLabel("Option C"));
        add(optionC);

        add(new JLabel("Option D"));
        add(optionD);

        add(new JLabel("Correct Answer"));
        add(correctAnswer);

        add(saveBtn);

        saveBtn.addActionListener(e -> saveQuestion());

        setVisible(true);
    }

    private void saveQuestion(){

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer, created_by) VALUES (?,?,?,?,?,?,?)"
            );

            ps.setString(1, questionField.getText());
            ps.setString(2, optionA.getText());
            ps.setString(3, optionB.getText());
            ps.setString(4, optionC.getText());
            ps.setString(5, optionD.getText());
            ps.setString(6, correctAnswer.getText());
            ps.setInt(7, examinerId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Question Added Successfully!");

        }catch(Exception e){

            e.printStackTrace();

        }

    }
}