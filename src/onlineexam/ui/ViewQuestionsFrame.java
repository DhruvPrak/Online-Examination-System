package onlineexam.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.util.DBConnection;

public class ViewQuestionsFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewQuestionsFrame() {

        setTitle("Question Bank");
        setSize(700,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Question");
        model.addColumn("Option A");
        model.addColumn("Option B");
        model.addColumn("Option C");
        model.addColumn("Option D");
        model.addColumn("Answer");

        loadQuestions();

        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadQuestions() {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM questions";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer")
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}