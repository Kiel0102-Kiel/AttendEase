package com.cebeastern.attendease.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String studentNumber;
    private String course;
    private String yearLevel;
    private String contact;
    private List<Attendance> attendanceHistory;

    public Student(String userId, String name, String username, String password,
                  String studentNumber, String course, String yearLevel, String contact) {
        super(userId, name, username, password);
        this.studentNumber = studentNumber;
        this.course = course;
        this.yearLevel = yearLevel;
        this.contact = contact;
        this.attendanceHistory = new ArrayList<>();
    }

    public void addAttendance(Attendance attendance) {
        attendanceHistory.add(attendance);
    }

    public List<Attendance> viewAttendance() {
        return attendanceHistory;
    }

    public String viewAttendanceSummary() {
        int present = 0;
        int absent = 0;
        int late = 0;

        for (Attendance attendance : attendanceHistory) {
            if (attendance.getStatus().equalsIgnoreCase("Present")) {
                present++;
            } else if (attendance.getStatus().equalsIgnoreCase("Absent")) {
                absent++;
            } else if (attendance.getStatus().equalsIgnoreCase("Late")) {
                late++;
            }
        }

        return "Present: " + present + " | Absent: " + absent + " | Late: " + late;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Student Dashboard for " + name);
        System.out.println("Student No.: " + studentNumber);
        System.out.println("Course: " + course + " | Year: " + yearLevel);
        System.out.println(viewAttendanceSummary());
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Attendance> getAttendanceHistory() {
        return attendanceHistory;
    }
}
