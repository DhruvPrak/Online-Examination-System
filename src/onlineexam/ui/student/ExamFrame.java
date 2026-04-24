package onlineexam.ui.student;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import onlineexam.util.DBConnection;

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
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        loadQuestions();

        if (questions.size() > 0) {
            displayQuestion();
            startTimer();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No questions available for this exam.");
            dispose();
        }

        setVisible(true);
    }

    private void initUI() {

        timerLabel = new JLabel("Time Left: 10:00", JLabel.RIGHT);
        add(timerLabel, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(5, 1, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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

        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton submit = new JButton("Submit");

        prev.addActionListener(e -> previousQuestion());
        next.addActionListener(e -> nextQuestion());
        submit.addActionListener(e -> submitExam());

        bottom.add(prev);
        bottom.add(next);
        bottom.add(submit);

        add(bottom, BorderLayout.SOUTH);
    }

    /*
     * RANDOM QUESTION LOADING LOGIC
     *
     * Example:
     * Admin creates exam:
     * total_questions = 20
     * questions_to_display = 10
     *
     * Student sees random 10 questions from those 20
     */
    private void loadQuestions() {

        try (Connection conn = DBConnection.getConnection()) {

            // First get how many questions should be shown to student
            PreparedStatement examStmt = conn.prepareStatement(
                    "SELECT questions_to_display FROM exams WHERE id=?");

            examStmt.setInt(1, examId);
            ResultSet examRs = examStmt.executeQuery();

            int questionsToDisplay = 10; // fallback default

            if (examRs.next()) {
                questionsToDisplay = examRs.getInt("questions_to_display");
            }

            // Randomized question selection
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM questions " +
                    "WHERE exam_id=? " +
                    "ORDER BY RAND() " +
                    "LIMIT ?");

            ps.setInt(1, examId);
            ps.setInt(2, questionsToDisplay);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> q = new HashMap<>();

                q.put("question", rs.getString("question_text"));
                q.put("A", rs.getString("option_a"));
                q.put("B", rs.getString("option_b"));
                q.put("C", rs.getString("option_c"));
                q.put("D", rs.getString("option_d"));
                q.put("correct", rs.getString("correct_answer"));

                questions.add(q);
            }

            System.out.println("Questions loaded randomly: " + questions.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion() {

        Map<String, String> q = questions.get(currentQuestionIndex);

        questionLabel.setText(
                "Q" + (currentQuestionIndex + 1) + ": " + q.get("question"));

        optionA.setText(q.get("A"));
        optionB.setText(q.get("B"));
        optionC.setText(q.get("C"));
        optionD.setText(q.get("D"));

        optionsGroup.clearSelection();

        if (answers.containsKey(currentQuestionIndex)) {
            String ans = answers.get(currentQuestionIndex);

            if (ans.equals("A")) optionA.setSelected(true);
            if (ans.equals("B")) optionB.setSelected(true);
            if (ans.equals("C")) optionC.setSelected(true);
            if (ans.equals("D")) optionD.setSelected(true);
        }
    }

    private void saveAnswer() {

        if (optionA.isSelected()) answers.put(currentQuestionIndex, "A");
        if (optionB.isSelected()) answers.put(currentQuestionIndex, "B");
        if (optionC.isSelected()) answers.put(currentQuestionIndex, "C");
        if (optionD.isSelected()) answers.put(currentQuestionIndex, "D");
    }

    private void nextQuestion() {
        saveAnswer();

        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void previousQuestion() {
        saveAnswer();

        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion();
        }
    }

    private void startTimer() {

        timer = new javax.swing.Timer(1000, (ActionEvent e) -> {

            timeLeft--;

            int min = timeLeft / 60;
            int sec = timeLeft % 60;

            timerLabel.setText(
                    "Time Left: " + String.format("%02d:%02d", min, sec));

            if (timeLeft <= 0) {
                timer.stop();
                submitExam();
            }
        });

        timer.start();
    }

    private void submitExam() {

        System.out.println("SUBMIT CLICKED");

        saveAnswer();

        if (timer != null) {
            timer.stop();
        }

        int correct = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).get("correct").equals(answers.get(i))) {
                correct++;
            }
        }

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO results (student_id, exam_id, score, total_marks) VALUES (?, ?, ?, ?)");

            ps.setInt(1, studentId);
            ps.setInt(2, examId);
            ps.setInt(3, correct);
            ps.setInt(4, questions.size());

            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE users SET exam_status='COMPLETED' WHERE id=?");

            ps2.setInt(1, studentId);
            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this,
                "Exam Submitted!\nScore: " + correct + "/" + questions.size());

        System.out.println("Exam submitted with score: " + correct);

        dispose();
        new StudentFrame(studentId);
    }
}
