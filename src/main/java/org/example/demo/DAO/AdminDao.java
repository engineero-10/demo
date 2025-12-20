package org.example.demo.DAO;

import org.example.demo.util.DBConnection;
import org.example.demo.util.ErrorHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    public static boolean checkAdmin(String username, String password) {
        String sql = "select * from admin where username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet result = ps.executeQuery()) {
                return result.next();
            }

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Login Error",
                    "Failed to verify admin credentials",
                    e
            );
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred during login",
                    e.getMessage()
            );
            return false;
        }
    }

    public static boolean addAdmin(String name, String email, String username, String password) {
        String sql = "INSERT INTO ADMIN (name, email, username, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);

            int result = ps.executeUpdate();
            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Admin account created successfully!");
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                ErrorHandler.showError(
                        "Registration Error",
                        "Username already exists",
                        "Please choose a different username."
                );
            } else {
                ErrorHandler.showErrorWithException(
                        "Registration Error",
                        "Failed to create admin account",
                        e
                );
            }
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred during registration",
                    e.getMessage()
            );
            return false;
        }
    }

    public static boolean assignEmployeeInProject(int empId, int proId) {
        String sql = "insert into employee_project (project_id, employee_id) values (?,?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, proId);
            ps.setInt(2, empId);
            int result = ps.executeUpdate();

            if (result > 0) {
                ErrorHandler.showSuccess(
                        "Success",
                        "Employee assigned to project successfully!"
                );
                return true;
            }
            return false;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                ErrorHandler.showWarning(
                        "Assignment Warning",
                        "This employee is already assigned to this project."
                );
            } else if (e.getMessage().contains("foreign key constraint")) {
                ErrorHandler.showError(
                        "Assignment Error",
                        "Invalid Employee ID or Project ID",
                        "Please check that both IDs exist in the database."
                );
            } else {
                ErrorHandler.showErrorWithException(
                        "Assignment Error",
                        "Failed to assign employee to project",
                        e
                );
            }
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred",
                    e.getMessage()
            );
            return false;
        }
    }
}