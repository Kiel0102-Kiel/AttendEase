package com.cebeastern.attendease.model;

import java.util.ArrayList;
import java.util.List;

public class StudentSearch {
    private String searchKeyword;
    private String searchType;
    private List<Student> students;

    public StudentSearch() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> searchByName(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> searchByStudentNumber(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getStudentNumber().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> searchByCourse(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getCourse().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public List<Student> getStudents() {
        return students;
    }
}
