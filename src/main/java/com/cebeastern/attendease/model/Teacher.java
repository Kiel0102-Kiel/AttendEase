package com.cebeastern.attendease.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private String employeeId;
    private String department;
    private String position;
    private List<ClassRecord> managedClasses;

    public Teacher(String userId, String name, String username, String password,
                  String employeeId, String department, String position) {
        super(userId, name, username, password);
        this.employeeId = employeeId;
        this.department = department;
        this.position = position;
        this.managedClasses = new ArrayList<>();
    }

    public void addClass(ClassRecord classRecord) {
        managedClasses.add(classRecord);
    }

    public void recordAttendance(Student student, ClassRecord classRecord, String attendanceDate,
                                String status, String remarks) {
        Attendance attendance = new Attendance(
                "ATT" + System.currentTimeMillis(),
                student.getStudentNumber(),
                classRecord.getClassId(),
                attendanceDate,
                status,
                remarks
        );
        student.addAttendance(attendance);
        classRecord.addAttendance(attendance);
    }

    public void updateAttendance(Attendance attendance, String newStatus, String newRemarks) {
        attendance.updateStatus(newStatus, newRemarks);
    }

    public List<Attendance> viewClassAttendance(ClassRecord classRecord) {
        return classRecord.getAttendanceRecords();
    }

    @Override
    public void displayDashboard() {
        System.out.println("Teacher Dashboard for " + name);
        System.out.println("Employee ID: " + employeeId + " | Department: " + department);
        System.out.println("Classes managed: " + managedClasses.size());
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<ClassRecord> getManagedClasses() {
        return managedClasses;
    }
}
