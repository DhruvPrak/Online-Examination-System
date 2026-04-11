package onlineexam.ui.examiner;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class ManageStudentsFrame extends JFrame {

JTable table;
DefaultTableModel model;

JButton approveBtn, banBtn;

public ManageStudentsFrame(){

    setTitle("Manage Students");
    setSize(600,400);
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

    model = new DefaultTableModel(
        new String[]{"ID","Username","Status"},0
    );

    table = new JTable(model);

    add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel bottom = new JPanel();

    approveBtn = new JButton("Approve");
    banBtn = new JButton("Ban");

    bottom.add(approveBtn);
    bottom.add(banBtn);

    add(bottom, BorderLayout.SOUTH);

    loadStudents();

    approveBtn.addActionListener(e -> updateStatus("APPROVED"));
    banBtn.addActionListener(e -> updateStatus("BANNED"));

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

private void updateStatus(String newStatus){

    int row = table.getSelectedRow();

    if(row == -1){
        JOptionPane.showMessageDialog(this,"Select a student!");
        return;
    }

    int studentId = (int) model.getValueAt(row,0);

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "UPDATE users SET exam_status=? WHERE id=?"
        );

        ps.setString(1,newStatus);
        ps.setInt(2,studentId);

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this,"Updated!");

        loadStudents();

    }catch(Exception e){
        e.printStackTrace();
    }
}

}
