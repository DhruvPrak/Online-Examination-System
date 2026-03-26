package onlineexam.ui.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import onlineexam.util.DBConnection;

public class StartExamFrame extends JFrame {

    private int studentId;
    private JComboBox<String> examDropdown;
    private JButton startButton;

    private Map<String, Integer> examMap = new HashMap<>();

    public StartExamFrame(int studentId) {

        this.studentId = studentId;

        setTitle("Start Exam");
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadExams();

        setVisible(true);
    }

    private void initUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        JLabel label = new JLabel("Select Exam", JLabel.CENTER);

        examDropdown = new JComboBox<>();

        startButton = new JButton("Start Exam");

        startButton.addActionListener((ActionEvent e) -> startExam());

        panel.add(label);
        panel.add(examDropdown);
        panel.add(startButton);

        add(panel);
    }

    private void loadExams() {

        try {

            Connection conn = DBConnection.getConnection();

            String query = "SELECT id, exam_title FROM exams";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                int examId = rs.getInt("id");
                String title = rs.getString("exam_title");

                examDropdown.addItem(title);
                examMap.put(title, examId);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void startExam() {

        String selectedExam = (String) examDropdown.getSelectedItem();

        if(selectedExam == null) {

            JOptionPane.showMessageDialog(this, "No exam available.");
            return;
        }

        int examId = examMap.get(selectedExam);

        dispose();

        new ExamFrame(studentId, examId);
    }
}