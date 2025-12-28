-- ==========================================
-- NOTE: This file is DEPRECATED
-- The application now uses SQLite with auto-initialization
-- Database is automatically created on first run
-- This file is kept for reference only (MySQL syntax)
-- ==========================================
-- 1. SETUP DATABASE (Clean Slate) - MySQL Syntax
-- ==========================================
DROP DATABASE IF EXISTS zest_db;
CREATE DATABASE zest_db;
USE zest_db;

-- ==========================================
-- 2. CREATE TABLES
-- ==========================================

-- TABLE: USERS
-- Stores both Customers and Merchants.
-- 'role' distinguishes them (Simple Factory Pattern support).
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL, -- In a real app, hash this!
    role ENUM('CUSTOMER', 'MERCHANT') DEFAULT 'CUSTOMER',
    loyalty_points INT DEFAULT 0
);

-- TABLE: RESTAURANTS
-- Stores the list of places available.
CREATE TABLE restaurants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_url VARCHAR(255) DEFAULT 'default_rest.png'
);

-- TABLE: MENU_ITEMS
-- The base food items.
-- Note: The "Decorator Pattern" (Extra Cheese) usually happens in Java,
-- but the base item (Burger) must exist here.
CREATE TABLE menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    description VARCHAR(255),
    image_url VARCHAR(255) DEFAULT 'default_item.png',
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

-- TABLE: ORDERS
-- Tracks the main order transaction.
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    total_price DOUBLE DEFAULT 0.0,
    status ENUM('PENDING', 'PREPARING', 'READY', 'DELIVERED') DEFAULT 'PENDING',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    group_code VARCHAR(10) DEFAULT NULL, -- For the Group Order Bonus
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- TABLE: ORDER_ITEMS (The Snapshot Pattern)
-- This table is critical. It saves the price *at the moment of purchase*.
-- Even if the Menu Price changes later, this record stays the same.
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    menu_item_name VARCHAR(100), -- Snapshot of the name
    price_at_purchase DOUBLE,    -- Snapshot of the price
    quantity INT DEFAULT 1,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- ==========================================
-- 3. INSERT DUMMY DATA (For Testing)
-- ==========================================

-- Users (Password is '123' for everyone for simplicity)
INSERT INTO users (name, email, password, role) VALUES
('Ahmed Admin', 'ahmed@zest.com', '123', 'MERCHANT'),
('Ziad Customer', 'ziad@gmail.com', '123', 'CUSTOMER'),
('Hamdy Customer', 'hamdy@gmail.com', '123', 'CUSTOMER');

-- Restaurants
INSERT INTO restaurants (name, image_url) VALUES
('Burger King', 'bk.png'),
('Pizza Hut', 'pizza.png'),
('KFC', 'kfc.png');

-- Menu Items for Burger King (ID: 1)
INSERT INTO menu_items (restaurant_id, name, price, description) VALUES
(1, 'Whopper', 85.00, 'Flame-grilled beef patty'),
(1, 'Chicken Royale', 75.00, 'Crispy chicken sandwich'),
(1, 'Fries (Medium)', 30.00, 'Golden crispy fries');

-- Menu Items for Pizza Hut (ID: 2)
INSERT INTO menu_items (restaurant_id, name, price, description) VALUES
(2, 'Super Supreme (M)', 150.00, 'Loaded with toppings'),
(2, 'Pepperoni (M)', 130.00, 'Classic pepperoni pizza');

-- Menu Items for KFC (ID: 3)
INSERT INTO menu_items (restaurant_id, name, price, description) VALUES
(3, 'Mighty Bucket', 200.00, '2 Pieces chicken + rice'),
(3, 'Rizo', 60.00, 'Rice with spicy sauce');

-- Dummy Order (To test History Screen)
-- Ziad (User ID 2) bought a Whopper
INSERT INTO orders (user_id, total_price, status) VALUES (2, 85.00, 'DELIVERED');
INSERT INTO order_items (order_id, menu_item_name, price_at_purchase, quantity) VALUES (1, 'Whopper', 85.00, 1);

-- ==========================================
-- END OF SCRIPT
-- ==========================================