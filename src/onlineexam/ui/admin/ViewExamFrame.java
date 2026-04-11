package onlineexam.ui.admin;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class ViewExamFrame extends JFrame {


JTextField examIdField;
JTable table;
DefaultTableModel model;

public ViewExamFrame() {

    setTitle("View Exam");
    setSize(700,400);
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

    JPanel top = new JPanel();

    examIdField = new JTextField(10);
    JButton loadBtn = new JButton("Load Exam");

    top.add(new JLabel("Exam ID:"));
    top.add(examIdField);
    top.add(loadBtn);

    add(top, BorderLayout.SOUTH);

    model = new DefaultTableModel();
    table = new JTable(model);

    model.addColumn("Question");
    model.addColumn("A");
    model.addColumn("B");
    model.addColumn("C");
    model.addColumn("D");
    model.addColumn("Answer");

    add(new JScrollPane(table), BorderLayout.CENTER);

    loadBtn.addActionListener(e -> loadExam());

    setVisible(true);
}

private void loadExam() {

    model.setRowCount(0);

    try(Connection conn = DBConnection.getConnection()) {

        PreparedStatement stmt = conn.prepareStatement(
            "SELECT * FROM questions WHERE exam_id=?"
        );

        stmt.setInt(1, Integer.parseInt(examIdField.getText()));

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer")
            });
        }

    } catch(Exception e) {
        e.printStackTrace();
    }
}


}
