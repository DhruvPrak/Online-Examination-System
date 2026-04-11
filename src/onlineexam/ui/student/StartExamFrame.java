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
    setSize(400,250);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setLayout(new BorderLayout());

    initUI();
    loadExams();

    setVisible(true);
}

private void initUI() {

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton logoutBtn = new JButton("Logout");

    logoutBtn.addActionListener(e -> {
        dispose();
        new LoginFrame();
    });

    topPanel.add(logoutBtn);
    add(topPanel, BorderLayout.NORTH);

    JPanel panel = new JPanel(new GridLayout(3,1,10,10));
    panel.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

    JLabel label = new JLabel("Select Exam", JLabel.CENTER);

    examDropdown = new JComboBox<>();

    startButton = new JButton("Start Exam");
    startButton.setEnabled(false);

    startButton.addActionListener(e -> startExam());

    panel.add(label);
    panel.add(examDropdown);
    panel.add(startButton);

    add(panel, BorderLayout.CENTER);
}

private void checkExamStatus() {

    String selectedExam = (String) examDropdown.getSelectedItem();
    if(selectedExam == null) return;

    int examId = examMap.get(selectedExam);

    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT status FROM exams WHERE id=?"
        );
        ps.setInt(1, examId);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            String status = rs.getString("status");
            startButton.setEnabled("STARTED".equalsIgnoreCase(status));
        }

    } catch(Exception e) {
        e.printStackTrace();
    }
}

private void loadExams() {

    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT id, exam_title FROM exams"
        );

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            String title = rs.getString("exam_title");
            examDropdown.addItem(title);
            examMap.put(title, rs.getInt("id"));
        }

        examDropdown.addActionListener(e -> checkExamStatus());

        if(examDropdown.getItemCount() > 0) {
            examDropdown.setSelectedIndex(0);
            checkExamStatus();
        }

    } catch(Exception e) {
        e.printStackTrace();
    }
}

private void startExam() {

    String selectedExam = (String) examDropdown.getSelectedItem();
    if(selectedExam == null) return;

    int examId = examMap.get(selectedExam);

    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement ps1 = conn.prepareStatement(
            "SELECT exam_status FROM users WHERE id=?"
        );
        ps1.setInt(1, studentId);

        ResultSet rs1 = ps1.executeQuery();

        if(rs1.next()){
            String status = rs1.getString("exam_status");

            if("BANNED".equalsIgnoreCase(status)){
                JOptionPane.showMessageDialog(this,"You are banned!");
                return;
            }

            if(!"APPROVED".equalsIgnoreCase(status) && !"IN_EXAM".equalsIgnoreCase(status)){
                JOptionPane.showMessageDialog(this,"Not approved!");
                return;
            }
        }

        PreparedStatement ps2 = conn.prepareStatement(
            "SELECT status FROM exams WHERE id=?"
        );
        ps2.setInt(1, examId);

        ResultSet rs2 = ps2.executeQuery();

        if(rs2.next()){
            if(!"STARTED".equalsIgnoreCase(rs2.getString("status"))){
                JOptionPane.showMessageDialog(this,"Exam not started!");
                return;
            }
        }

    } catch(Exception e) {
        e.printStackTrace();
        return;
    }

    dispose();
    new ExamFrame(studentId, examId);
}

}
