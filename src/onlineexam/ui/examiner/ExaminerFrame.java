package onlineexam.ui.examiner;

import java.awt.*;
import javax.swing.*;
import onlineexam.ui.LoginFrame;

public class ExaminerFrame extends JFrame {

int examinerId;

public ExaminerFrame(int examinerId){

    this.examinerId = examinerId;

    setTitle("Examiner Dashboard");
    setSize(400,300);
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

    JPanel panel = new JPanel(new GridLayout(4,1,10,10));

    JButton addQuestionBtn = new JButton("Add Question");
    JButton viewQuestionsBtn = new JButton("View Questions");
    JButton manageStudentsBtn = new JButton("Manage Students");
    JButton startExamBtn = new JButton("Start Exam");

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

    panel.add(addQuestionBtn);
    panel.add(viewQuestionsBtn);
    panel.add(manageStudentsBtn);
    panel.add(startExamBtn);

    add(panel, BorderLayout.CENTER);

    setVisible(true);
}

}
