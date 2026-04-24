package onlineexam.ui.student;

import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class StartExamFrame extends JFrame {

    private int studentId;
    private JComboBox<String> examDropdown;
    private JButton startButton;

    private Map<String, Integer> examMap = new HashMap<>();

    public StartExamFrame(int studentId) {

        this.studentId = studentId;

        setTitle("Start Exam");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();
        loadExams();

        setVisible(true);
    }

    private void initUI() {

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 10, 20)
        );

        JLabel titleLabel = new JLabel(
                "START EXAM PORTAL",
                JLabel.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutBtn);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        formPanel.setBorder(
                BorderFactory.createTitledBorder("Exam Selection")
        );
        formPanel.setPreferredSize(new Dimension(400, 200));

        JLabel selectLabel = new JLabel(
                "Select Available Exam",
                JLabel.CENTER
        );
        selectLabel.setFont(new Font("Arial", Font.BOLD, 16));

        examDropdown = new JComboBox<>();

        startButton = new JButton("Start Selected Exam");
        startButton.setEnabled(false);
        startButton.setFont(new Font("Arial", Font.BOLD, 15));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 15));

        startButton.addActionListener(e -> startExam());

        backButton.addActionListener(e -> {
            dispose();
            new StudentFrame(studentId);
        });

        formPanel.add(selectLabel);
        formPanel.add(examDropdown);
        formPanel.add(startButton);
        formPanel.add(backButton);

        centerPanel.add(formPanel);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JLabel footer = new JLabel(
                "Exam can only start after examiner approval"
        );
        footer.setFont(new Font("Arial", Font.ITALIC, 13));

        bottomPanel.add(footer);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadExams() {

    try (Connection conn = DBConnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(
                "SELECT id, exam_title FROM exams"
        );

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String title = rs.getString("exam_title");

            examDropdown.addItem(title);
            examMap.put(title, rs.getInt("id"));
        }

        if (examDropdown.getItemCount() > 0) {
            startButton.setEnabled(true);
        }

    } catch (Exception e) {
        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Error loading exams."
        );
    }
}

private void startExam() {

    String selectedExam =
            (String) examDropdown.getSelectedItem();

    if (selectedExam == null) {
        JOptionPane.showMessageDialog(
                this,
                "Please select an exam."
        );
        return;
    }

    int examId = examMap.get(selectedExam);

    dispose();
    new ExamFrame(studentId, examId);
}
}