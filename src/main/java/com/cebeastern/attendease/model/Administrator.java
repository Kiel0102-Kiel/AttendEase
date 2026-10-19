package com.cebeastern.attendease.model;

public class Administrator extends User {
    public Administrator(String userId, String name, String username, String password) {
        super(userId, name, username, password);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Administrator Dashboard for " + name);
    }
}