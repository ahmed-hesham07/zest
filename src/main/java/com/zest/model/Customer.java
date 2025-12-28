package com.zest.model;

/**
 * Customer class
 * Represents a customer user in the system
 */
public class Customer extends User {
    private int loyaltyPoints;
    
    public Customer(int id, String name, String email, String password) {
        super(id, name, email, password, "CUSTOMER");
        this.loyaltyPoints = 0;
    }
    
    public Customer(int id, String name, String email, String password, int loyaltyPoints) {
        super(id, name, email, password, "CUSTOMER");
        this.loyaltyPoints = loyaltyPoints;
    }
    
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    
    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
}
