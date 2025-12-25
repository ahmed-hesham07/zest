package com.zest.model;

/**
 * Order Model
 * Represents an order in the system
 */
public class Order {
    private int id;
    private int userId;
    private double totalPrice;
    private String status;
    
    /**
     * Default constructor
     */
    public Order() {
    }
    
    /**
     * Constructor with parameters
     * @param id Order ID
     * @param userId User ID who placed the order
     * @param totalPrice Total price of the order
     * @param status Order status (e.g., "pending", "completed", "cancelled")
     */
    public Order(int id, int userId, double totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    
    /**
     * Constructor without ID (for new orders)
     * @param userId User ID who placed the order
     * @param totalPrice Total price of the order
     * @param status Order status
     */
    public Order(int userId, double totalPrice, String status) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
