package org.example.demo.model;

public class Employee {
    private int employeeId;
    private String name,email,position;

    public Employee(int employeeId, String name, String email, String position) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.position = position;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
