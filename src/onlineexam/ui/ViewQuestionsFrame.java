package onlineexam.ui;

import javax.swing.*;

public class ViewQuestionsFrame extends JFrame {

    public ViewQuestionsFrame() {

        setTitle("View Questions");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Question List (Coming Soon)", JLabel.CENTER);
        add(label);

        setVisible(true);
    }
}