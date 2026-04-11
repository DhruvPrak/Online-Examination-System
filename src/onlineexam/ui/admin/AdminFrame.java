package onlineexam.ui.admin;

import java.awt.*;
import javax.swing.*;
import onlineexam.ui.LoginFrame;

public class AdminFrame extends JFrame {

public AdminFrame() {

    setTitle("Admin Dashboard");
    setSize(400,250);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setLayout(new BorderLayout());

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton logoutBtn = new JButton("Logout");

    logoutBtn.addActionListener(e -> {
        dispose();
        new LoginFrame();
    });

    topPanel.add(logoutBtn);
    add(topPanel, BorderLayout.NORTH);

    JPanel centerPanel = new JPanel(new GridLayout(3,1,10,10));

    JLabel title = new JLabel("Admin Dashboard", JLabel.CENTER);

    JButton createExamBtn = new JButton("Create Exam");
    JButton viewExamBtn = new JButton("View Exam");
    JButton viewResultsBtn = new JButton("View Results");

    createExamBtn.addActionListener(e -> new CreateExamFrame());
    viewExamBtn.addActionListener(e -> new ViewExamFrame());
    viewResultsBtn.addActionListener(e -> new ViewResultsFrame());

    centerPanel.add(title);
    centerPanel.add(createExamBtn);
    centerPanel.add(viewExamBtn);
    centerPanel.add(viewResultsBtn);

    add(centerPanel, BorderLayout.CENTER);

    setVisible(true);
}

}
