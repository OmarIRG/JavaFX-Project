package com.example.social_media.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://ourdatabase.c1igyio6wgx5.ca-central-1.rds.amazonaws.com:3306/social_media_app";
    private static final String USER = "admin";
    private static final String PASSWORD = "worldOFdatabases";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
