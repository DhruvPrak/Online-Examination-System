package onlineexam.ui.examiner;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.util.DBConnection;

public class ExamControlFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    JButton startExamBtn;
    JButton banStudentBtn;

    int examinerId;

    public ExamControlFrame(int examinerId){

        this.examinerId = examinerId;

        setTitle("Exam Control Panel");
        setSize(600,400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();

        model.addColumn("Student ID");
        model.addColumn("Username");
        model.addColumn("Status");

        table = new JTable(model);

        startExamBtn = new JButton("Start Exam For Approved Students");
        banStudentBtn = new JButton("Ban Selected Student");

        startExamBtn.addActionListener(e -> startExam());

        banStudentBtn.addActionListener(e -> banStudent());

        JPanel panel = new JPanel();
        panel.add(startExamBtn);
        panel.add(banStudentBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        loadStudents();

        setVisible(true);
    }

    private void loadStudents(){

        model.setRowCount(0);

        try{

            Connection conn = DBConnection.getConnection();

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

    private void startExam(){

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET exam_status='IN_EXAM' WHERE exam_status='APPROVED'"
            );

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Exam started for approved students!");

            loadStudents();

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

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET exam_status='BANNED' WHERE id=?"
            );

            ps.setInt(1,studentId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Student banned from exam!");

            loadStudents();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}