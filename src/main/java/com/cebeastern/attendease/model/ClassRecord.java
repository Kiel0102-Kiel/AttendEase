package com.cebeastern.attendease.model;

import java.util.ArrayList;
import java.util.List;

public class ClassRecord {
    private String classId;
    private String subjectCode;
    private String subjectName;
    private String section;
    private String schedule;
    private List<Attendance> attendanceRecords;

    public ClassRecord(String classId, String subjectCode, String subjectName,
                      String section, String schedule) {
        this.classId = classId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.section = section;
        this.schedule = schedule;
        this.attendanceRecords = new ArrayList<>();
    }

    public void createClass() {
        System.out.println("Class created: " + subjectName + " (" + section + ")");
    }

    public void updateClass(String subjectCode, String subjectName, String section, String schedule) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.section = section;
        this.schedule = schedule;
    }

    public void viewClass() {
        System.out.println("Class ID: " + classId);
        System.out.println("Subject: " + subjectCode + " - " + subjectName);
        System.out.println("Section: " + section);
        System.out.println("Schedule: " + schedule);
    }

    public void addAttendance(Attendance attendance) {
        attendanceRecords.add(attendance);
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public List<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }
}
