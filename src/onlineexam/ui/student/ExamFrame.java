package onlineexam.ui.student;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamFrame extends JFrame {

private int studentId;
private int examId;

private JLabel questionLabel, timerLabel;
private JRadioButton optionA, optionB, optionC, optionD;
private ButtonGroup optionsGroup;

private javax.swing.Timer timer;
private int timeLeft = 600;

private List<Map<String, String>> questions = new ArrayList<>();
private Map<Integer, String> answers = new HashMap<>();

private int currentQuestionIndex = 0;

public ExamFrame(int studentId, int examId) {

    this.studentId = studentId;
    this.examId = examId;

    setTitle("Exam");
    setSize(700,400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initUI();
    loadQuestions();
    displayQuestion();
    startTimer();

    setVisible(true);
}

private void initUI(){

    setLayout(new BorderLayout());

    timerLabel = new JLabel("Time Left: 10:00", JLabel.RIGHT);
    add(timerLabel, BorderLayout.NORTH);

    JPanel center = new JPanel(new GridLayout(5,1));

    questionLabel = new JLabel();

    optionA = new JRadioButton();
    optionB = new JRadioButton();
    optionC = new JRadioButton();
    optionD = new JRadioButton();

    optionsGroup = new ButtonGroup();
    optionsGroup.add(optionA);
    optionsGroup.add(optionB);
    optionsGroup.add(optionC);
    optionsGroup.add(optionD);

    center.add(questionLabel);
    center.add(optionA);
    center.add(optionB);
    center.add(optionC);
    center.add(optionD);

    add(center, BorderLayout.CENTER);

    JPanel bottom = new JPanel();

    JButton next = new JButton("Next");
    JButton prev = new JButton("Previous");
    JButton submit = new JButton("Submit");

    next.addActionListener(e -> nextQuestion());
    prev.addActionListener(e -> previousQuestion());
    submit.addActionListener(e -> submitExam());

    bottom.add(prev);
    bottom.add(next);
    bottom.add(submit);

    add(bottom, BorderLayout.SOUTH);
}

private void loadQuestions(){

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM questions WHERE exam_id=? LIMIT 10"
        );

        ps.setInt(1, examId);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Map<String,String> q = new HashMap<>();

            q.put("question", rs.getString("question_text"));
            q.put("A", rs.getString("option_a"));
            q.put("B", rs.getString("option_b"));
            q.put("C", rs.getString("option_c"));
            q.put("D", rs.getString("option_d"));
            q.put("correct", rs.getString("correct_answer"));

            questions.add(q);
        }

    }catch(Exception e){
        e.printStackTrace();
    }
}

private void displayQuestion(){

    Map<String,String> q = questions.get(currentQuestionIndex);

    questionLabel.setText("Q" + (currentQuestionIndex+1) + ": " + q.get("question"));

    optionA.setText(q.get("A"));
    optionB.setText(q.get("B"));
    optionC.setText(q.get("C"));
    optionD.setText(q.get("D"));

    optionsGroup.clearSelection();

    if(answers.containsKey(currentQuestionIndex)){
        String ans = answers.get(currentQuestionIndex);

        if(ans.equals("A")) optionA.setSelected(true);
        if(ans.equals("B")) optionB.setSelected(true);
        if(ans.equals("C")) optionC.setSelected(true);
        if(ans.equals("D")) optionD.setSelected(true);
    }
}

private void saveAnswer(){

    if(optionA.isSelected()) answers.put(currentQuestionIndex,"A");
    if(optionB.isSelected()) answers.put(currentQuestionIndex,"B");
    if(optionC.isSelected()) answers.put(currentQuestionIndex,"C");
    if(optionD.isSelected()) answers.put(currentQuestionIndex,"D");
}

private void nextQuestion(){
    saveAnswer();
    if(currentQuestionIndex < questions.size()-1){
        currentQuestionIndex++;
        displayQuestion();
    }
}

private void previousQuestion(){
    saveAnswer();
    if(currentQuestionIndex > 0){
        currentQuestionIndex--;
        displayQuestion();
    }
}

private void startTimer(){

    timer = new javax.swing.Timer(1000, (ActionEvent e) -> {

        timeLeft--;

        int min = timeLeft / 60;
        int sec = timeLeft % 60;

        timerLabel.setText("Time Left: " + String.format("%02d:%02d", min, sec));

        if(timeLeft <= 0){
            timer.stop();
            submitExam();
        }
    });

    timer.start();
}

private void submitExam(){

    saveAnswer();
    timer.stop();

    int correct = 0;

    for(int i=0;i<questions.size();i++){
        if(questions.get(i).get("correct").equals(answers.get(i))){
            correct++;
        }
    }

    JOptionPane.showMessageDialog(this,
        "Exam Submitted!\nScore: " + correct + "/" + questions.size());

    dispose();
    new StudentFrame(studentId);
}

}
