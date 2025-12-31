package com.zest.dao;

/**
 * DBConnection - Database Connection Manager Singleton
 * 
 * PURPOSE:
 * This class manages the database connection using the Singleton Pattern.
 * It ensures only one database connection exists throughout the application
 * and handles database initialization, schema creation, and migrations.
 * 
 * DESIGN PATTERNS:
 * - Singleton Pattern: Only one database connection instance exists
 * - Double-Checked Locking: Thread-safe singleton initialization
 * 
 * DATABASE:
 * - SQLite: Lightweight, file-based database
 * - Database file: zest_db.sqlite (created in project root)
 * - Auto-creates database file if it doesn't exist
 * 
 * DATABASE INITIALIZATION:
 * - Creates tables if they don't exist
 * - Inserts seed data for testing
 * - Migrates existing databases to new schema
 * 
 * SCHEMA MANAGEMENT:
 * - createTables(): Creates all database tables
 * - insertSeedData(): Inserts initial test data
 * - migrateDatabase(): Migrates existing databases
 * 
 * FOREIGN KEYS:
 * - Enables foreign key constraints (SQLite requires explicit enablement)
 * - Ensures referential integrity
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Database Architecture & Connection Management
 */

// Necessary JDBC imports for database connectivity
import java.sql.Connection; // JDBC Connection interface
import java.sql.DriverManager; // JDBC DriverManager for creating connections
import java.sql.SQLException; // SQL exception handling
import java.sql.Statement; // SQL Statement for executing queries
import java.io.File; // File operations

/**
 * Database Connection Manager using Singleton Pattern
 * Ensures only one database connection instance exists throughout the application
 */
public class DBConnection {
    
    /**
     * SINGLETON PATTERN FIELDS:
     * These fields implement the Singleton Pattern
     */
    
    /**
     * instance - Single DBConnection instance (Singleton Pattern)
     * PURPOSE: Stores the only instance of DBConnection
     * TYPE: static DBConnection
     * VISIBILITY: private
     * SINGLETON: Only one database connection exists in the entire application
     * THREAD-SAFE: Protected by synchronized block in getInstance()
     */
    private static DBConnection instance;
    
    /**
     * connection - JDBC Connection object
     * PURPOSE: Active database connection to SQLite database
     * TYPE: Connection
     * VISIBILITY: private
     * LIFETIME: Exists for entire application lifecycle
     * AUTO-RECONNECT: Reconnects if connection is lost
     */
    private Connection connection;
    
    /**
     * initialized - Flag to track database initialization
     * PURPOSE: Prevents multiple database initializations
     * TYPE: static boolean
     * VISIBILITY: private
     * USAGE: Ensures schema creation and seed data insertion happen only once
     */
    private static boolean initialized = false;
    
    /**
     * DATABASE CONFIGURATION:
     * These constants define database file and connection URL
     */
    
    /**
     * DB_FILE - SQLite database file name
     * PURPOSE: Name of the SQLite database file
     * TYPE: static final String
     * LOCATION: Project root directory
     * FORMAT: SQLite database file (.sqlite extension)
     */
    private static final String DB_FILE = "zest_db.sqlite";
    
    /**
     * DB_URL - JDBC connection URL
     * PURPOSE: JDBC URL for connecting to SQLite database
     * TYPE: static final String
     * FORMAT: "jdbc:sqlite:{filename}"
     * USAGE: Used by DriverManager.getConnection()
     */
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;
    
    /**
     * Private constructor - Singleton Pattern
     * 
     * PURPOSE:
     * Private constructor prevents external instantiation.
     * Only getInstance() can create DBConnection instance.
     * 
     * INITIALIZATION FLOW:
     * 1. Create database connection using DriverManager
     * 2. Enable foreign key constraints
     * 3. Initialize database schema (if not already initialized)
     * 4. Handle connection errors gracefully
     * 
     * SINGLETON PATTERN:
     * - Constructor is private
     * - Cannot be called from outside class
     * - Ensures only one instance exists
     */
    private DBConnection() {
        try {
            /**
             * STEP 1: CREATE DATABASE CONNECTION
             * SQLite driver is automatically loaded (no need for Class.forName)
             * SQLite will create the database file if it doesn't exist
             */
            this.connection = DriverManager.getConnection(DB_URL);
            
            /**
             * STEP 2: ENABLE FOREIGN KEYS
             * SQLite requires foreign key constraints to be explicitly enabled
             * This ensures referential integrity (cascade deletes, etc.)
             */
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON"); // Enable foreign key constraints
            }
            
            /**
             * STEP 3: INITIALIZE DATABASE SCHEMA
             * Only initialize once (checked by initialized flag)
             * Creates tables and inserts seed data if needed
             */
            if (!initialized) {
                initializeDatabase(); // Initialize schema and seed data
                initialized = true; // Mark as initialized
            }
            
            System.out.println("SQLite database connected: " + DB_FILE); // Log success
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Print error but don't crash the application
             * Connection will be null, but application can still run
             */
            System.err.println("Failed to establish database connection:");
            e.printStackTrace();
        }
    }
    
    /**
     * getInstance() - Gets singleton instance
     * 
     * PURPOSE:
     * Returns the single DBConnection instance. Creates it if it doesn't exist.
     * Uses Double-Checked Locking for thread safety.
     * 
     * SINGLETON PATTERN:
     * - Ensures only one database connection exists
     * - Thread-safe using synchronized block
     * - Double-checked locking for performance
     * 
     * THREAD SAFETY:
     * - First check: Fast path if instance exists
     * - Synchronized block: Thread-safe creation
     * - Second check: Prevents multiple instances in multi-threaded environment
     * 
     * @return The single DBConnection instance
     */
    public static DBConnection getInstance() {
        /**
         * FIRST CHECK (FAST PATH):
         * If instance already exists, return it immediately
         * This avoids synchronization overhead in most cases
         */
        if (instance == null) {
            /**
             * SYNCHRONIZED BLOCK:
             * Only one thread can enter at a time
             * Prevents race condition in multi-threaded environment
             */
            synchronized (DBConnection.class) {
                /**
                 * SECOND CHECK (DOUBLE-CHECKED LOCKING):
                 * Check again after acquiring lock
                 * Another thread might have created instance while we waited
                 */
                if (instance == null) {
                    instance = new DBConnection(); // Create single instance
                }
            }
        }
        return instance; // Return instance
    }
    
    /**
     * initializeDatabase() - Initializes database schema and seed data
     * 
     * PURPOSE:
     * Checks if database tables exist, creates them if needed, and inserts seed data.
     * Also migrates existing databases to new schema if tables already exist.
     * 
     * INITIALIZATION FLOW:
     * 1. Check if tables exist (try to query users table)
     * 2. If tables don't exist:
     *    - Create all tables (createTables)
     *    - Insert seed data (insertSeedData)
     * 3. If tables exist:
     *    - Migrate database schema (migrateDatabase)
     * 
     * MIGRATION:
     * - Handles existing databases gracefully
     * - Adds new columns without losing data
     * - Updates existing data to match new schema
     */
    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            /**
             * CHECK IF TABLES EXIST:
             * Try to query users table
             * If query succeeds, tables exist
             * If query fails, tables don't exist
             */
            boolean tablesExist = false;
            try (var rs = stmt.executeQuery("SELECT 1 FROM users LIMIT 1")) {
                // If query succeeds, table exists
                tablesExist = true;
            } catch (SQLException e) {
                // Tables don't exist, we'll create them
                tablesExist = false;
            }
            
            if (!tablesExist) {
                /**
                 * CASE 1: NEW DATABASE
                 * Tables don't exist, create everything from scratch
                 */
                System.out.println("Initializing database schema and seed data...");
                
                createTables(stmt); // Create all tables
                insertSeedData(stmt); // Insert seed data
                
                System.out.println("Database initialized successfully!");
            } else {
                /**
                 * CASE 2: EXISTING DATABASE
                 * Tables exist, migrate to new schema
                 */
                System.out.println("Database already exists. Migrating schema...");
                migrateDatabase(stmt); // Migrate existing database
            }
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if initialization fails
             */
            System.err.println("Error initializing database:");
            e.printStackTrace();
        }
    }
    
    /**
     * migrateDatabase() - Migrates existing database to new schema
     * 
     * PURPOSE:
     * Adds new columns and tables to existing database without losing data.
     * Handles cases where columns already exist gracefully.
     * 
     * MIGRATION FLOW:
     * 1. Add new columns to existing tables (if they don't exist)
     * 2. Create new tables (if they don't exist)
     * 3. Update existing data to match new schema
     * 
     * COLUMNS ADDED:
     * - users: password_hash, business_license
     * - restaurants: merchant_id
     * - menu_items: current_price, is_available
     * - orders: total_amount, share_link
     * 
     * TABLES CREATED:
     * - reviews: New table for customer reviews
     * 
     * DATA UPDATES:
     * - Copy price to current_price in menu_items
     * - Set is_available to 1 (true) in menu_items
     * - Copy total_price to total_amount in orders
     * 
     * @param stmt Statement object for executing SQL
     * @throws SQLException If migration fails
     */
    private void migrateDatabase(Statement stmt) throws SQLException {
        /**
         * ADD NEW COLUMNS TO EXISTING TABLES:
         * Use try-catch to handle cases where columns already exist
         * This allows migration to run multiple times safely
         */
        
        // Add password_hash column to users table
        try {
            stmt.execute("ALTER TABLE users ADD COLUMN password_hash TEXT");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        // Add business_license column to users table
        try {
            stmt.execute("ALTER TABLE users ADD COLUMN business_license TEXT");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        // Add merchant_id column to restaurants table
        try {
            stmt.execute("ALTER TABLE restaurants ADD COLUMN merchant_id INTEGER");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        // Add current_price column to menu_items table
        try {
            stmt.execute("ALTER TABLE menu_items ADD COLUMN current_price REAL");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        // Add is_available column to menu_items table
        try {
            stmt.execute("ALTER TABLE menu_items ADD COLUMN is_available INTEGER DEFAULT 1");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        // Add total_amount column to orders table
        try {
            stmt.execute("ALTER TABLE orders ADD COLUMN total_amount REAL DEFAULT 0.0");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        // Add share_link column to orders table
        try {
            stmt.execute("ALTER TABLE orders ADD COLUMN share_link TEXT DEFAULT NULL");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
        
        /**
         * CREATE NEW TABLES:
         * Create reviews table if it doesn't exist
         */
        try {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reviews (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    restaurant_id INTEGER,
                    customer_id INTEGER,
                    rating INTEGER NOT NULL CHECK(rating >= 1 AND rating <= 5),
                    comment TEXT,
                    review_date TEXT DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
                    FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);
        } catch (SQLException e) {
            // Table already exists, ignore
        }
        
        /**
         * UPDATE EXISTING DATA:
         * Copy old column values to new columns
         * This ensures data consistency
         */
        try {
            // Copy price to current_price in menu_items
            stmt.execute("UPDATE menu_items SET current_price = price WHERE current_price IS NULL");
            // Set is_available to 1 (true) if null
            stmt.execute("UPDATE menu_items SET is_available = 1 WHERE is_available IS NULL");
        } catch (SQLException e) {
            // Ignore errors (columns might not exist)
        }
        
        // Copy total_price to total_amount in orders
        try {
            stmt.execute("UPDATE orders SET total_amount = total_price WHERE total_amount IS NULL OR total_amount = 0");
        } catch (SQLException e) {
            // Ignore errors (columns might not exist)
        }
        
        System.out.println("Database migration completed!");
    }
    
    /**
     * createTables() - Creates all database tables
     * 
     * PURPOSE:
     * Creates all database tables according to UML schema.
     * Uses IF NOT EXISTS to prevent errors if tables already exist.
     * 
     * TABLES CREATED:
     * 1. users: User accounts (Customers and Merchants)
     * 2. restaurants: Restaurant information
     * 3. menu_items: Menu items for restaurants
     * 4. orders: Customer orders
     * 5. order_items: Order items with Snapshot Pattern (priceAtPurchase)
     * 6. reviews: Customer reviews for restaurants
     * 
     * FOREIGN KEYS:
     * - Ensures referential integrity
     * - Cascade deletes for related records
     * 
     * @param stmt Statement object for executing SQL
     * @throws SQLException If table creation fails
     */
    private void createTables(Statement stmt) throws SQLException {
        /**
         * TABLE: USERS
         * Stores user accounts (Customers and Merchants)
         * Updated with password_hash and business_license for UML spec
         * Role-based access: CUSTOMER or MERCHANT
         */
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                password_hash TEXT,
                role TEXT DEFAULT 'CUSTOMER' CHECK(role IN ('CUSTOMER', 'MERCHANT')),
                business_license TEXT
            )
        """);
        
        /**
         * TABLE: RESTAURANTS
         * Stores restaurant information
         * Updated with merchant_id to link to Merchant owner
         */
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS restaurants (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                merchant_id INTEGER,
                image_url TEXT DEFAULT 'default_rest.png',
                FOREIGN KEY (merchant_id) REFERENCES users(id) ON DELETE SET NULL
            )
        """);
        
        /**
         * TABLE: MENU_ITEMS
         * Stores menu items for restaurants
         * Updated with is_available and current_price for UML spec
         */
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS menu_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                restaurant_id INTEGER,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                current_price REAL,
                is_available INTEGER DEFAULT 1,
                description TEXT,
                image_url TEXT DEFAULT 'default_item.png',
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
            )
        """);
        
        /**
         * TABLE: ORDERS
         * Stores customer orders
         * Updated schema with total_amount and share_link
         */
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS orders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                total_price REAL DEFAULT 0.0,
                total_amount REAL DEFAULT 0.0,
                status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'PREPARING', 'READY', 'DELIVERED')),
                order_date TEXT DEFAULT CURRENT_TIMESTAMP,
                group_code TEXT DEFAULT NULL,
                share_link TEXT DEFAULT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """);
        
        /**
         * TABLE: ORDER_ITEMS (The Snapshot Pattern)
         * Stores order items with priceAtPurchase (frozen price)
         * CRITICAL: price_at_purchase captures price at order time
         */
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
        
        /**
         * TABLE: REVIEWS
         * Stores customer reviews for restaurants
         * New table for UML spec
         */
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS reviews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                restaurant_id INTEGER,
                customer_id INTEGER,
                rating INTEGER NOT NULL CHECK(rating >= 1 AND rating <= 5),
                comment TEXT,
                review_date TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE,
                FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """);
    }
    
    /**
     * insertSeedData() - Inserts seed data into database
     * 
     * PURPOSE:
     * Inserts initial test data for development and testing.
     * Creates sample users, restaurants, menu items, and orders.
     * 
     * SEED DATA INCLUDES:
     * - Users: 1 Merchant (Ahmed), 2 Customers (Ziad, Hamdy)
     * - Restaurants: 3 restaurants (Burger King, Pizza Hut, KFC)
     * - Menu Items: Sample items for each restaurant
     * - Orders: 1 sample order for testing history screen
     * 
     * PASSWORD:
     * - All users have password '123' for simplicity
     * - password_hash will be calculated by User class
     * - password kept for backward compatibility
     * 
     * @param stmt Statement object for executing SQL
     * @throws SQLException If data insertion fails
     */
    private void insertSeedData(Statement stmt) throws SQLException {
        /**
         * INSERT USERS:
         * Create sample users (1 Merchant, 2 Customers)
         * Password is '123' for everyone (for testing)
         * IMPORTANT: Merchant must be inserted first to get ID = 1 for restaurant ownership
         */
        stmt.execute("""
            INSERT INTO users (name, email, password, role, business_license) VALUES
            ('Ahmed Admin', 'ahmed@zest.com', '123', 'MERCHANT', 'BL-2024-001')
        """);
        
        stmt.execute("""
            INSERT INTO users (name, email, password, role) VALUES
            ('Ziad Customer', 'ziad@gmail.com', '123', 'CUSTOMER'),
            ('Hamdy Customer', 'hamdy@gmail.com', '123', 'CUSTOMER')
        """);
        
        /**
         * INSERT RESTAURANTS:
         * Create sample restaurants linked to merchant_id = 1 (Ahmed Admin)
         * Merchant must exist before restaurants are created
         */
        stmt.execute("""
            INSERT INTO restaurants (name, merchant_id, image_url) VALUES
            ('Burger King', 1, 'bk.png'),
            ('Pizza Hut', 1, 'pizza.png'),
            ('KFC', 1, 'kfc.png')
        """);
        
        /**
         * INSERT MENU ITEMS FOR BURGER KING (Restaurant ID: 1):
         * Create sample menu items with prices and availability
         */
        stmt.execute("""
            INSERT INTO menu_items (restaurant_id, name, price, current_price, is_available, description, image_url) VALUES
            (1, 'Whopper', 85.00, 85.00, 1, 'Flame-grilled beef patty', 'whopper.png'),
            (1, 'Chicken Royale', 75.00, 75.00, 1, 'Crispy chicken sandwich', 'chicken_royale.png'),
            (1, 'Fries (Medium)', 30.00, 30.00, 1, 'Golden crispy fries', 'fries.png')
        """);
        
        /**
         * INSERT MENU ITEMS FOR PIZZA HUT (Restaurant ID: 2):
         * Create sample pizza items
         */
        stmt.execute("""
            INSERT INTO menu_items (restaurant_id, name, price, current_price, is_available, description, image_url) VALUES
            (2, 'Super Supreme (M)', 150.00, 150.00, 1, 'Loaded with toppings', 'supreme.png'),
            (2, 'Pepperoni (M)', 130.00, 130.00, 1, 'Classic pepperoni pizza', 'pepperoni.png')
        """);
        
        /**
         * INSERT MENU ITEMS FOR KFC (Restaurant ID: 3):
         * Create sample KFC items
         */
        stmt.execute("""
            INSERT INTO menu_items (restaurant_id, name, price, current_price, is_available, description, image_url) VALUES
            (3, 'Mighty Bucket', 200.00, 200.00, 1, '2 Pieces chicken + rice', 'bucket.png'),
            (3, 'Rizo', 60.00, 60.00, 1, 'Rice with spicy sauce', 'rizo.png')
        """);
        
        /**
         * INSERT DUMMY ORDER:
         * Create sample order for testing History screen
         * Ziad (User ID 2) bought a Whopper
         */
        stmt.execute("""
            INSERT INTO orders (user_id, total_price, total_amount, status) VALUES (2, 85.00, 85.00, 'DELIVERED')
        """);
        
        /**
         * INSERT ORDER ITEM:
         * Create order item with Snapshot Pattern (priceAtPurchase)
         * CRITICAL: price_at_purchase = 85.00 (frozen price)
         */
        stmt.execute("""
            INSERT INTO order_items (order_id, menu_item_name, price_at_purchase, quantity) 
            VALUES (1, 'Whopper', 85.00, 1)
        """);
    }
    
    /**
     * getConnection() - Gets database connection
     * 
     * PURPOSE:
     * Returns the database connection. Reconnects if connection is lost.
     * 
     * CONNECTION MANAGEMENT:
     * - Checks if connection is null or closed
     * - Reconnects if connection is lost
     * - Re-enables foreign keys after reconnection
     * 
     * @return Connection object, or null if connection failed
     */
    public Connection getConnection() {
        try {
            /**
             * CHECK CONNECTION STATUS:
             * Verify connection exists and is not closed
             * Reconnect if connection was lost
             */
            if (connection == null || connection.isClosed()) {
                /**
                 * RECONNECT:
                 * Create new connection if connection was lost
                 */
                this.connection = DriverManager.getConnection(DB_URL);
                
                /**
                 * RE-ENABLE FOREIGN KEYS:
                 * Foreign keys must be enabled after reconnection
                 */
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON"); // Enable foreign keys
                }
            }
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if reconnection fails
             */
            System.err.println("Failed to reconnect to database:");
            e.printStackTrace();
        }
        return connection; // Return connection (may be null if failed)
    }
    
    /**
     * closeConnection() - Closes database connection
     * 
     * PURPOSE:
     * Closes the database connection gracefully.
     * Called when application shuts down.
     * 
     * CLOSURE FLOW:
     * 1. Check if connection exists and is not already closed
     * 2. Close connection
     * 3. Handle errors gracefully
     */
    public void closeConnection() {
        try {
            /**
             * CHECK CONNECTION STATUS:
             * Only close if connection exists and is not already closed
             */
            if (connection != null && !connection.isClosed()) {
                connection.close(); // Close connection
            }
        } catch (SQLException e) {
            /**
             * ERROR HANDLING:
             * Log error if closure fails
             */
            System.err.println("Error closing database connection:");
            e.printStackTrace();
        }
    }
    
    /**
     * getDatabasePath() - Gets database file path
     * 
     * PURPOSE:
     * Returns the absolute path to the SQLite database file.
     * Useful for debugging and file location verification.
     * 
     * @return Absolute path to the SQLite database file
     */
    public static String getDatabasePath() {
        return new File(DB_FILE).getAbsolutePath(); // Return absolute path
    }
}
