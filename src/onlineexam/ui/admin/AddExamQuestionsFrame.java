package onlineexam.ui.admin;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import onlineexam.ui.LoginFrame;
import onlineexam.util.DBConnection;

public class AddExamQuestionsFrame extends JFrame {


JTextField q,a,b,c,d,ans;
int examId;

public AddExamQuestionsFrame(int examId) {

    this.examId = examId;

    setTitle("Add Questions");
    setSize(500,400);
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

    JPanel panel = new JPanel(new GridLayout(7,2,10,10));

    q=new JTextField(); a=new JTextField(); b=new JTextField();
    c=new JTextField(); d=new JTextField(); ans=new JTextField();

    JButton addBtn=new JButton("Add");

    panel.add(new JLabel("Question")); panel.add(q);
    panel.add(new JLabel("A")); panel.add(a);
    panel.add(new JLabel("B")); panel.add(b);
    panel.add(new JLabel("C")); panel.add(c);
    panel.add(new JLabel("D")); panel.add(d);
    panel.add(new JLabel("Answer")); panel.add(ans);
    panel.add(new JLabel()); panel.add(addBtn);

    add(panel, BorderLayout.CENTER);

    addBtn.addActionListener(e->addQ());

    setVisible(true);
}

private void addQ(){
    try(Connection conn=DBConnection.getConnection()){

        PreparedStatement ps=conn.prepareStatement(
            "INSERT INTO questions VALUES(NULL,?,?,?,?,?,?,?)"
        );

        ps.setInt(1,examId);
        ps.setString(2,q.getText());
        ps.setString(3,a.getText());
        ps.setString(4,b.getText());
        ps.setString(5,c.getText());
        ps.setString(6,d.getText());
        ps.setString(7,ans.getText());

        ps.executeUpdate();
        JOptionPane.showMessageDialog(this,"Added");

        q.setText(""); a.setText(""); b.setText("");
        c.setText(""); d.setText(""); ans.setText("");

    }catch(Exception e){ e.printStackTrace(); }
}


}
