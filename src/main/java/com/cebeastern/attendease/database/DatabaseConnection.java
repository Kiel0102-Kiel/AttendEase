package com.cebeastern.attendease.database;

import java.sql.Connection;

public class DatabaseConnection {
    private Connection connection;

    public Connection connect() {
        System.out.println("Simple offline mode: database is not required for this version.");
        return connection;
    }

    public void disconnect() {
        connection = null;
    }

    public Connection getConnection() {
        return connection;
    }
}
