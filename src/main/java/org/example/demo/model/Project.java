package org.example.demo.model;

public class Project {
    private int projectId;
    private String projectName,description;
    private String timeEstimation;

    public Project(int id, String projectName, String description, String timeEstimation) {
        this.projectId = id;
        this.projectName = projectName;
        this.description = description;
        this.timeEstimation = timeEstimation;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeEstimation(String timeEstimation) {
        this.timeEstimation = timeEstimation;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeEstimation() {
        return timeEstimation;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", timeEstimation='" + timeEstimation + '\'' +
                '}';
    }
}
