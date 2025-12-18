package org.example.demo.DAO;

import org.example.demo.model.Employee;
import org.example.demo.model.EmployeeWithProjects;
import org.example.demo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    public static boolean addEmployee(String name, String email, String position) {
        String sql ="INSERT INTO EMPLOYEE (name, email, position) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, position);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateEmployee(int id ,String name, String email, String position) {

        String sql = "update employee set name=?,email=?,position=? where emp_id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, position);
            ps.setInt(4,id);
            int result = ps.executeUpdate();
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteEmployee(int id) {
        String sql = "delete from employee where emp_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1,id);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Employee getEmployeeById(int empId){
        Employee employee = null;
        String sql="select * from employee where emp_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1,empId);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                employee = new Employee(result.getInt("emp_id"),result.getString("name"),result.getString("email"),result.getString("position"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }
    public static List<EmployeeWithProjects> getEmployeeWithProjects(){
        List<EmployeeWithProjects> employees = new ArrayList<>();
        String sql="select * from employee_with_project";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ResultSet result = ps.executeQuery();
            while (result.next()){
                employees.add(new EmployeeWithProjects(result.getInt("Emp_ID"),result.getString("Name"),result.getString("Email"),result.getString("Position"),result.getInt("Pro_ID"),result.getString("Project Name"),result.getString("Start Date")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }
}
