package onlineexam.ui.examiner;

import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.util.DBConnection;

public class ExamControlFrame extends JFrame {

JTable table;
DefaultTableModel model;

JButton startExamBtn, banStudentBtn;

JComboBox<String> examBox;
Map<String, Integer> examMap = new HashMap<>();

int examinerId;

public ExamControlFrame(int examinerId){

    this.examinerId = examinerId;

    setTitle("Exam Control Panel");
    setSize(700,450);
    setLocationRelativeTo(null);

    model = new DefaultTableModel();
    model.addColumn("Student ID");
    model.addColumn("Username");
    model.addColumn("Status");

    table = new JTable(model);

    examBox = new JComboBox<>();

    startExamBtn = new JButton("Start Selected Exam");
    banStudentBtn = new JButton("Ban Selected Student");

    startExamBtn.addActionListener(e -> startExam());
    banStudentBtn.addActionListener(e -> banStudent());

    JPanel top = new JPanel();
    top.add(new JLabel("Select Exam:"));
    top.add(examBox);
    top.add(startExamBtn);

    JPanel bottom = new JPanel();
    bottom.add(banStudentBtn);

    add(top, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(bottom, BorderLayout.SOUTH);

    loadStudents();
    loadExams();

    setVisible(true);
}

private void loadStudents(){
    model.setRowCount(0);

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "SELECT id, username, exam_status FROM users WHERE role='STUDENT'"
        );

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("exam_status")
            });
        }

    }catch(Exception e){
        e.printStackTrace();
    }
}

private void loadExams(){

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "SELECT id, exam_title FROM exams"
        );

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            examBox.addItem(rs.getString("exam_title"));
            examMap.put(rs.getString("exam_title"), rs.getInt("id"));
        }

    }catch(Exception e){
        e.printStackTrace();
    }
}

private void startExam(){

    String selected = (String) examBox.getSelectedItem();

    if(selected == null){
        JOptionPane.showMessageDialog(this,"No exam selected!");
        return;
    }

    int examId = examMap.get(selected);

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE exams SET status='STARTED' WHERE id=?"
        );

        ps.setInt(1, examId);
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this,"Exam started successfully!");

    }catch(Exception e){
        e.printStackTrace();
    }
}

private void banStudent(){

    int row = table.getSelectedRow();

    if(row == -1){
        JOptionPane.showMessageDialog(this,"Select a student first!");
        return;
    }

    int studentId = (int) model.getValueAt(row,0);

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE users SET exam_status='BANNED' WHERE id=?"
        );

        ps.setInt(1, studentId);
        ps.executeUpdate();

        JOptionPane.showMessageDialog(this,"Student banned!");

        loadStudents();

    }catch(Exception e){
        e.printStackTrace();
    }
}


}
