package onlineexam.ui.student;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class StudentResultsFrame extends JFrame {

public StudentResultsFrame(int studentId){

    setTitle("My Results");
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

    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(new String[]{"Exam", "Score"});

    JTable table = new JTable(model);
    add(new JScrollPane(table), BorderLayout.CENTER);

    try(Connection conn = DBConnection.getConnection()){

        PreparedStatement ps = conn.prepareStatement(
            "SELECT e.exam_title, r.score FROM results r " +
            "JOIN exams e ON r.exam_id = e.id " +
            "WHERE r.student_id=?"
        );

        ps.setInt(1, studentId);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            model.addRow(new Object[]{
                rs.getString("exam_title"),
                rs.getInt("score")
            });
        }

    }catch(Exception e){
        e.printStackTrace();
    }

    setVisible(true);
}

}