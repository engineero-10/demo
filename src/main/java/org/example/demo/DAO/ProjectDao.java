package org.example.demo.DAO;

import org.example.demo.model.Project;
import org.example.demo.util.DBConnection;
import org.example.demo.util.ErrorHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    public static boolean addProject(String pro_name, String description, String time_estimation) {
        String sql = "insert into project (pro_name, description, time_estimation) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, pro_name);
            ps.setString(2, description);
            ps.setString(3, time_estimation);

            int result = ps.executeUpdate();
            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Project added successfully!");
                return true;
            }
            return false;

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Add Project Error",
                    "Failed to add project",
                    e
            );
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while adding project",
                    e.getMessage()
            );
            return false;
        }
    }

    public static boolean updateProject(int pro_id, String pro_name, String description, String time_estimation) {
        String sql = "update project set pro_name=?, description=?, time_estimation=? where pro_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pro_name);
            ps.setString(2, description);
            ps.setString(3, time_estimation);
            ps.setInt(4, pro_id);

            int result = ps.executeUpdate();
            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Project updated successfully!");
                return true;
            } else {
                ErrorHandler.showWarning(
                        "Update Warning",
                        "No project found with ID: " + pro_id
                );
                return false;
            }

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Update Project Error",
                    "Failed to update project",
                    e
            );
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while updating project",
                    e.getMessage()
            );
            return false;
        }
    }

    public static boolean deleteProject(int id) {
        String sql = "delete from project where pro_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int result = ps.executeUpdate();

            if (result > 0) {
                ErrorHandler.showSuccess("Success", "Project deleted successfully!");
                return true;
            } else {
                ErrorHandler.showWarning(
                        "Delete Warning",
                        "No project found with ID: " + id
                );
                return false;
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key constraint")) {
                ErrorHandler.showError(
                        "Delete Error",
                        "Cannot delete project",
                        "This project has employees assigned to it. Please remove employee assignments first."
                );
            } else {
                ErrorHandler.showErrorWithException(
                        "Delete Project Error",
                        "Failed to delete project",
                        e
                );
            }
            return false;
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while deleting project",
                    e.getMessage()
            );
            return false;
        }
    }

    public static Project getProjectById(int proId) {
        Project project = null;
        String sql = "select * from project where pro_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proId);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                project = new Project(
                        result.getInt("pro_id"),
                        result.getString("pro_name"),
                        result.getString("description"),
                        result.getString("time_estimation")
                );
            }

            if (project == null) {
                ErrorHandler.showWarning(
                        "Not Found",
                        "No project found with ID: " + proId
                );
            }

        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Fetch Project Error",
                    "Failed to fetch project data",
                    e
            );
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while fetching project",
                    e.getMessage()
            );
        }
        return project;
    }

    public static List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "select * from project";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                projects.add(new Project(
                        result.getInt("pro_id"),
                        result.getString("pro_name"),
                        result.getString("description"),
                        result.getString("time_estimation")
                ));
            }
        } catch (SQLException e) {
            ErrorHandler.showErrorWithException(
                    "Fetch Data Error",
                    "Failed to load projects",
                    e
            );
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while loading projects",
                    e.getMessage()
            );
        }
        return projects;
    }
}