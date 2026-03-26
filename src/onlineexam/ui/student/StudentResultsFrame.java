package onlineexam.ui.student;

import onlineexam.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentResultsFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    int studentId;

    public StudentResultsFrame(int studentId){

        this.studentId = studentId;

        setTitle("My Exam Results");
        setSize(600,400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();

        model.addColumn("Exam ID");
        model.addColumn("Score");
        model.addColumn("Total Marks");
        model.addColumn("Status");
        model.addColumn("Submitted At");

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadResults();

        setVisible(true);
    }

    private void loadResults(){

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT exam_id, score, total_marks, status, submitted_at FROM results WHERE student_id=?"
            );

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("exam_id"),
                        rs.getInt("score"),
                        rs.getInt("total_marks"),
                        rs.getString("status"),
                        rs.getTimestamp("submitted_at")
                });

            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}