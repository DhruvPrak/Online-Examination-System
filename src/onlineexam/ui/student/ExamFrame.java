package onlineexam.ui.student;

import onlineexam.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ExamFrame extends JFrame {

    private int studentId;
    private int examId;

    private JLabel questionLabel;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;

    private JButton nextButton, prevButton, submitButton;

    private JLabel timerLabel;
    private Timer timer;
    private int timeLeft = 600; // 10 minutes

    private java.util.List<Map<String, String>> questions = new ArrayList<>();
    private Map<Integer, String> answers = new HashMap<>();

    private int currentQuestionIndex = 0;

    public ExamFrame(int studentId, int examId) {

        this.studentId = studentId;
        this.examId = examId;

        setTitle("Exam");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadQuestions();
        displayQuestion();
        startTimer();

        setVisible(true);
    }

    private void initUI() {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Time Left: 10:00");
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(timerLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5,1));

        questionLabel = new JLabel();
        centerPanel.add(questionLabel);

        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        centerPanel.add(optionA);
        centerPanel.add(optionB);
        centerPanel.add(optionC);
        centerPanel.add(optionD);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit Exam");

        prevButton.addActionListener(e -> previousQuestion());
        nextButton.addActionListener(e -> nextQuestion());
        submitButton.addActionListener(e -> submitExam());

        bottomPanel.add(prevButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadQuestions() {

        try {

            Connection conn = DBConnection.getConnection();

            String query =
                    "SELECT question_text, option_a, option_b, option_c, option_d, correct_answer " +
                    "FROM questions WHERE exam_id=? ORDER BY RAND() LIMIT 10";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, examId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Map<String, String> q = new HashMap<>();

                q.put("question", rs.getString("question_text"));
                q.put("A", rs.getString("option_a"));
                q.put("B", rs.getString("option_b"));
                q.put("C", rs.getString("option_c"));
                q.put("D", rs.getString("option_d"));
                q.put("correct", rs.getString("correct_answer"));

                questions.add(q);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion() {

        if(questions.isEmpty()) return;

        Map<String,String> q = questions.get(currentQuestionIndex);

        questionLabel.setText(
                "Q" + (currentQuestionIndex+1) + ": " + q.get("question")
        );

        optionA.setText(q.get("A"));
        optionB.setText(q.get("B"));
        optionC.setText(q.get("C"));
        optionD.setText(q.get("D"));

        optionsGroup.clearSelection();

        if(answers.containsKey(currentQuestionIndex)) {

            String ans = answers.get(currentQuestionIndex);

            if(ans.equals("A")) optionA.setSelected(true);
            if(ans.equals("B")) optionB.setSelected(true);
            if(ans.equals("C")) optionC.setSelected(true);
            if(ans.equals("D")) optionD.setSelected(true);
        }
    }

    private void saveAnswer() {

        String selected = null;

        if(optionA.isSelected()) selected = "A";
        if(optionB.isSelected()) selected = "B";
        if(optionC.isSelected()) selected = "C";
        if(optionD.isSelected()) selected = "D";

        if(selected != null)
            answers.put(currentQuestionIndex, selected);
    }

    private void nextQuestion() {

        saveAnswer();

        if(currentQuestionIndex < questions.size()-1) {
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void previousQuestion() {

        saveAnswer();

        if(currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion();
        }
    }

    private void startTimer() {

        timer = new Timer(1000, (ActionEvent e) -> {

            timeLeft--;

            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;

            timerLabel.setText(
                    String.format("Time Left: %02d:%02d", minutes, seconds)
            );

            if(timeLeft <= 0) {

                timer.stop();
                submitExam();
            }
        });

        timer.start();
    }

    private void submitExam() {

        saveAnswer();

        timer.stop();

        int correct = 0;

        for(int i=0;i<questions.size();i++) {

            String correctAns = questions.get(i).get("correct");
            String studentAns = answers.get(i);

            if(correctAns != null && correctAns.equals(studentAns))
                correct++;
        }

        int total = questions.size();

        double percentage = ((double) correct / total) * 100;

        String grade = calculateGrade(percentage);

        saveResult(correct,total);

        JOptionPane.showMessageDialog(this,
                "Exam Submitted!\n\n" +
                "Score: " + correct + "/" + total +
                "\nPercentage: " + String.format("%.2f", percentage) + "%" +
                "\nGrade: " + grade);

        dispose();

        new StudentFrame(studentId);
    }

    private String calculateGrade(double percentage) {

        if(percentage >= 90) return "A";
        else if(percentage >= 75) return "B";
        else if(percentage >= 60) return "C";
        else if(percentage >= 40) return "D";
        else return "F";
    }

    private void saveResult(int score, int total) {

        try {

            Connection conn = DBConnection.getConnection();

            String query =
                    "INSERT INTO results (student_id, exam_id, score, total_marks, status) " +
                    "VALUES (?, ?, ?, ?, 'SUBMITTED')";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, studentId);
            ps.setInt(2, examId);
            ps.setInt(3, score);
            ps.setInt(4, total);

            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}