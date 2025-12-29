package com.zest.model;

/**
 * Merchant - Merchant User Model Class
 * 
 * PURPOSE:
 * This class represents a merchant (restaurant owner) user in the system.
 * Merchants can manage their restaurant's menu items, update prices, and manage data.
 * 
 * UML SPECIFICATION:
 * - Extends: User (abstract base class)
 * - Fields: businessLicense (String)
 * - Methods: addMenuItem(MenuItem m), updatePrice(MenuItem m, double price), purgeData()
 * 
 * INHERITANCE:
 * - Extends User class
 * - Inherits: email, passwordHash, authenticate()
 * - Adds: businessLicense, addMenuItem(), updatePrice(), purgeData()
 * 
 * RESTAURANT MANAGEMENT:
 * - Merchants own restaurants
 * - Can add menu items to their restaurant
 * - Can update prices of menu items
 * - Can purge/clean up restaurant data
 * 
 * BUSINESS LICENSE:
 * - businessLicense: Unique identifier for merchant's business
 * - Required for merchant registration
 * - Used for verification and compliance
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: User Management & Merchant Features
 */

/**
 * Merchant class
 * Represents a merchant/restaurant owner who can manage restaurants and menu items
 */
public class Merchant extends User {
    
    /**
     * UML SPEC FIELD:
     * This field is required by the UML specification
     */
    
    /**
     * businessLicense - Business license number
     * PURPOSE: Unique identifier for merchant's business registration
     * TYPE: String
     * VISIBILITY: private
     * UML SPEC: Required field
     * USAGE: Used for business verification and compliance
     * EXAMPLE: "BL-12345", "LIC-2024-001"
     */
    private String businessLicense;
    
    /**
     * Constructor - Creates new Merchant instance
     * 
     * PURPOSE:
     * Initializes Merchant with email, password hash, and business license.
     * 
     * UML SPEC: Required constructor
     * 
     * @param email Merchant's email address
     * @param passwordHash Pre-hashed password (hex string)
     * @param businessLicense Business license number
     */
    public Merchant(String email, String passwordHash, String businessLicense) {
        super(email, passwordHash); // Call User constructor with email and passwordHash
        this.businessLicense = businessLicense; // Set business license
    }
    
    /**
     * UML SPEC METHODS:
     * These methods are required by the UML specification
     */
    
    /**
     * addMenuItem() - Adds a menu item to restaurant
     * 
     * PURPOSE:
     * Allows merchant to add a new menu item to their restaurant's menu.
     * This is the UML-specified method for menu item management.
     * 
     * UML SPEC: Required method
     * 
     * IMPLEMENTATION NOTE:
     * This method would typically add the menu item to a Restaurant's menu list.
     * Full implementation depends on Restaurant-Merchant relationship.
     * 
     * @param m The MenuItem object to add to restaurant menu
     */
    public void addMenuItem(MenuItem m) {
        /**
         * MENU ITEM ADDITION:
         * This would typically add the menu item to a restaurant's menu
         * Implementation depends on Restaurant-Merchant relationship
         * This is a placeholder for the menu item addition logic
         */
        // This would typically add to a restaurant's menu
        // Implementation depends on Restaurant-Merchant relationship
    }
    
    /**
     * updatePrice() - Updates price of a menu item
     * 
     * PURPOSE:
     * Allows merchant to update the price of a menu item.
     * This is the UML-specified method for price management.
     * 
     * UML SPEC: Required method
     * PRICE UPDATE: Changes MenuItem.currentPrice, not OrderItem.priceAtPurchase
     * 
     * PRICE UPDATE FLOW:
     * 1. Validate menu item is not null
     * 2. Call MenuItem.setPrice() to update currentPrice
     * 3. Price change affects future orders, not historical orders
     * 
     * @param m The MenuItem object to update
     * @param price New price for the menu item
     */
    public void updatePrice(MenuItem m, double price) {
        /**
         * VALIDATION: Check if menu item is valid
         * Only update price if menu item is not null
         */
        if (m != null) {
            /**
             * UPDATE PRICE:
             * Call MenuItem.setPrice() to update currentPrice
             * This changes the live price, not historical order prices
             */
            m.setPrice(price); // Update menu item price
        }
    }
    
    /**
     * purgeData() - Purges/cleans up merchant data
     * 
     * PURPOSE:
     * Allows merchant to purge/clean up their restaurant data.
     * This is the UML-specified method for data management.
     * 
     * UML SPEC: Required method
     * 
     * IMPLEMENTATION NOTE:
     * This method would typically clear restaurant data, menu items, orders, etc.
     * Used for testing, cleanup, or account deletion.
     * 
     * DATA PURGE FLOW:
     * 1. Clear restaurant menu items
     * 2. Clear restaurant orders
     * 3. Clear restaurant reviews
     * 4. Other cleanup operations
     */
    public void purgeData() {
        /**
         * DATA CLEANUP:
         * Implementation for data cleanup
         * This would typically clear restaurant data
         * This is a placeholder for the data purge logic
         */
        // Implementation for data cleanup
        // This would typically clear restaurant data
    }
    
    /**
     * GETTER AND SETTER METHODS:
     * These methods provide access to businessLicense field
     */
    
    /**
     * getBusinessLicense() - Gets business license number
     * 
     * PURPOSE:
     * Returns the business license number of this merchant.
     * 
     * @return Business license number
     */
    public String getBusinessLicense() {
        return businessLicense; // Return business license
    }
    
    /**
     * setBusinessLicense() - Sets business license number
     * 
     * PURPOSE:
     * Updates the business license number of this merchant.
     * 
     * @param businessLicense New business license number
     */
    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense; // Set business license
    }
}
