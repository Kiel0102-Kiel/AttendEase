package com.cebeastern.attendease.service;

import com.cebeastern.attendease.model.Attendance;
import com.cebeastern.attendease.model.ClassRecord;
import com.cebeastern.attendease.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceService {
    private final List<Attendance> attendanceRecords;

    public AttendanceService() {
        attendanceRecords = new ArrayList<>();
    }

    public void recordAttendance(Student student, ClassRecord classRecord, String date, String status, String remarks) {
        Attendance attendance = new Attendance(
                "ATT" + System.currentTimeMillis(),
                student.getStudentNumber(),
                classRecord.getClassId(),
                date,
                status,
                remarks
        );
        attendanceRecords.add(attendance);
        student.addAttendance(attendance);
        classRecord.addAttendance(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRecords;
    }

    public Map<String, Integer> getAttendanceSummary(List<Attendance> records) {
        Map<String, Integer> summary = new HashMap<>();
        summary.put("Present", 0);
        summary.put("Absent", 0);
        summary.put("Late", 0);

        for (Attendance record : records) {
            String status = record.getStatus();
            if (summary.containsKey(status)) {
                summary.put(status, summary.get(status) + 1);
            }
        }

        return summary;
    }

    public double calculateAttendancePercentage(List<Attendance> records) {
        if (records.isEmpty()) {
            return 0.0;
        }

        int present = 0;
        for (Attendance record : records) {
            if (record.getStatus().equalsIgnoreCase("Present")) {
                present++;
            }
        }

        return (present * 100.0) / records.size();
    }
}
