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
    private int timeLeft;

    private List<Map<String, String>> questions = new ArrayList<>();
    private Map<Integer, String> answers = new HashMap<>();

    private int currentQuestionIndex = 0;
    private int warningCount = 0;

    public ExamFrame(int studentId, int examId) {

        this.studentId = studentId;
        this.examId = examId;

        setTitle("Exam");
        setSize(750, 450);
        setLocationRelativeTo(null);
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowLostFocus(java.awt.event.WindowEvent e) {

        warningCount++;

        if (warningCount >= 3) {
            JOptionPane.showMessageDialog(
                ExamFrame.this,
                "You switched away from exam window 3 times.\nExam will be auto-submitted."
            );

            submitExam();
            return;
        }

        JOptionPane.showMessageDialog(
            ExamFrame.this,
            "Warning!\nYou switched away from exam window.\nWarning: "
                    + warningCount + "/3"
        );
    }
});

        setLayout(new BorderLayout());

        initUI();
        loadExamDuration();
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

    setLayout(new BorderLayout(10, 10));

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
    );

    JLabel examTitle = new JLabel(
            "ONLINE EXAMINATION",
            JLabel.LEFT
    );
    examTitle.setFont(new Font("Arial", Font.BOLD, 18));

    timerLabel = new JLabel("Time Left: 10:00", JLabel.RIGHT);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

    topPanel.add(examTitle, BorderLayout.WEST);
    topPanel.add(timerLabel, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);

    JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
    questionPanel.setBorder(
            BorderFactory.createTitledBorder("Question Panel")
    );

    questionLabel = new JLabel();
    questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
    questionLabel.setBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
    );

    JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 12, 12));
    optionsPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
    );

    optionA = new JRadioButton();
    optionB = new JRadioButton();
    optionC = new JRadioButton();
    optionD = new JRadioButton();

    optionA.setFont(new Font("Arial", Font.PLAIN, 15));
    optionB.setFont(new Font("Arial", Font.PLAIN, 15));
    optionC.setFont(new Font("Arial", Font.PLAIN, 15));
    optionD.setFont(new Font("Arial", Font.PLAIN, 15));

    optionsGroup = new ButtonGroup();
    optionsGroup.add(optionA);
    optionsGroup.add(optionB);
    optionsGroup.add(optionC);
    optionsGroup.add(optionD);

    optionsPanel.add(optionA);
    optionsPanel.add(optionB);
    optionsPanel.add(optionC);
    optionsPanel.add(optionD);

    questionPanel.add(questionLabel, BorderLayout.NORTH);
    questionPanel.add(optionsPanel, BorderLayout.CENTER);

    add(questionPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 10)
    );

    JButton prevBtn = new JButton("⬅ Previous");
    JButton nextBtn = new JButton("Next ➡");
    JButton submitBtn = new JButton("Submit Exam");

    prevBtn.setFont(new Font("Arial", Font.BOLD, 14));
    nextBtn.setFont(new Font("Arial", Font.BOLD, 14));
    submitBtn.setFont(new Font("Arial", Font.BOLD, 14));

    prevBtn.addActionListener(e -> previousQuestion());
    nextBtn.addActionListener(e -> nextQuestion());
    submitBtn.addActionListener(e -> submitExam());

    bottomPanel.add(prevBtn);
    bottomPanel.add(nextBtn);
    bottomPanel.add(submitBtn);

    add(bottomPanel, BorderLayout.SOUTH);
}
    private void loadQuestions() {

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement examStmt = conn.prepareStatement(
                    "SELECT questions_to_display FROM exams WHERE id=?");

            examStmt.setInt(1, examId);
            ResultSet examRs = examStmt.executeQuery();

            int questionsToDisplay = 10;

            if (examRs.next()) {
                questionsToDisplay = examRs.getInt("questions_to_display");
            }

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
    private void loadExamDuration() {

    try (Connection conn = DBConnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT duration FROM exams WHERE id=?"
        );

        ps.setInt(1, examId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            timeLeft = rs.getInt("duration");
        } else {
            timeLeft = 600;
        }

        int min = timeLeft / 60;
        int sec = timeLeft % 60;

        timerLabel.setText(
            "Time Left: " + String.format("%02d:%02d", min, sec)
        );

    } catch (Exception e) {
        e.printStackTrace();
        timeLeft = 600;
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

        int totalMarks = questions.size();

        double percentage = ((double) correct / totalMarks) * 100;

        String grade;
        String resultStatus;

        if (percentage >= 80) {
            grade = "A";
            resultStatus = "PASS";
        } else if (percentage >= 60) {
            grade = "B";
            resultStatus = "PASS";
        } else if (percentage >= 40) {
            grade = "C";
            resultStatus = "PASS";
        } else {
            grade = "F";
            resultStatus = "FAIL";
        }

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO results " +
                            "(student_id, exam_id, score, total_marks, percentage, grade, result_status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setInt(1, studentId);
            ps.setInt(2, examId);
            ps.setInt(3, correct);
            ps.setInt(4, totalMarks);
            ps.setDouble(5, percentage);
            ps.setString(6, grade);
            ps.setString(7, resultStatus);

            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE users SET exam_status='COMPLETED' WHERE id=?"
            );

            ps2.setInt(1, studentId);
            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(
                this,
                "Exam Submitted!\n\n" +
                        "Score: " + correct + "/" + totalMarks + "\n" +
                        "Percentage: " + String.format("%.2f", percentage) + "%\n" +
                        "Grade: " + grade + "\n" +
                        "Result: " + resultStatus
        );

        dispose();
        new StudentFrame(studentId);

        System.out.println("Exam submitted with score: " + correct);
    }
}