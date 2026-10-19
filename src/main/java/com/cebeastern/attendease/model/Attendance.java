package com.cebeastern.attendease.model;

public class Attendance {
    private String attendanceId;
    private String studentId;
    private String classId;
    private String attendanceDate;
    private String status;
    private String remarks;

    public Attendance(String attendanceId, String studentId, String classId,
                      String attendanceDate, String status, String remarks) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.classId = classId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }

    public void recordAttendance() {
        System.out.println("Attendance recorded for student " + studentId + " on " + attendanceDate + " as " + status);
    }

    public void updateStatus(String newStatus, String newRemarks) {
        this.status = newStatus;
        this.remarks = newRemarks;
    }

    public String checkStatus() {
        return status;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
