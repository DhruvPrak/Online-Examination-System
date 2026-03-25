package onlineexam.ui;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import onlineexam.util.DBConnection;

public class ExamFrame extends JFrame {

    int studentId;
    int examId;

    JLabel timerLabel;
    JLabel questionLabel;

    JRadioButton optionA, optionB, optionC, optionD;
    ButtonGroup optionsGroup;

    JButton nextButton;
    JButton submitButton;

    Timer timer;
    int timeLeft = 600; // 10 minutes

    ArrayList<String> correctAnswers = new ArrayList<>();
    ArrayList<String> studentAnswers = new ArrayList<>();

    ResultSet rs;

    public ExamFrame(int studentId, int examId){

        this.studentId = studentId;
        this.examId = examId;

        setTitle("Online Exam");
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timerLabel = new JLabel("Time Remaining: 10:00");
        timerLabel.setFont(new Font("Arial",Font.BOLD,16));
        timerLabel.setForeground(Color.RED);

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

        setLayout(new GridLayout(8,1));

        add(timerLabel);
        add(questionLabel);
        add(optionA);
        add(optionB);
        add(optionC);
        add(optionD);
        add(nextButton);
        add(submitButton);

        loadQuestions();
        startTimer();

        nextButton.addActionListener(e -> {

            studentAnswers.add(getSelectedAnswer());
            loadNextQuestion();

        });

        submitButton.addActionListener(e -> submitExam());

        setVisible(true);
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

                JOptionPane.showMessageDialog(this,"No more questions!");

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getSelectedAnswer(){

        if(optionA.isSelected()) return optionA.getText();
        if(optionB.isSelected()) return optionB.getText();
        if(optionC.isSelected()) return optionC.getText();
        if(optionD.isSelected()) return optionD.getText();

        return "";
    }

    private void submitExam(){
        System.out.println("Submitting Exam...");
        timer.stop();

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

        System.out.println("Saving Result to Database...");

        Connection conn = DBConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO results (student_id, exam_id, score, total_marks, status) VALUES (?,?,?,?,?)"
        );

        ps.setInt(1, studentId);
        ps.setInt(2, examId);
        ps.setInt(3, score);
        ps.setInt(4, correctAnswers.size());

        String status = score >= (correctAnswers.size()/2) ? "PASS" : "FAIL";

        ps.setString(5, status);

        ps.executeUpdate();

        System.out.println("Result inserted successfully!");

    }catch(Exception e){
        e.printStackTrace();
    }
}
    private void startTimer(){

        timer = new Timer(1000, e -> {

            timeLeft--;

            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;

            timerLabel.setText("Time Remaining: "+minutes+":"+String.format("%02d",seconds));

            if(timeLeft <= 0){

                timer.stop();

                JOptionPane.showMessageDialog(this,"Time is up!");

                submitExam();

            }

        });

        timer.start();
    }
}