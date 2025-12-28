package com.zest.dao;

import com.zest.model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zest.model.MenuItem;

/**
 * Centralized Data Service for all SQL operations
 * This is the only place where SQL queries should be executed
 */
public class DataService {
    private DBConnection dbConnection;
    
    public DataService() {
        this.dbConnection = DBConnection.getInstance();
    }
    
 // --- NEW METHOD: FETCHES MENU FROM DB ---
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        Connection conn = dbConnection.getConnection();
        
        // DEBUG: Check connection
        if (conn == null) {
            System.err.println("CRITICAL: DB Connection is NULL in getAllMenuItems!"); 
            return items;
        }

        String sql = "SELECT * FROM menu_items";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Handle null image_url gracefully
                String imageUrl = rs.getString("image_url");
                if (imageUrl == null) {
                    imageUrl = "default_item.png";
                }
                
                MenuItem item = new MenuItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    imageUrl
                );
                items.add(item);
                // DEBUG: Confirm row fetch
                System.out.println("Fetched Item: " + item.getName()); 
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllMenuItems:"); 
            e.printStackTrace();
        }
        
        // DEBUG: Final count
        System.out.println("DataService returning " + items.size() + " items.");
        return items;
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
     * Register a new user in the database
     * @param name User's name
     * @param email User's email
     * @param password User's password
     * @return true if registration successful, false if email already exists
     */
    public boolean registerUser(String name, String email, String password) {
        Connection conn = dbConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is not available");
            return false;
        }
        
        // Check if email already exists
        String checkSql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                rs.close();
                return false; // Email already exists
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error checking email existence:");
            e.printStackTrace();
            return false;
        }
        
        // Insert new user
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'CUSTOMER')";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user:");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get user ID by email
     * @param email User's email
     * @return User ID or -1 if not found
     */
    public int getUserIdByEmail(String email) {
        Connection conn = dbConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is not available");
            return -1;
        }
        
        String sql = "SELECT id FROM users WHERE email = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                rs.close();
                return userId;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error getting user ID:");
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Get order history for a user
     * @param userId User ID
     * @return List of orders for the user
     */
    public List<Order> getOrderHistory(int userId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = dbConnection.getConnection();
        if (conn == null) {
            System.err.println("Database connection is not available");
            return orders;
        }
        
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getDouble("total_price"),
                    rs.getString("status")
                );
                orders.add(order);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error fetching order history:");
            e.printStackTrace();
        }
        
        return orders;
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
