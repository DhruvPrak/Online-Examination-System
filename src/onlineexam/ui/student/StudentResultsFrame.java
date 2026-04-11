package onlineexam.ui.student;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import onlineexam.ui.LoginFrame;

public class StudentResultsFrame extends JFrame {

public StudentResultsFrame(int studentId){

    setTitle("My Results");
    setSize(600,400);
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

    JTable table = new JTable(new DefaultTableModel());
    add(new JScrollPane(table), BorderLayout.CENTER);

    setVisible(true);
}


}
