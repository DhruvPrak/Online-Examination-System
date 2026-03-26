package onlineexam.ui.examiner;

import java.awt.*;
import javax.swing.*;

public class ExaminerFrame extends JFrame {

    int examinerId;

    JButton addQuestionBtn;
    JButton viewQuestionsBtn;
    JButton manageStudentsBtn;
    JButton startExamBtn;

    public ExaminerFrame(int examinerId){

        this.examinerId = examinerId;

        setTitle("Examiner Dashboard");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Examiner Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        addQuestionBtn = new JButton("Add Question");
        viewQuestionsBtn = new JButton("View My Questions");
        manageStudentsBtn = new JButton("Manage Students");
        startExamBtn = new JButton("Start Exam");

        addQuestionBtn.addActionListener(e -> {

            new AddQuestionFrame(examinerId);

        });

        viewQuestionsBtn.addActionListener(e -> {

            new ViewQuestionsFrame(examinerId);

        });

        manageStudentsBtn.addActionListener(e -> {

            new ManageStudentsFrame();

        });

        startExamBtn.addActionListener(e -> {

            new ExamControlFrame(examinerId);

        });

        setLayout(new GridLayout(5,1,10,10));

        add(title);
        add(addQuestionBtn);
        add(viewQuestionsBtn);
        add(manageStudentsBtn);
        add(startExamBtn);

        setVisible(true);
    }
}