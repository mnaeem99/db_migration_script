package com.merhein.grumpeat.script.driverManager;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection getConnection(String databaseName, String databaseUser, String databasePassword) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+databaseName, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;
    }
}
