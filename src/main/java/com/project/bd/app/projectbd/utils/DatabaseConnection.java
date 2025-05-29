package com.project.bd.app.projectbd.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection databaseLink;

    public static Connection getConnection() {
        if (databaseLink != null) {
            try {
                if (!databaseLink.isClosed()) {
                    return databaseLink;
                }
            } catch (SQLException e) {
                System.out.println("Error checking connection: " + e.getMessage());
            }
        }

        // Kalau belum ada koneksi, buat baru
        String databaseName = "Project-BD";
        String user = "postgres";
        String password = "abcd1234";
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;

        try {
            Class.forName("org.postgresql.Driver");
            databaseLink = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return databaseLink;
    }

    public static Statement getStatement() throws SQLException {
        return  getConnection().createStatement();
    }
}
