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
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

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

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        String sql = "SELECT role FROM users WHERE username=? AND password=?";

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
                return;
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String dbRole = rs.getString("role");

                if (role.equalsIgnoreCase(dbRole)) {

                    JOptionPane.showMessageDialog(this, "Login Successful!");

                    openDashboard(role);
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect role selected!");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database.");
        }
    }

    private void openDashboard(String role) {

        switch (role) {

            case "ADMIN":
                new AdminFrame();
                break;

            case "EXAMINER":
                new ExaminerFrame();
                break;

            case "STUDENT":
                new StudentFrame();
                break;

            default:
                JOptionPane.showMessageDialog(this, "Unknown role.");
        }
    }
}