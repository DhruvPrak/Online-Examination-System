package onlineexam.ui.admin;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class CreateExamFrame extends JFrame {


JTextField examIdField, titleField, questionCountField, durationField;

public CreateExamFrame() {

    setTitle("Create Exam");
    setSize(400,300);
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

    JPanel panel = new JPanel(new GridLayout(5,2,10,10));

    examIdField = new JTextField();
    titleField = new JTextField();
    questionCountField = new JTextField();
    durationField = new JTextField();

    JButton createBtn = new JButton("Create Exam");

    panel.add(new JLabel("Exam ID:"));
    panel.add(examIdField);

    panel.add(new JLabel("Exam Title:"));
    panel.add(titleField);

    panel.add(new JLabel("Questions:"));
    panel.add(questionCountField);

    panel.add(new JLabel("Duration (min):"));
    panel.add(durationField);

    panel.add(new JLabel());
    panel.add(createBtn);

    add(panel, BorderLayout.CENTER);

    createBtn.addActionListener(e -> createExam());

    setVisible(true);
}

private void createExam() {

    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO exams (id, exam_title, total_questions, questions_to_display, duration) VALUES (?,?,?,?,?)"
        );

        int examId = Integer.parseInt(examIdField.getText());
        int q = Integer.parseInt(questionCountField.getText());
        int duration = Integer.parseInt(durationField.getText()) * 60;

        stmt.setInt(1, examId);
        stmt.setString(2, titleField.getText());
        stmt.setInt(3, q);
        stmt.setInt(4, q);
        stmt.setInt(5, duration);

        stmt.executeUpdate();

        JOptionPane.showMessageDialog(this,"Exam Created!");

        new AddExamQuestionsFrame(examId);
        dispose();

    } catch(Exception e) {
        e.printStackTrace();
    }
}


}
