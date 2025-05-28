package com.project.bd.app.projectbd.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection getConnection(){
        String databaseName = "Project-BD";
        String user = "postgres";
        String password = "";
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded successfully!");
            databaseLink = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return  databaseLink;
    }

    public static Statement getStatement() throws SQLException {
        return  getConnection().createStatement();
    }
}
