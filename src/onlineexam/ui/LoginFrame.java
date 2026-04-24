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

        setTitle("Online Examination System");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();

        setVisible(true);
    }

    private void initUI() {

        /*
         * TOP TITLE PANEL
         */
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel(
                "ONLINE EXAMINATION SYSTEM",
                JLabel.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel(
                "Secure Login Portal",
                JLabel.CENTER
        );

        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(subtitleLabel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        /*
         * CENTER LOGIN PANEL
         */
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 30, 20, 30)
        );

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        formPanel.setBorder(
                BorderFactory.createTitledBorder("Login Details")
        );

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        roleBox = new JComboBox<>(new String[]{
                "ADMIN",
                "EXAMINER",
                "STUDENT"
        });

        JButton loginButton = new JButton("Login");

        loginButton.setFont(new Font("Arial", Font.BOLD, 15));

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleBox);

        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        centerPanel.add(formPanel);

        add(centerPanel, BorderLayout.CENTER);

        /*
         * FOOTER PANEL
         */
        JPanel bottomPanel = new JPanel();

        JLabel footerLabel = new JLabel(
                "Java Swing + MySQL Based Examination System"
        );

        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        bottomPanel.add(footerLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> login());
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String selectedRole = roleBox.getSelectedItem().toString();

        /*
         * INPUT VALIDATION
         */
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields."
            );
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database connection failed."
                );
                return;
            }

            /*
             * SAFE LOGIN USING PREPARED STATEMENT
             */
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, role, password FROM users WHERE username=?"
            );

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("id");
                String dbRole = rs.getString("role");
                String hashedPassword = rs.getString("password");

                /*
                 * BCrypt PASSWORD CHECK
                 */
                boolean passwordMatched = PasswordUtil.checkPassword(
                        password,
                        hashedPassword
                );

                if (!passwordMatched) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid password!"
                    );
                    return;
                }

                /*
                 * ROLE VALIDATION
                 */
                if (!dbRole.equalsIgnoreCase(selectedRole)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Incorrect role selected!"
                    );
                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!"
                );

                dispose();

                /*
                 * ROLE-BASED REDIRECTION
                 */
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
                JOptionPane.showMessageDialog(
                        this,
                        "User not found!"
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login Error!"
            );
            e.printStackTrace();
        }
    }
}