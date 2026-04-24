package onlineexam.ui.admin;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class AddExamQuestionsFrame extends JFrame {

    JTextField q, a, b, c, d, ans;

    int examId;
    int totalQuestionsRequired;
    int addedQuestions = 0;

    JLabel progressLabel;
    JLabel questionNumberLabel;

    public AddExamQuestionsFrame(int examId, int totalQuestionsRequired) {

        this.examId = examId;
        this.totalQuestionsRequired = totalQuestionsRequired;

        setTitle("Add Exam Questions");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();

        setVisible(true);
    }

    private void initUI() {

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        JLabel titleLabel = new JLabel(
                "ADD QUESTIONS TO EXAM",
                JLabel.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutBtn);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 12, 12));
        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        q = new JTextField();
        a = new JTextField();
        b = new JTextField();
        c = new JTextField();
        d = new JTextField();
        ans = new JTextField();

        questionNumberLabel = new JLabel(
                "Currently Adding: Question No. 1",
                JLabel.CENTER
        );
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));

        progressLabel = new JLabel(
                "Questions Added: 0 / " + totalQuestionsRequired,
                JLabel.CENTER
        );
        progressLabel.setFont(new Font("Arial", Font.BOLD, 15));

        JButton addBtn = new JButton("Add Question");
        addBtn.setFont(new Font("Arial", Font.BOLD, 15));

        inputPanel.add(new JLabel("Question:"));
        inputPanel.add(q);

        inputPanel.add(new JLabel("Option A:"));
        inputPanel.add(a);

        inputPanel.add(new JLabel("Option B:"));
        inputPanel.add(b);

        inputPanel.add(new JLabel("Option C:"));
        inputPanel.add(c);

        inputPanel.add(new JLabel("Option D:"));
        inputPanel.add(d);

        inputPanel.add(new JLabel("Correct Answer (A/B/C/D):"));
        inputPanel.add(ans);

        inputPanel.add(new JLabel());
        inputPanel.add(addBtn);

        formPanel.add(questionNumberLabel, BorderLayout.NORTH);
        formPanel.add(inputPanel, BorderLayout.CENTER);
        formPanel.add(progressLabel, BorderLayout.SOUTH);

        add(formPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addQ());
    }

    private void addQ() {

        try (Connection conn = DBConnection.getConnection()) {

            if (addedQuestions >= totalQuestionsRequired) {
                JOptionPane.showMessageDialog(
                        this,
                        "All required questions are already added!"
                );
                return;
            }

            if (q.getText().trim().isEmpty() ||
                    a.getText().trim().isEmpty() ||
                    b.getText().trim().isEmpty() ||
                    c.getText().trim().isEmpty() ||
                    d.getText().trim().isEmpty() ||
                    ans.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill Question + All Options + Correct Answer properly."
                );
                return;
            }

            String correctAnswer = ans.getText().trim().toUpperCase();

            if (!correctAnswer.equals("A") &&
                    !correctAnswer.equals("B") &&
                    !correctAnswer.equals("C") &&
                    !correctAnswer.equals("D")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Correct Answer must be only A, B, C or D."
                );
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO questions " +
                    "(exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setInt(1, examId);
            ps.setString(2, q.getText().trim());
            ps.setString(3, a.getText().trim());
            ps.setString(4, b.getText().trim());
            ps.setString(5, c.getText().trim());
            ps.setString(6, d.getText().trim());
            ps.setString(7, correctAnswer);

            ps.executeUpdate();

            addedQuestions++;

            progressLabel.setText(
                    "Questions Added: " + addedQuestions +
                    " / " + totalQuestionsRequired
            );

            questionNumberLabel.setText(
                    "Currently Adding: Question No. " + (addedQuestions + 1)
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Question No. " + addedQuestions + " Added Successfully!"
            );

            q.setText("");
            a.setText("");
            b.setText("");
            c.setText("");
            d.setText("");
            ans.setText("");

            if (addedQuestions == totalQuestionsRequired) {
                JOptionPane.showMessageDialog(
                        this,
                        "All questions added successfully!\nExam is now ready for students."
                );
                dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error while adding question:\n" + e.getMessage()
            );
        }
    }
}