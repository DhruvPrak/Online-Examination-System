package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ExamFrame extends JFrame {

    int examId;
    int sId;

    JLabel questionLabel;
    JRadioButton optionA, optionB, optionC, optionD;
    ButtonGroup optionsGroup;

    JButton nextButton;
    JButton submitButton;

    ArrayList<String> correctAnswers = new ArrayList<>();
    ArrayList<String> studentAnswers = new ArrayList<>();

    ResultSet rs;

    public ExamFrame(int sId, int examId){

        this.sId = sId;
        this.examId = examId;

        setTitle("Online Exam");
        setSize(600,400);
        setLocationRelativeTo(null);

        questionLabel = new JLabel("Question");

        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();

        optionsGroup = new ButtonGroup();

        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");

        loadQuestions();

        nextButton.addActionListener(e -> {

            studentAnswers.add(getSelectedAnswer());

            loadNextQuestion();

        });

        submitButton.addActionListener(e -> submitExam());

        setLayout(new GridLayout(7,1));

        add(questionLabel);
        add(optionA);
        add(optionB);
        add(optionC);
        add(optionD);
        add(nextButton);
        add(submitButton);

        setVisible(true);
    }

    private void submitExam(){

    studentAnswers.add(getSelectedAnswer());

    int score = 0;

    for(int i=0;i<correctAnswers.size();i++){

        if(studentAnswers.get(i).equals(correctAnswers.get(i))){
            score++;
        }

    }

    saveResult(score);

    JOptionPane.showMessageDialog(this,
        "Exam Finished!\nYour Score: "+score+"/"+correctAnswers.size());

    dispose();
}

private void saveResult(int score){

    try{

        Connection conn = DBConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO results (student_id, exam_id, score, total_marks, status) VALUES (?,?,?,?,?)");

        ps.setInt(1, sId);
        ps.setInt(2, examId);
        ps.setInt(3, score);
        ps.setInt(4, correctAnswers.size());

        String status = score >= (correctAnswers.size()/2) ? "PASS" : "FAIL";

        ps.setString(5, status);

        ps.executeUpdate();

    }catch(Exception e){
        e.printStackTrace();
    }
}

    private void loadQuestions(){

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM questions WHERE exam_id=? ORDER BY RAND() LIMIT 10"
            );

            ps.setInt(1, examId);

            rs = ps.executeQuery();

            loadNextQuestion();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private String getSelectedAnswer() {

    if(optionA.isSelected()) return optionA.getText();
    if(optionB.isSelected()) return optionB.getText();
    if(optionC.isSelected()) return optionC.getText();
    if(optionD.isSelected()) return optionD.getText();

    return "";
}

    private void loadNextQuestion(){

    try{

        if(rs.next()){

            questionLabel.setText(rs.getString("question_text"));

            optionA.setText(rs.getString("option_a"));
            optionB.setText(rs.getString("option_b"));
            optionC.setText(rs.getString("option_c"));
            optionD.setText(rs.getString("option_d"));

            correctAnswers.add(rs.getString("correct_answer"));

            optionsGroup.clearSelection();

        }else{

            JOptionPane.showMessageDialog(this,"All questions completed!");
        }

    }catch(Exception e){
        e.printStackTrace();
    }
}}