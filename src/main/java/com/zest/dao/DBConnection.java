package com.zest.dao;

// Necessary JDBC imports for database connectivity
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Manager using Singleton Pattern
 * Ensures only one database connection instance exists throughout the application
 */
public class DBConnection {
    // Singleton Pattern Applied Here
    private static DBConnection instance;
    private Connection connection;
    
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/zest_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Admin@1179";
    
    /**
     * Private constructor to prevent instantiation from outside
     * Singleton Pattern: Prevents multiple instances
     */
    private DBConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Print stack trace but do not crash the application
            System.err.println("Failed to establish database connection:");
            e.printStackTrace();
        }
    }
    
    /**
     * Singleton Pattern: Static method to get the single instance
     * @return The single instance of DBConnection
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get the database connection
     * @return Connection object, or null if connection failed
     */
    public Connection getConnection() {
        try {
            // Check if connection is still valid
            if (connection == null || connection.isClosed()) {
                // Reconnect if connection was lost
                this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Failed to reconnect to database:");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Close the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection:");
            e.printStackTrace();
        }
    }
}
