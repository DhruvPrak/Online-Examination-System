package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageStudentsFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ManageStudentsFrame(){

        setTitle("Manage Students");
        setSize(600,400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Status");

        table = new JTable(model);

        JButton approveBtn = new JButton("Approve Student");
        JButton banBtn = new JButton("Ban Student");

        approveBtn.addActionListener(e -> updateStatus("APPROVED"));
        banBtn.addActionListener(e -> updateStatus("BANNED"));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.add(approveBtn);
        panel.add(banBtn);

        add(panel, BorderLayout.SOUTH);

        loadStudents();

        setVisible(true);
    }

    private void loadStudents(){

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

    private void updateStatus(String status){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a student first");
            return;
        }

        int studentId = (int) model.getValueAt(row,0);

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET exam_status=? WHERE id=?"
            );

            ps.setString(1,status);
            ps.setInt(2,studentId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Student status updated!");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}