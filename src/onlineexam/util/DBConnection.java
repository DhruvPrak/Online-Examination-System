package onlineexam.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/online_exam_system?useSSL=false&serverTimezone=UTC";

    // Safer DB user instead of root
    private static final String USER = "exam_user";
    private static final String PASSWORD = "Exam@123";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}