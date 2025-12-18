package org.example.demo.model;

public class EmployeeWithProjects extends Employee{
    private int projectId;
    private String projectName,startDate;
    public EmployeeWithProjects(int employeeId, String name, String email, String position, int projectId, String projectName, String startDate) {
        super(employeeId, name, email, position);
        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return super.toString()+"{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
