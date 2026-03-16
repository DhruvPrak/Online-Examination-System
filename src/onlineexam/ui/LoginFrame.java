package onlineexam.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import onlineexam.util.DBConnection;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginFrame() {

        setTitle("Online Examination System - Login");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5,2,10,10));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        roleBox = new JComboBox<>();
        roleBox.addItem("ADMIN");
        roleBox.addItem("EXAMINER");
        roleBox.addItem("STUDENT");

        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(this::loginUser);

        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(roleLabel);
        add(roleBox);
        add(new JLabel());
        add(loginBtn);

        setVisible(true);
    }

    private void loginUser(ActionEvent e) {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {

                JOptionPane.showMessageDialog(this,"Login Successful!");

                if(role.equals("ADMIN")) {
                    new AdminFrame();
                }
                else if(role.equals("EXAMINER")) {
                    new ExaminerFrame();
                }
                else {
                    new StudentFrame();
                }

                dispose();

            } else {

                JOptionPane.showMessageDialog(this,"Invalid Credentials!");

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}