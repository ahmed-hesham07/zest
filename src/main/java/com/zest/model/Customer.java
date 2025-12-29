package com.zest.model;

/**
 * Customer - Customer User Model Class
 * 
 * PURPOSE:
 * This class represents a customer user in the system. Customers can place orders,
 * view order history, leave reviews, and earn loyalty points.
 * 
 * UML SPECIFICATION:
 * - Extends: User (abstract base class)
 * - Fields: name (String), orderHistory (List<Order>), loyaltyPoints (int)
 * - Methods: placeOrder(Order o), leaveReview(Review r)
 * 
 * INHERITANCE:
 * - Extends User class
 * - Inherits: email, passwordHash, authenticate()
 * - Adds: name, orderHistory, loyaltyPoints, placeOrder(), leaveReview()
 * 
 * ORDER MANAGEMENT:
 * - orderHistory: List of all orders placed by this customer
 * - placeOrder(): Adds order to history and awards loyalty points
 * 
 * LOYALTY PROGRAM:
 * - loyaltyPoints: Points earned from orders (1 point per 10 EGP)
 * - Automatically calculated when order is placed
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: User Management & Customer Features
 */

import java.util.ArrayList; // For orderHistory list
import java.util.List; // List interface

/**
 * Customer class
 * Represents a customer user who can place orders and leave reviews
 */
public class Customer extends User {
    
    /**
     * UML SPEC FIELDS:
     * These fields are required by the UML specification
     */
    
    /**
     * name - Customer's full name
     * PURPOSE: Display name for customer account
     * TYPE: String
     * VISIBILITY: private
     * UML SPEC: Required field
     * EXAMPLE: "Ahmed Mohamed", "Sara Ali"
     */
    private String name;
    
    /**
     * orderHistory - List of orders placed by this customer
     * PURPOSE: Stores all orders customer has placed
     * TYPE: List<Order>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * USAGE: Used to display order history, track past purchases
     */
    private List<Order> orderHistory;
    
    /**
     * loyaltyPoints - Points earned from orders
     * PURPOSE: Tracks customer loyalty points for rewards program
     * TYPE: int
     * VISIBILITY: private
     * CALCULATION: 1 point per 10 EGP spent
     * INITIALIZATION: Starts at 0
     * USAGE: Can be used for discounts, rewards, etc.
     */
    private int loyaltyPoints;
    
    /**
     * Constructor - Creates new Customer instance
     * 
     * PURPOSE:
     * Initializes Customer with name, email, and password hash.
     * Sets up empty order history and zero loyalty points.
     * 
     * UML SPEC: Required constructor
     * 
     * @param name Customer's full name
     * @param email Customer's email address
     * @param passwordHash Pre-hashed password (hex string)
     */
    public Customer(String name, String email, String passwordHash) {
        super(email, passwordHash); // Call User constructor with email and passwordHash
        this.name = name; // Set customer name
        this.orderHistory = new ArrayList<>(); // Initialize empty order history
        this.loyaltyPoints = 0; // Initialize loyalty points to zero
    }
    
    /**
     * placeOrder() - Places an order and adds to history
     * 
     * PURPOSE:
     * Adds an order to customer's order history and awards loyalty points.
     * This is the UML-specified method for order placement.
     * 
     * UML SPEC: Required method
     * LOYALTY POINTS: Automatically calculates and awards points (1 per 10 EGP)
     * 
     * ORDER PLACEMENT FLOW:
     * 1. Validate order is not null
     * 2. Add order to orderHistory list
     * 3. Calculate loyalty points earned (totalAmount / 10)
     * 4. Add points to loyaltyPoints
     * 
     * @param o The Order object to place
     */
    public void placeOrder(Order o) {
        /**
         * VALIDATION: Check if order is valid
         * Only add non-null orders to history
         */
        if (o != null) {
            /**
             * ADD ORDER TO HISTORY:
             * Add order to orderHistory list for tracking
             */
            orderHistory.add(o); // Add order to history
            
            /**
             * AWARD LOYALTY POINTS:
             * Calculate points based on order total
             * Formula: 1 point per 10 EGP spent
             * Example: 150 EGP order = 15 points
             */
            int pointsEarned = (int)(o.getTotalAmount() / 10); // Calculate points
            this.loyaltyPoints += pointsEarned; // Add points to total
        }
    }
    
    /**
     * leaveReview() - Leaves a review for a restaurant
     * 
     * PURPOSE:
     * Allows customer to leave a review for a restaurant.
     * This is the UML-specified method for review creation.
     * 
     * UML SPEC: Required method
     * VALIDATION: Ensures review author matches this customer
     * 
     * REVIEW FLOW:
     * 1. Validate review is not null
     * 2. Validate review author is this customer
     * 3. Review would typically be added to Restaurant's reviews list
     * 
     * NOTE: Full implementation depends on Restaurant-Review relationship
     * 
     * @param r The Review object to leave
     */
    public void leaveReview(Review r) {
        /**
         * VALIDATION: Check review and author
         * Only process valid reviews where author matches this customer
         */
        if (r != null && r.getAuthor() == this) {
            /**
             * REVIEW PROCESSING:
             * Review would typically be added to Restaurant's reviews list
             * Implementation depends on Restaurant-Review relationship
             * This is a placeholder for the review logic
             */
            // Review would typically be added to a Restaurant's reviews list
            // Implementation depends on Restaurant-Review relationship
        }
    }
    
    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getName() - Gets customer's name
     * 
     * PURPOSE:
     * Returns the full name of this customer.
     * 
     * @return Customer's name
     */
    public String getName() {
        return name; // Return name
    }
    
    /**
     * getOrderHistory() - Gets customer's order history
     * 
     * PURPOSE:
     * Returns a copy of the order history list.
     * Returns copy to prevent external modification of internal list.
     * 
     * @return Copy of order history list
     */
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory); // Return copy of list
    }
    
    /**
     * getLoyaltyPoints() - Gets customer's loyalty points
     * 
     * PURPOSE:
     * Returns the current loyalty points balance.
     * 
     * @return Loyalty points balance
     */
    public int getLoyaltyPoints() {
        return loyaltyPoints; // Return loyalty points
    }
    
    /**
     * SETTER METHODS:
     * These methods allow modification of fields
     */
    
    /**
     * setName() - Sets customer's name
     * 
     * PURPOSE:
     * Updates the name of this customer.
     * 
     * @param name New name for customer
     */
    public void setName(String name) {
        this.name = name; // Set name
    }
    
    /**
     * setOrderHistory() - Sets customer's order history
     * 
     * PURPOSE:
     * Updates the order history list.
     * Creates copy of provided list to prevent external modification.
     * 
     * @param orderHistory New order history list (can be null)
     */
    public void setOrderHistory(List<Order> orderHistory) {
        // Create copy to prevent external modification
        this.orderHistory = orderHistory != null ? new ArrayList<>(orderHistory) : new ArrayList<>();
    }
    
    /**
     * setLoyaltyPoints() - Sets customer's loyalty points
     * 
     * PURPOSE:
     * Updates the loyalty points balance.
     * 
     * @param loyaltyPoints New loyalty points balance
     */
    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints; // Set loyalty points
    }
}
