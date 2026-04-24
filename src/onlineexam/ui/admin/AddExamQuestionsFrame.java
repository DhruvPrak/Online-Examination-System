package onlineexam.ui.admin;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class AddExamQuestionsFrame extends JFrame {

    JTextField q, a, b, c, d, ans;
    int examId;
    int totalQuestionsRequired;
    int addedQuestions = 0;

    JLabel progressLabel;

    public AddExamQuestionsFrame(int examId, int totalQuestionsRequired) {

        this.examId = examId;
        this.totalQuestionsRequired = totalQuestionsRequired;

        setTitle("Add Questions");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logout = new JButton("Logout");

        logout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        top.add(logout);
        add(top, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        q = new JTextField();
        a = new JTextField();
        b = new JTextField();
        c = new JTextField();
        d = new JTextField();
        ans = new JTextField();

        progressLabel = new JLabel(
            "Questions Added: 0 / " + totalQuestionsRequired,
            JLabel.CENTER
        );

        JButton addBtn = new JButton("Add Question");

        panel.add(new JLabel("Question"));
        panel.add(q);

        panel.add(new JLabel("Option A"));
        panel.add(a);

        panel.add(new JLabel("Option B"));
        panel.add(b);

        panel.add(new JLabel("Option C"));
        panel.add(c);

        panel.add(new JLabel("Option D"));
        panel.add(d);

        panel.add(new JLabel("Correct Answer (A/B/C/D)"));
        panel.add(ans);

        panel.add(new JLabel());
        panel.add(addBtn);

        add(progressLabel, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addQ());

        setVisible(true);
    }

    private void addQ() {

        try (Connection conn = DBConnection.getConnection()) {

            if (addedQuestions >= totalQuestionsRequired) {
                JOptionPane.showMessageDialog(this,
                    "All required questions already added!");
                return;
            }

            if (q.getText().trim().isEmpty() ||
                a.getText().trim().isEmpty() ||
                b.getText().trim().isEmpty() ||
                c.getText().trim().isEmpty() ||
                d.getText().trim().isEmpty() ||
                ans.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                    "Please fill all fields.");
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setInt(1, examId);
            ps.setString(2, q.getText().trim());
            ps.setString(3, a.getText().trim());
            ps.setString(4, b.getText().trim());
            ps.setString(5, c.getText().trim());
            ps.setString(6, d.getText().trim());
            ps.setString(7, ans.getText().trim().toUpperCase());

            ps.executeUpdate();

            addedQuestions++;
            progressLabel.setText(
                "Questions Added: " + addedQuestions + " / " + totalQuestionsRequired
            );

            JOptionPane.showMessageDialog(this, "Question Added Successfully!");

            q.setText("");
            a.setText("");
            b.setText("");
            c.setText("");
            d.setText("");
            ans.setText("");

            if (addedQuestions == totalQuestionsRequired) {
                JOptionPane.showMessageDialog(this,
                    "All questions added successfully!\nExam is ready for students.");
                dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error while adding question: " + e.getMessage());
        }
    }
}
