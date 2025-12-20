package org.example.demo.DAO;

import org.example.demo.model.Employee;
import org.example.demo.model.EmployeeWithProjects;
import org.example.demo.util.DBConnection;
import org.example.demo.util.ErrorHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    public static boolean addEmployee(String name, String email, String position) {
        String sql = "INSERT INTO EMPLOYEE (name, email, position) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, position);

            int result = ps.executeUpdate();
            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Employee added successfully!");
                return true;
            }
            return false;

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Add Employee Error",
                    "Failed to add employee",
                    e
            );
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while adding employee",
                    e.getMessage()
            );
            return false;
        }
    }

    public static boolean updateEmployee(int id, String name, String email, String position) {
        String sql = "update employee set name=?, email=?, position=? where emp_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, position);
            ps.setInt(4, id);

            int result = ps.executeUpdate();
            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Employee updated successfully!");
                return true;
            } else {
                ErrorHandler.showWarning(
                        "Update Warning",
                        "No employee found with ID: " + id
                );
                return false;
            }

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Update Employee Error",
                    "Failed to update employee",
                    e
            );
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while updating employee",
                    e.getMessage()
            );
            return false;
        }
    }

    public static boolean deleteEmployee(int id) {
        String sql = "delete from employee where emp_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();

            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Employee deleted successfully!");
                return true;
            } else {
                ErrorHandler.showWarning(
                        "Delete Warning",
                        "No employee found with ID: " + id
                );
                return false;
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                ErrorHandler.showError(
                        "Delete Error",
                        "Cannot delete employee",
                        "This employee is assigned to projects. Please remove project assignments first."
                );
            } else {
                ErrorHandler.showErrorWithException(
                        "Delete Employee Error",
                        "Failed to delete employee",
                        e
                );
            }
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while deleting employee",
                    e.getMessage()
            );
            return false;
        }
    }

    public static Employee getEmployeeById(int empId) {
        Employee employee = null;
        String sql = "select * from employee where emp_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                employee = new Employee(
                        result.getInt("emp_id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("position")
                );
            }

            if (employee == null) {
                ErrorHandler.showWarning(
                        "Not Found",
                        "No employee found with ID: " + empId
                );
            }

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Fetch Employee Error",
                    "Failed to fetch employee data",
                    e
            );
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while fetching employee",
                    e.getMessage()
            );
        }
        return employee;
    }

    public static List<EmployeeWithProjects> getEmployeeWithProjects() {
        List<EmployeeWithProjects> employees = new ArrayList<>();
        String sql = "select * from employee_with_project";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                employees.add(new EmployeeWithProjects(
                        result.getInt("Emp_ID"),
                        result.getString("Name"),
                        result.getString("Email"),
                        result.getString("Position"),
                        result.getInt("Pro_ID"),
                        result.getString("Project Name"),
                        result.getString("Start Date")
                ));
            }
        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Fetch Data Error",
                    "Failed to load employees with projects",
                    e
            );
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while loading data",
                    e.getMessage()
            );
        }
        return employees;
    }
}