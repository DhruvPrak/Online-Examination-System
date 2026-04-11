package onlineexam.ui.admin;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class ViewResultsFrame extends JFrame {


private JTable table;
private DefaultTableModel model;

public ViewResultsFrame() {

    setTitle("All Student Results");
    setSize(900,450);
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

    initUI();
    loadResults();

    setVisible(true);
}

private void initUI() {

    String[] columns = {"Student","Exam","Score","Total","%","Grade","Status","Time"};

    model = new DefaultTableModel(columns, 0);
    table = new JTable(model);

    add(new JScrollPane(table), BorderLayout.CENTER);
}

private void loadResults() {
    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(
            "SELECT u.username, e.exam_title, r.score, r.total_marks, r.status, r.submitted_at " +
            "FROM results r JOIN users u ON r.student_id=u.id JOIN exams e ON r.exam_id=e.id"
        );

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            double per = (rs.getInt("score") * 100.0) / rs.getInt("total_marks");

            model.addRow(new Object[]{
                rs.getString("username"),
                rs.getString("exam_title"),
                rs.getInt("score"),
                rs.getInt("total_marks"),
                String.format("%.2f", per),
                grade(per),
                rs.getString("status"),
                rs.getString("submitted_at")
            });
        }

    } catch(Exception e) { e.printStackTrace(); }
}

private String grade(double p){
    if(p>=90) return "A";
    if(p>=75) return "B";
    if(p>=60) return "C";
    if(p>=40) return "D";
    return "F";
}

}
