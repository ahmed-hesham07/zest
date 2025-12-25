package com.zest.dao;

import com.zest.model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Centralized Data Service for all SQL operations
 * This is the only place where SQL queries should be executed
 */
public class DataService {
    private DBConnection dbConnection;
    
    public DataService() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    /**
     * Authenticate user by checking email and password in the users table
     * @param email User's email address
     * @param password User's password
     * @return true if credentials are valid, false otherwise
     */
    public boolean login(String email, String password) {
        Connection conn = dbConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is not available");
            return false;
        }
        
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            boolean found = rs.next();
            rs.close();
            
            return found;
        } catch (SQLException e) {
            System.err.println("Error during login query:");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Save an order to the orders table
     * @param order The Order object to be saved
     */
    public void saveOrder(Order order) {
        Connection conn = dbConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is not available");
            return;
        }
        
        String sql = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setDouble(2, order.getTotalPrice());
            pstmt.setString(3, order.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order saved successfully");
            } else {
                System.err.println("Failed to save order");
            }
        } catch (SQLException e) {
            System.err.println("Error saving order to database:");
            e.printStackTrace();
        }
    }
}
