package onlineexam.ui;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    public AdminFrame() {

        setTitle("Admin Dashboard");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,1,10,10));

        JLabel title = new JLabel("Admin Dashboard", JLabel.CENTER);

        JButton addQuestionBtn = new JButton("Add Question");
        JButton viewQuestionsBtn = new JButton("View Questions");

        addQuestionBtn.addActionListener(e -> new AddQuestionFrame());
        viewQuestionsBtn.addActionListener(e -> new ViewQuestionsFrame());

        add(title);
        add(addQuestionBtn);
        add(viewQuestionsBtn);

        setVisible(true);
    }
}