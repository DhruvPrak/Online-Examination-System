package onlineexam.ui;

import onlineexam.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewQuestionsFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    int examinerId;

    public ViewQuestionsFrame(int examinerId){

        this.examinerId = examinerId;

        setTitle("My Questions");
        setSize(600,400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Question");
        model.addColumn("Correct Answer");

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadQuestions();

        setVisible(true);
    }

    private void loadQuestions(){

        try{

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, question_text, correct_answer FROM questions WHERE created_by=?"
            );

            ps.setInt(1, examinerId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{

                        rs.getInt("id"),
                        rs.getString("question_text"),
                        rs.getString("correct_answer")

                });

            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }
}