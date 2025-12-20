package org.example.demo.DAO;

import org.example.demo.util.DBConnection;

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
            throw new RuntimeException(e.getMessage());
        }
    }
    public static boolean addAdmin(String name, String email, String username, String password) {
        String sql ="INSERT INTO ADMIN (name, email, username, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);
            if (ps.executeUpdate() > 0) {
                connection.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static boolean assignEmployeeInProject(int empId,int proId){
        String sql ="insert into employee_project (project_id,employee_id) values (?,?)";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,proId);
            ps.setInt(2,empId);
            int result = ps.executeUpdate();
            return result>0;
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
