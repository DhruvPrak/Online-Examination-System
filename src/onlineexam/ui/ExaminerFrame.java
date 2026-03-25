package onlineexam.ui;

import javax.swing.*;
import java.awt.*;

public class ExaminerFrame extends JFrame {

    int examinerId;

    JButton addQuestionBtn;
    JButton viewQuestionsBtn;
    JButton manageStudentsBtn;
    JButton startExamBtn;

    public ExaminerFrame(int examinerId){

        this.examinerId = examinerId;

        setTitle("Examiner Dashboard");
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        setLayout(new FlowLayout());

        add(addQuestionBtn);
        add(viewQuestionsBtn);

        setVisible(true);
    }
}