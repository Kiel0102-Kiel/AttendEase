package com.cebeastern.attendease;

import com.cebeastern.attendease.ui.AttendEaseApp;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AttendEaseApp().setVisible(true);
        });
    }
}
