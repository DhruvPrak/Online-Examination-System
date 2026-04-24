package onlineexam.ui;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import onlineexam.ui.admin.AdminFrame;
import onlineexam.ui.examiner.ExaminerFrame;
import onlineexam.ui.student.StudentFrame;
import onlineexam.util.DBConnection;
import onlineexam.util.PasswordUtil;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginFrame() {

        setTitle("Online Examination System - Login");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        roleBox = new JComboBox<>(new String[]{
                "ADMIN",
                "EXAMINER",
                "STUDENT"
        });

        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);

        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String selectedRole = roleBox.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed.");
                return;
            }

            // Fetch user by username only
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, role, password FROM users WHERE username=?"
            );

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("id");
                String dbRole = rs.getString("role");
                String hashedPassword = rs.getString("password");

                // Verify password using BCrypt
                boolean passwordMatched = PasswordUtil.checkPassword(
                        password,
                        hashedPassword
                );

                if (!passwordMatched) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid password!");
                    return;
                }

                // Verify selected role
                if (!dbRole.equalsIgnoreCase(selectedRole)) {
                    JOptionPane.showMessageDialog(this,
                            "Incorrect role selected!");
                    return;
                }

                JOptionPane.showMessageDialog(this,
                        "Login Successful!");

                dispose();

                if (selectedRole.equalsIgnoreCase("ADMIN")) {
                    new AdminFrame();
                }
                else if (selectedRole.equalsIgnoreCase("EXAMINER")) {
                    new ExaminerFrame(userId);
                }
                else if (selectedRole.equalsIgnoreCase("STUDENT")) {
                    new StudentFrame(userId);
                }

            } else {
                JOptionPane.showMessageDialog(this,
                        "User not found!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Login Error!");
            e.printStackTrace();
        }
    }
}
