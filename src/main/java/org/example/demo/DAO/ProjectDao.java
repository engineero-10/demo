package org.example.demo.DAO;

import org.example.demo.model.Employee;
import org.example.demo.model.EmployeeWithProjects;
import org.example.demo.model.Project;
import org.example.demo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    public static boolean addProject(String pro_name, String description, String time_estimation) {
        String sql ="insert into project (pro_name,description,time_estimation) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pro_name);
            ps.setString(2, description);
            ps.setString(3, time_estimation);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static boolean updateProject(int pro_id ,String pro_name, String description, String time_estimation) {

        String sql = "update project set pro_name=?,description=?,time_estimation=? where pro_id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setString(1, pro_name);
            ps.setString(2, description);
            ps.setString(3, time_estimation);
            ps.setInt(4,pro_id);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean deleteProject(int id) {
        String sql = "delete from project where pro_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1,id);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Project getProjectById(int proId){
        Project project = null;
        String sql="select * from project where pro_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1,proId);
            ResultSet result = ps.executeQuery();
            while (result.next()){
                project = new Project(result.getInt("pro_id"),result.getString("pro_name"),result.getString("description"),result.getString("time_estimation"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return project;
    }
    public static List<Project> getAllProjects(){
        List<Project> projects = new ArrayList<>();
        String sql="select * from project";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {
            ResultSet result = ps.executeQuery();
            while (result.next()){
                projects.add(new Project(result.getInt("pro_id"),result.getString("pro_name"),result.getString("description"),result.getString("time_estimation")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return projects;
    }
}
