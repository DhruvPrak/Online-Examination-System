package onlineexam.ui.examiner;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class AddQuestionFrame extends JFrame {

int examinerId;

JTextField questionField, optionA, optionB, optionC, optionD, correctAnswer;

public AddQuestionFrame(int examinerId){

    this.examinerId = examinerId;

    setTitle("Add Question");
    setSize(400,400);
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

    JPanel panel = new JPanel(new GridLayout(7,2,10,10));

    questionField = new JTextField();
    optionA = new JTextField();
    optionB = new JTextField();
    optionC = new JTextField();
    optionD = new JTextField();
    correctAnswer = new JTextField();

    JButton saveBtn = new JButton("Save Question");

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

    panel.add(new JLabel("Correct Answer"));
    panel.add(correctAnswer);

    panel.add(new JLabel());
    panel.add(saveBtn);

    add(panel, BorderLayout.CENTER);

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

        JOptionPane.showMessageDialog(this,"Question Added!");

        questionField.setText("");
        optionA.setText("");
        optionB.setText("");
        optionC.setText("");
        optionD.setText("");
        correctAnswer.setText("");

    }catch(Exception e){
        e.printStackTrace();
    }
}

}