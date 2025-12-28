package com.zest.dao;

// Necessary JDBC imports for database connectivity
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

/**
 * Database Connection Manager using Singleton Pattern
 * Ensures only one database connection instance exists throughout the application
 * Uses SQLite - database file is auto-created if it doesn't exist
 */
public class DBConnection {
    // Singleton Pattern Applied Here
    private static DBConnection instance;
    private Connection connection;
    private static boolean initialized = false;
    
    // SQLite database file path (in project root)
    private static final String DB_FILE = "zest_db.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;
    
    /**
     * Private constructor to prevent instantiation from outside
     * Singleton Pattern: Prevents multiple instances
     */
    private DBConnection() {
        try {
            // SQLite driver is automatically loaded, no need for Class.forName
            // SQLite will create the database file if it doesn't exist
            this.connection = DriverManager.getConnection(DB_URL);
            
            // Enable foreign keys (SQLite requires this to be enabled)
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            
            // Initialize database schema and seed data if needed
            if (!initialized) {
                initializeDatabase();
                initialized = true;
            }
            
            System.out.println("SQLite database connected: " + DB_FILE);
        } catch (SQLException e) {
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
     * Initialize database schema and seed data
     * Creates tables if they don't exist and inserts seed data
     */
    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Check if tables already exist
            boolean tablesExist = false;
            try (var rs = stmt.executeQuery("SELECT 1 FROM users LIMIT 1")) {
                // If query succeeds, table exists
                tablesExist = true;
            } catch (SQLException e) {
                // Tables don't exist, we'll create them
                tablesExist = false;
            }
            
            if (!tablesExist) {
                System.out.println("Initializing database schema and seed data...");
                
                // Create tables
                createTables(stmt);
                
                // Insert seed data
                insertSeedData(stmt);
                
                System.out.println("Database initialized successfully!");
            } else {
                System.out.println("Database already exists, skipping initialization.");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database:");
            e.printStackTrace();
        }
    }
    
    /**
     * Create all database tables
     */
    private void createTables(Statement stmt) throws SQLException {
        // TABLE: USERS
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT DEFAULT 'CUSTOMER' CHECK(role IN ('CUSTOMER', 'MERCHANT')),
                loyalty_points INTEGER DEFAULT 0
            )
        """);
        
        // TABLE: RESTAURANTS
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS restaurants (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                image_url TEXT DEFAULT 'default_rest.png'
            )
        """);
        
        // TABLE: MENU_ITEMS
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS menu_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                restaurant_id INTEGER,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                description TEXT,
                image_url TEXT DEFAULT 'default_item.png',
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
            )
        """);
        
        // TABLE: ORDERS
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS orders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                total_price REAL DEFAULT 0.0,
                status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'PREPARING', 'READY', 'DELIVERED')),
                order_date TEXT DEFAULT CURRENT_TIMESTAMP,
                group_code TEXT DEFAULT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """);
        
        // TABLE: ORDER_ITEMS (The Snapshot Pattern)
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS order_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                order_id INTEGER,
                menu_item_name TEXT,
                price_at_purchase REAL,
                quantity INTEGER DEFAULT 1,
                FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
            )
        """);
    }
    
    /**
     * Insert seed data into the database
     */
    private void insertSeedData(Statement stmt) throws SQLException {
        // Users (Password is '123' for everyone for simplicity)
        stmt.execute("""
            INSERT INTO users (name, email, password, role) VALUES
            ('Ahmed Admin', 'ahmed@zest.com', '123', 'MERCHANT'),
            ('Ziad Customer', 'ziad@gmail.com', '123', 'CUSTOMER'),
            ('Hamdy Customer', 'hamdy@gmail.com', '123', 'CUSTOMER')
        """);
        
        // Restaurants
        stmt.execute("""
            INSERT INTO restaurants (name, image_url) VALUES
            ('Burger King', 'bk.png'),
            ('Pizza Hut', 'pizza.png'),
            ('KFC', 'kfc.png')
        """);
        
        // Menu Items for Burger King (ID: 1)
        stmt.execute("""
            INSERT INTO menu_items (restaurant_id, name, price, description, image_url) VALUES
            (1, 'Whopper', 85.00, 'Flame-grilled beef patty', 'whopper.png'),
            (1, 'Chicken Royale', 75.00, 'Crispy chicken sandwich', 'chicken_royale.png'),
            (1, 'Fries (Medium)', 30.00, 'Golden crispy fries', 'fries.png')
        """);
        
        // Menu Items for Pizza Hut (ID: 2)
        stmt.execute("""
            INSERT INTO menu_items (restaurant_id, name, price, description, image_url) VALUES
            (2, 'Super Supreme (M)', 150.00, 'Loaded with toppings', 'supreme.png'),
            (2, 'Pepperoni (M)', 130.00, 'Classic pepperoni pizza', 'pepperoni.png')
        """);
        
        // Menu Items for KFC (ID: 3)
        stmt.execute("""
            INSERT INTO menu_items (restaurant_id, name, price, description, image_url) VALUES
            (3, 'Mighty Bucket', 200.00, '2 Pieces chicken + rice', 'bucket.png'),
            (3, 'Rizo', 60.00, 'Rice with spicy sauce', 'rizo.png')
        """);
        
        // Dummy Order (To test History Screen)
        // Ziad (User ID 2) bought a Whopper
        stmt.execute("""
            INSERT INTO orders (user_id, total_price, status) VALUES (2, 85.00, 'DELIVERED')
        """);
        stmt.execute("""
            INSERT INTO order_items (order_id, menu_item_name, price_at_purchase, quantity) 
            VALUES (1, 'Whopper', 85.00, 1)
        """);
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
                this.connection = DriverManager.getConnection(DB_URL);
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
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
    
    /**
     * Get the database file path
     * @return Path to the SQLite database file
     */
    public static String getDatabasePath() {
        return new File(DB_FILE).getAbsolutePath();
    }
}
