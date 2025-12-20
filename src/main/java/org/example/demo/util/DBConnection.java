package org.example.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/employee_management";
    private static final String user = "root";
    private static final String password = "1204";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            ErrorHandler.showDatabaseError(e);
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            ErrorHandler.showError(
                    "Driver Error",
                    "MySQL Driver not found",
                    "Please make sure MySQL Connector is properly installed.\n\nError: " + e.getMessage()
            );
            throw new RuntimeException("MySQL Driver not found: " + e.getMessage());
        }
    }
}