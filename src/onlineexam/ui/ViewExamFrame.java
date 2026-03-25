package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewExamFrame extends JFrame {

    JTextField examIdField;
    JTable table;
    DefaultTableModel model;

    public ViewExamFrame() {

        setTitle("View Exam");
        setSize(700,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();

        examIdField = new JTextField(10);
        JButton loadBtn = new JButton("Load Exam");

        top.add(new JLabel("Enter Exam ID:"));
        top.add(examIdField);
        top.add(loadBtn);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("Question");
        model.addColumn("A");
        model.addColumn("B");
        model.addColumn("C");
        model.addColumn("D");
        model.addColumn("Answer");

        loadBtn.addActionListener(e -> loadExam());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadExam() {

        model.setRowCount(0);

        String sql = "SELECT * FROM questions WHERE exam_id=?";

        try(Connection conn = DBConnection.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(sql);
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

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}