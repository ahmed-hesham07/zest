package com.zest.model;

/**
 * OrderStatus - Order Status Enumeration
 * 
 * PURPOSE:
 * This enum represents the possible statuses of an order in the system.
 * It provides type-safe status values and conversion methods for backward compatibility.
 * 
 * UML SPECIFICATION:
 * - Values: PENDING, PREPARING, READY, DELIVERED
 * - Methods: fromString(String), toString()
 * 
 * ORDER LIFECYCLE:
 * 1. PENDING: Order placed, waiting to be prepared
 * 2. PREPARING: Order being prepared by restaurant
 * 3. READY: Order ready for delivery
 * 4. DELIVERED: Order delivered to customer
 * 
 * TYPE SAFETY:
 * - Enum prevents invalid status values
 * - Compiler enforces valid status usage
 * - Better than string-based status (prevents typos)
 * 
 * BACKWARD COMPATIBILITY:
 * - fromString(): Converts string to enum (for database/legacy code)
 * - toString(): Converts enum to string (for database/display)
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Order Processing & Status Management
 */

/**
 * OrderStatus enum
 * Represents the status of an order in the system
 */
public enum OrderStatus {
    
    /**
     * PENDING - Order placed, waiting to be prepared
     * PURPOSE: Initial status when order is first placed
     * DISPLAY: "Pending" with yellow/orange badge
     * ICON: ⏳ (hourglass)
     */
    PENDING,
    
    /**
     * PREPARING - Order being prepared by restaurant
     * PURPOSE: Restaurant is currently preparing the order
     * DISPLAY: "Preparing" with blue badge
     * ICON: 👨‍🍳 (chef)
     */
    PREPARING,
    
    /**
     * READY - Order ready for delivery
     * PURPOSE: Order is prepared and ready to be delivered
     * DISPLAY: "Ready" with green badge
     * ICON: ✅ (checkmark)
     */
    READY,
    
    /**
     * DELIVERED - Order delivered to customer
     * PURPOSE: Final status - order has been delivered
     * DISPLAY: "Delivered" with gray badge
     * ICON: 📦 (package)
     */
    DELIVERED;
    
    /**
     * fromString() - Converts string to OrderStatus enum
     * 
     * PURPOSE:
     * Converts string representation of status to OrderStatus enum.
     * Used for backward compatibility with database and legacy code.
     * 
     * BACKWARD COMPATIBILITY:
     * - Database stores status as string
     * - Legacy code may use string status
     * - This method converts string to enum
     * 
     * CONVERSION FLOW:
     * 1. Check if string is null (default to PENDING)
     * 2. Convert string to uppercase
     * 3. Use valueOf() to get enum value
     * 4. If invalid, default to PENDING
     * 
     * @param status String representation of status (case-insensitive)
     * @return OrderStatus enum value (defaults to PENDING if invalid)
     */
    public static OrderStatus fromString(String status) {
        /**
         * VALIDATION: Check for null
         * If status is null, default to PENDING
         */
        if (status == null) {
            return PENDING; // Default to PENDING if null
        }
        
        try {
            /**
             * CONVERSION:
             * Convert string to uppercase and get enum value
             * valueOf() throws IllegalArgumentException if invalid
             */
            return OrderStatus.valueOf(status.toUpperCase()); // Convert to enum
        } catch (IllegalArgumentException e) {
            /**
             * ERROR HANDLING:
             * If string doesn't match any enum value, default to PENDING
             * This prevents crashes from invalid status strings
             */
            return PENDING; // Default to PENDING if invalid
        }
    }
    
    /**
     * toString() - Converts OrderStatus enum to string
     * 
     * PURPOSE:
     * Converts OrderStatus enum to string representation.
     * Used for backward compatibility with database and display.
     * 
     * BACKWARD COMPATIBILITY:
     * - Database expects string status
     * - Display code may need string
     * - This method converts enum to string
     * 
     * CONVERSION:
     * Returns enum name as string (e.g., "PENDING", "DELIVERED")
     * 
     * @return String representation of status (enum name)
     */
    @Override
    public String toString() {
        return this.name(); // Return enum name as string
    }
}
