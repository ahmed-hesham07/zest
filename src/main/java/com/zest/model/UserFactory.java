package com.zest.model;

/**
 * User Factory
 * Factory Pattern: Creates User objects based on role
 */
public class UserFactory {
    /**
     * Factory Method Pattern
     * Creates a User object based on the role string
     * @param id User ID
     * @param name User name
     * @param email User email
     * @param password User password
     * @param role User role ("CUSTOMER" or "MERCHANT")
     * @return User object (Customer or Merchant)
     */
    public static User createUser(int id, String name, String email, String password, String role) {
        if (role == null) {
            return null;
        }
        
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return new Customer(id, name, email, password);
            case "MERCHANT":
                // Merchant class can be implemented later if needed
                // For now, return null or throw exception
                throw new IllegalArgumentException("Merchant class not yet implemented");
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }
    
    /**
     * Overloaded factory method for creating Customer with loyalty points
     */
    public static Customer createCustomer(int id, String name, String email, String password, int loyaltyPoints) {
        return new Customer(id, name, email, password, loyaltyPoints);
    }
}
