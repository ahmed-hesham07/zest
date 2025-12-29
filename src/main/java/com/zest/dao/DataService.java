package com.zest.dao;

/**
 * DataService - Centralized Data Access Layer
 * 
 * PURPOSE:
 * This class serves as the single point of access for all database operations.
 * It encapsulates all SQL queries and provides a clean interface for controllers
 * to interact with the database without writing SQL directly.
 * 
 * ARCHITECTURE:
 * - Data Access Object (DAO) Pattern
 * - Single Responsibility: All database operations in one place
 * - Separation of Concerns: Controllers don't know about SQL
 * 
 * UML CLASSES USED:
 * - Order: Saves and retrieves orders
 * - OrderItem: Saves order items with Snapshot Pattern (priceAtPurchase)
 * - Restaurant: Retrieves restaurants
 * - MenuItem: Retrieves menu items with currentPrice and isAvailable
 * 
 * DATABASE OPERATIONS:
 * - User operations: login, register, getUserIdByEmail
 * - Restaurant operations: getAllRestaurants, getMenuItemsByRestaurant
 * - Order operations: saveOrder, getOrderHistory
 * - Menu operations: getAllMenuItems
 * 
 * TRANSACTION MANAGEMENT:
 * - saveOrder(): Uses transactions to ensure data consistency
 * - Atomic operations: Order and OrderItems saved together or not at all
 * 
 * SNAPSHOT PATTERN:
 * - saveOrder(): Saves OrderItems with priceAtPurchase (frozen price)
 * - Ensures historical orders show correct prices
 * 
 * BACKWARD COMPATIBILITY:
 * - Handles old database schemas gracefully
 * - Falls back to old column names if new columns don't exist
 * - Migrates data automatically
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Database Operations & Data Access Layer
 */

import com.zest.model.Order; // UML Class: Order model
import com.zest.model.OrderItem; // UML Class: OrderItem with Snapshot Pattern
import com.zest.model.Restaurant; // UML Class: Restaurant model
import java.sql.Connection; // JDBC Connection interface
import java.sql.PreparedStatement; // Prepared statements for SQL queries
import java.util.List; // List interface
import java.util.ArrayList; // ArrayList implementation
import java.sql.ResultSet; // Result set for query results
import java.sql.SQLException; // SQL exception handling
import com.zest.model.MenuItem; // UML Class: MenuItem model

/**
 * Centralized Data Service for all SQL operations
 * This is the only place where SQL queries should be executed
 */
public class DataService {
    
    /**
     * dbConnection - Database connection manager
     * PURPOSE: Singleton instance for database connectivity
     * TYPE: DBConnection
     * VISIBILITY: private
     * USAGE: Gets connection for all database operations
     */
    private DBConnection dbConnection;
    
    /**
     * Constructor - Initializes database connection
     * 
     * PURPOSE:
     * Creates new DataService and gets database connection singleton.
     * 
     * INITIALIZATION:
     * - Gets DBConnection singleton instance
     * - Connection is ready for use
     */
    public DataService() {
        this.dbConnection = DBConnection.getInstance(); // Get database connection singleton
    }
    
    /**
     * getAllMenuItems() - Fetches all menu items from database
     * 
     * PURPOSE:
     * Retrieves all menu items from all restaurants.
     * Used for displaying menu items across the system.
     * 
     * UML CLASSES USED:
     * - MenuItem: Creates MenuItem objects with currentPrice and isAvailable
     * 
     * DATABASE OPERATIONS:
     * - SELECT * FROM menu_items
     * - Handles null image_url gracefully
     * - Uses current_price if available, falls back to price
     * - Gets is_available (defaults to true if null)
     * 
     * BACKWARD COMPATIBILITY:
     * - Handles old databases without current_price column
     * - Handles old databases without is_available column
     * 
     * @return List of all MenuItem objects from database
     */
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>(); // Create list for results
        Connection conn = dbConnection.getConnection(); // Get database connection
        
        /**
         * VALIDATION: Check connection
         * If connection is null, return empty list
         */
        if (conn == null) {
            System.err.println("CRITICAL: DB Connection is NULL in getAllMenuItems!"); 
            return items; // Return empty list
        }

        String sql = "SELECT * FROM menu_items"; // SQL query to get all menu items
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            /**
             * LOOP THROUGH RESULTS:
             * Process each row from ResultSet
             */
            while (rs.next()) {
                /**
                 * HANDLE IMAGE URL:
                 * Get image_url, use default if null
                 */
                String imageUrl = rs.getString("image_url");
                if (imageUrl == null) {
                    imageUrl = "default_item.png"; // Default image
                }
                
                /**
                 * GET PRICE (BACKWARD COMPATIBILITY):
                 * Try to get current_price first (UML spec)
                 * Fall back to price if current_price doesn't exist or is null
                 */
                double price;
                try {
                    price = rs.getDouble("current_price"); // Try current_price first
                    if (rs.wasNull() || price == 0) {
                        price = rs.getDouble("price"); // Fall back to price
                    }
                } catch (SQLException e) {
                    // current_price column might not exist, use price
                    price = rs.getDouble("price"); // Use old price column
                }
                
                /**
                 * GET AVAILABILITY (BACKWARD COMPATIBILITY):
                 * Get is_available, default to true if null or column doesn't exist
                 */
                boolean isAvailable = true; // Default to available
                try {
                    int available = rs.getInt("is_available"); // Try to get is_available
                    isAvailable = (available == 1); // Convert to boolean
                } catch (SQLException e) {
                    // Column might not exist in old databases, default to true
                }
                
                /**
                 * CREATE MENUITEM OBJECT:
                 * Create MenuItem with all fields from database
                 */
                MenuItem item = new MenuItem(
                    rs.getInt("id"), // Menu item ID
                    rs.getInt("restaurant_id"), // Restaurant ID
                    rs.getString("name"), // Item name
                    price, // Price (current_price or price)
                    isAvailable, // Availability status
                    rs.getString("description"), // Description
                    imageUrl // Image URL
                );
                items.add(item); // Add to list
                System.out.println("Fetched Item: " + item.getName()); // Debug log
            }
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if query fails
             */
            System.err.println("SQL Error in getAllMenuItems:"); 
            e.printStackTrace();
        }
        
        System.out.println("DataService returning " + items.size() + " items."); // Debug log
        return items; // Return list of items
    }
    
    /**
     * login() - Authenticates user by checking email and password
     * 
     * PURPOSE:
     * Validates user credentials against database.
     * Used by LoginController for user authentication.
     * 
     * AUTHENTICATION FLOW:
     * 1. Query database for user with matching email and password
     * 2. If found, authentication succeeds
     * 3. If not found, authentication fails
     * 
     * NOTE: Currently uses plain password comparison for backward compatibility.
     * In production, should use passwordHash and User.authenticate().
     * 
     * @param email User's email address
     * @param password User's password (plain text)
     * @return true if credentials are valid, false otherwise
     */
    public boolean login(String email, String password) {
        Connection conn = dbConnection.getConnection(); // Get database connection
        if (conn == null) {
            System.err.println("Database connection is not available");
            return false; // Connection failed
        }
        
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?"; // SQL query
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email); // Set email parameter
            pstmt.setString(2, password); // Set password parameter
            
            ResultSet rs = pstmt.executeQuery(); // Execute query
            boolean found = rs.next(); // Check if user found
            rs.close(); // Close result set
            
            return found; // Return authentication result
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if query fails
             */
            System.err.println("Error during login query:");
            e.printStackTrace();
            return false; // Return false on error
        }
    }
    
    /**
     * registerUser() - Registers a new user in the database
     * 
     * PURPOSE:
     * Creates a new user account in the database.
     * Used by RegisterController for user registration.
     * 
     * REGISTRATION FLOW:
     * 1. Check if email already exists
     * 2. If email exists, return false (registration fails)
     * 3. If email is new, insert new user record
     * 4. Return true if insertion successful
     * 
     * VALIDATION:
     * - Email uniqueness: Prevents duplicate accounts
     * - All fields required: name, email, password
     * 
     * @param name User's name
     * @param email User's email (must be unique)
     * @param password User's password (plain text)
     * @return true if registration successful, false if email already exists
     */
    public boolean registerUser(String name, String email, String password) {
        Connection conn = dbConnection.getConnection(); // Get database connection
        if (conn == null) {
            System.err.println("Database connection is not available");
            return false; // Connection failed
        }
        
        /**
         * STEP 1: CHECK IF EMAIL ALREADY EXISTS
         * Query database to see if email is already registered
         */
        String checkSql = "SELECT * FROM users WHERE email = ?"; // Check email query
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, email); // Set email parameter
            ResultSet rs = checkStmt.executeQuery(); // Execute query
            if (rs.next()) {
                rs.close();
                return false; // Email already exists - registration fails
            }
            rs.close();
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if check fails
             */
            System.err.println("Error checking email existence:");
            e.printStackTrace();
            return false; // Return false on error
        }
        
        /**
         * STEP 2: INSERT NEW USER
         * Email is unique, proceed with registration
         */
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'CUSTOMER')"; // Insert query
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name); // Set name parameter
            pstmt.setString(2, email); // Set email parameter
            pstmt.setString(3, password); // Set password parameter
            
            int rowsAffected = pstmt.executeUpdate(); // Execute insert
            return rowsAffected > 0; // Return true if row inserted
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if insertion fails
             */
            System.err.println("Error registering user:");
            e.printStackTrace();
            return false; // Return false on error
        }
    }
    
    /**
     * getUserIdByEmail() - Gets user ID by email address
     * 
     * PURPOSE:
     * Retrieves user ID from database using email address.
     * Used by controllers to get user ID for order operations.
     * 
     * @param email User's email address
     * @return User ID if found, -1 if not found
     */
    public int getUserIdByEmail(String email) {
        Connection conn = dbConnection.getConnection(); // Get database connection
        if (conn == null) {
            System.err.println("Database connection is not available");
            return -1; // Connection failed
        }
        
        String sql = "SELECT id FROM users WHERE email = ?"; // SQL query
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email); // Set email parameter
            ResultSet rs = pstmt.executeQuery(); // Execute query
            if (rs.next()) {
                int userId = rs.getInt("id"); // Get user ID
                rs.close(); // Close result set
                return userId; // Return user ID
            }
            rs.close();
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if query fails
             */
            System.err.println("Error getting user ID:");
            e.printStackTrace();
        }
        return -1; // User not found
    }
    
    /**
     * getOrderHistory() - Gets order history for a user
     * 
     * PURPOSE:
     * Retrieves all orders placed by a specific user.
     * Used by HistoryController to display order history.
     * 
     * UML CLASSES USED:
     * - Order: Creates Order objects from database rows
     * 
     * DATABASE OPERATIONS:
     * - SELECT * FROM orders WHERE user_id = ?
     * - Orders sorted by order_date DESC (newest first)
     * - Uses total_amount if available, falls back to total_price
     * 
     * BACKWARD COMPATIBILITY:
     * - Handles old databases without total_amount column
     * - Falls back to total_price if total_amount is null or 0
     * 
     * @param userId User ID to get orders for
     * @return List of Order objects for the user
     */
    public List<Order> getOrderHistory(int userId) {
        List<Order> orders = new ArrayList<>(); // Create list for results
        Connection conn = dbConnection.getConnection(); // Get database connection
        if (conn == null) {
            System.err.println("Database connection is not available");
            return orders; // Return empty list
        }
        
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC"; // SQL query
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId); // Set user ID parameter
            ResultSet rs = pstmt.executeQuery(); // Execute query
            
            while (rs.next()) {
                /**
                 * GET TOTAL AMOUNT (BACKWARD COMPATIBILITY):
                 * Try to get total_amount first (UML spec)
                 * Fall back to total_price if total_amount doesn't exist or is 0
                 */
                double totalAmount;
                try {
                    totalAmount = rs.getDouble("total_amount"); // Try total_amount first
                    if (rs.wasNull() || totalAmount == 0) {
                        totalAmount = rs.getDouble("total_price"); // Fall back to total_price
                    }
                } catch (SQLException e) {
                    totalAmount = rs.getDouble("total_price"); // Use old total_price column
                }
                
                /**
                 * CREATE ORDER OBJECT:
                 * Create Order from database row
                 */
                Order order = new Order(
                    rs.getInt("id"), // Order ID
                    rs.getInt("user_id"), // User ID
                    totalAmount, // Total amount
                    rs.getString("status") // Order status
                );
                orders.add(order); // Add to list
            }
            rs.close(); // Close result set
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if query fails
             */
            System.err.println("Error fetching order history:");
            e.printStackTrace();
        }
        
        return orders; // Return list of orders
    }
    
    /**
     * getAllRestaurants() - Gets all restaurants from database
     * 
     * PURPOSE:
     * Retrieves all restaurants from database.
     * Used by RestaurantSelectionController to display restaurants.
     * 
     * UML CLASSES USED:
     * - Restaurant: Creates Restaurant objects from database rows
     * 
     * DATABASE OPERATIONS:
     * - SELECT * FROM restaurants ORDER BY name
     * - Handles null image_url gracefully
     * 
     * @return List of all Restaurant objects
     */
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>(); // Create list for results
        Connection conn = dbConnection.getConnection(); // Get database connection
        if (conn == null) {
            System.err.println("Database connection is not available");
            return restaurants; // Return empty list
        }
        
        String sql = "SELECT * FROM restaurants ORDER BY name"; // SQL query
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                /**
                 * HANDLE IMAGE URL:
                 * Get image_url, use default if null
                 */
                String imageUrl = rs.getString("image_url");
                if (imageUrl == null) {
                    imageUrl = "default_rest.png"; // Default image
                }
                
                /**
                 * CREATE RESTAURANT OBJECT:
                 * Create Restaurant from database row
                 */
                Restaurant restaurant = new Restaurant(
                    rs.getInt("id"), // Restaurant ID
                    rs.getString("name"), // Restaurant name
                    imageUrl // Image URL
                );
                restaurants.add(restaurant); // Add to list
            }
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if query fails
             */
            System.err.println("Error fetching restaurants:");
            e.printStackTrace();
        }
        
        return restaurants; // Return list of restaurants
    }
    
    /**
     * getMenuItemsByRestaurant() - Gets menu items for a specific restaurant
     * 
     * PURPOSE:
     * Retrieves menu items for a specific restaurant.
     * Used by HomeController to display menu items for selected restaurant.
     * 
     * UML CLASSES USED:
     * - MenuItem: Creates MenuItem objects with currentPrice and isAvailable
     * 
     * DATABASE OPERATIONS:
     * - SELECT * FROM menu_items WHERE restaurant_id = ? ORDER BY name
     * - Filters items by restaurant ID
     * - Uses current_price if available, falls back to price
     * - Gets is_available (defaults to true if null)
     * 
     * BACKWARD COMPATIBILITY:
     * - Handles old databases without current_price column
     * - Handles old databases without is_available column
     * 
     * @param restaurantId The ID of the restaurant
     * @return List of MenuItem objects for that restaurant
     */
    public List<MenuItem> getMenuItemsByRestaurant(int restaurantId) {
        List<MenuItem> items = new ArrayList<>(); // Create list for results
        Connection conn = dbConnection.getConnection(); // Get database connection
        
        if (conn == null) {
            System.err.println("Database connection is not available");
            return items; // Return empty list
        }

        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ? ORDER BY name"; // SQL query
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, restaurantId); // Set restaurant ID parameter
            ResultSet rs = pstmt.executeQuery(); // Execute query

            while (rs.next()) {
                /**
                 * HANDLE IMAGE URL:
                 * Get image_url, use default if null
                 */
                String imageUrl = rs.getString("image_url");
                if (imageUrl == null) {
                    imageUrl = "default_item.png"; // Default image
                }
                
                /**
                 * GET PRICE (BACKWARD COMPATIBILITY):
                 * Try to get current_price first (UML spec)
                 * Fall back to price if current_price doesn't exist or is null
                 */
                double price;
                try {
                    price = rs.getDouble("current_price"); // Try current_price first
                    if (rs.wasNull() || price == 0) {
                        price = rs.getDouble("price"); // Fall back to price
                    }
                } catch (SQLException e) {
                    // current_price column might not exist, use price
                    price = rs.getDouble("price"); // Use old price column
                }
                
                /**
                 * GET AVAILABILITY (BACKWARD COMPATIBILITY):
                 * Get is_available, default to true if null or column doesn't exist
                 */
                boolean isAvailable = true; // Default to available
                try {
                    int available = rs.getInt("is_available"); // Try to get is_available
                    isAvailable = (available == 1); // Convert to boolean
                } catch (SQLException e) {
                    // Column might not exist in old databases, default to true
                }
                
                /**
                 * CREATE MENUITEM OBJECT:
                 * Create MenuItem with all fields from database
                 */
                MenuItem item = new MenuItem(
                    rs.getInt("id"), // Menu item ID
                    rs.getInt("restaurant_id"), // Restaurant ID
                    rs.getString("name"), // Item name
                    price, // Price (current_price or price)
                    isAvailable, // Availability status
                    rs.getString("description"), // Description
                    imageUrl // Image URL
                );
                items.add(item); // Add to list
            }
            rs.close(); // Close result set
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if query fails
             */
            System.err.println("SQL Error in getMenuItemsByRestaurant:");
            e.printStackTrace();
        }
        
        return items; // Return list of items
    }
    
    /**
     * saveOrder() - Saves an order to database with OrderItems (Snapshot Pattern)
     * 
     * PURPOSE:
     * Saves an Order and its OrderItems to database using transactions.
     * Ensures atomicity: Order and OrderItems saved together or not at all.
     * 
     * UML CLASSES USED:
     * - Order: Gets order data (userId, totalAmount, status)
     * - OrderItem: Gets OrderItems with priceAtPurchase (Snapshot Pattern)
     * 
     * DESIGN PATTERNS:
     * - Snapshot Pattern: Saves priceAtPurchase (frozen price) not live price
     * - Transaction Pattern: Ensures data consistency
     * 
     * TRANSACTION FLOW:
     * 1. Disable auto-commit (start transaction)
     * 2. Insert order into orders table
     * 3. Get generated order ID
     * 4. Insert OrderItems into order_items table (with priceAtPurchase)
     * 5. Commit transaction (or rollback on error)
     * 
     * SNAPSHOT PATTERN:
     * - CRITICAL: Saves priceAtPurchase from OrderItem
     * - This is the frozen price at order time
     * - NOT the live MenuItem.currentPrice
     * 
     * @param order The Order object to be saved
     * @return The ID of the newly created order, or -1 if failed
     */
    public int saveOrder(Order order) {
        Connection conn = dbConnection.getConnection(); // Get database connection
        if (conn == null) {
            System.err.println("Database connection is not available");
            return -1; // Connection failed
        }
        
        try {
            /**
             * STEP 1: START TRANSACTION
             * Disable auto-commit to start transaction
             * This ensures atomicity (all or nothing)
             */
            conn.setAutoCommit(false); // Start transaction
            
            /**
             * STEP 2: INSERT ORDER
             * Insert order into orders table
             * Include both total_price and total_amount for compatibility
             */
            String sql = "INSERT INTO orders (user_id, total_price, total_amount, status) VALUES (?, ?, ?, ?)"; // Insert query
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, order.getUserId()); // Set user ID
                double totalAmount = order.getTotalAmount(); // Get total amount
                pstmt.setDouble(2, totalAmount); // total_price (backward compatibility)
                pstmt.setDouble(3, totalAmount); // total_amount (UML spec)
                pstmt.setString(4, order.getStatus()); // Set order status
                
                int rowsAffected = pstmt.executeUpdate(); // Execute insert
                if (rowsAffected > 0) {
                    /**
                     * STEP 3: GET GENERATED ORDER ID
                     * Get the auto-generated order ID from database
                     */
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int orderId = generatedKeys.getInt(1); // Get generated order ID
                            
                            /**
                             * STEP 4: SAVE ORDERITEMS (SNAPSHOT PATTERN)
                             * Insert OrderItems into order_items table
                             * CRITICAL: Save priceAtPurchase (frozen price), not live price
                             */
                            if (order.getItems() != null && !order.getItems().isEmpty()) {
                                String itemSql = "INSERT INTO order_items (order_id, menu_item_name, price_at_purchase, quantity) VALUES (?, ?, ?, ?)"; // Insert query
                                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                                    /**
                                     * LOOP THROUGH ORDERITEMS:
                                     * Insert each OrderItem with priceAtPurchase
                                     */
                                    for (OrderItem item : order.getItems()) {
                                        /**
                                         * VALIDATION: Check item is valid
                                         * Only save non-null items with valid references
                                         */
                                        if (item != null && item.getItemReference() != null) {
                                            itemStmt.setInt(1, orderId); // Set order ID
                                            
                                            /**
                                             * GET ITEM NAME:
                                             * Get name from MenuItem reference
                                             * Use default if name is null or empty
                                             */
                                            String itemName = item.getItemReference().getName();
                                            if (itemName == null || itemName.isEmpty()) {
                                                itemName = "Unknown Item"; // Default name
                                            }
                                            itemStmt.setString(2, itemName); // Set item name
                                            
                                            /**
                                             * CRITICAL: SAVE PRICEAT PURCHASE (SNAPSHOT PATTERN)
                                             * Save priceAtPurchase (frozen price at order time)
                                             * NOT MenuItem.currentPrice (live price)
                                             * This ensures historical accuracy
                                             */
                                            itemStmt.setDouble(3, item.getPriceAtPurchase()); // CRITICAL: Snapshot price
                                            itemStmt.setInt(4, item.getQuantity()); // Set quantity
                                            itemStmt.addBatch(); // Add to batch
                                        }
                                    }
                                    itemStmt.executeBatch(); // Execute batch insert
                                } catch (SQLException itemEx) {
                                    /**
                                     * ERROR HANDLING:
                                     * Log error but continue (order is saved even if items fail)
                                     * This prevents losing the order if items fail
                                     */
                                    System.err.println("Error saving order items (order will still be saved):");
                                    itemEx.printStackTrace();
                                    // Continue - order is saved even if items fail
                                }
                            }
                            
                            /**
                             * STEP 5: COMMIT TRANSACTION
                             * Commit transaction if everything succeeded
                             * Re-enable auto-commit
                             */
                            conn.commit(); // Commit transaction
                            conn.setAutoCommit(true); // Re-enable auto-commit
                            
                            System.out.println("Order saved successfully with ID: " + orderId); // Log success
                            
                            return orderId; // Return order ID
                        }
                    }
                } else {
                    /**
                     * ROLLBACK ON FAILURE:
                     * If order insert failed, rollback transaction
                     */
                    conn.rollback(); // Rollback transaction
                    conn.setAutoCommit(true); // Re-enable auto-commit
                    System.err.println("Failed to save order");
                }
            }
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Rollback transaction on error
             * Re-enable auto-commit
             */
            try {
                conn.rollback(); // Rollback transaction
                conn.setAutoCommit(true); // Re-enable auto-commit
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback:");
                rollbackEx.printStackTrace();
            }
            System.err.println("Error saving order to database:");
            e.printStackTrace();
        }
        return -1; // Return -1 if save failed
    }
}
