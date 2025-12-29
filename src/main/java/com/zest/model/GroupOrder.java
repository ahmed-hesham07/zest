package com.zest.model;

/**
 * GroupOrder - Group Order Model Class
 * 
 * PURPOSE:
 * This class extends Order to support group orders where multiple customers
 * can add items and split the bill. Each customer pays only for their items.
 * 
 * UML SPECIFICATION:
 * - Extends: Order (inherits all Order functionality)
 * - Fields: participantSplits (Map<Customer, List<OrderItem>>), shareLink (String)
 * - Methods: addToGroup(Customer c, OrderItem item), splitBill() -> Map<Customer, Double>
 * 
 * INHERITANCE:
 * - Extends Order class
 * - Inherits: id, status, items, paymentMethod, totalAmount, all Order methods
 * - Adds: participantSplits, shareLink, addToGroup(), splitBill()
 * 
 * GROUP ORDER FEATURES:
 * - Multiple customers can add items to same order
 * - Each customer's items are tracked separately
 * - Bill can be split so each customer pays for their items
 * - Share link allows customers to join the group order
 * 
 * BILL SPLITTING:
 * - participantSplits: Maps each customer to their order items
 * - splitBill(): Calculates how much each customer owes
 * - Each customer pays only for their items
 * 
 * SHARE LINK:
 * - shareLink: Unique URL for sharing group order
 * - Allows other customers to join the order
 * - Generated using order ID and timestamp
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Group Order Features & Bill Splitting
 */

import java.util.ArrayList; // For OrderItem lists
import java.util.HashMap; // For participantSplits map
import java.util.List; // List interface
import java.util.Map; // Map interface

/**
 * GroupOrder class
 * Extends Order to support group orders with bill splitting
 */
public class GroupOrder extends Order {
    
    /**
     * UML SPEC FIELDS:
     * These fields are required by the UML specification
     */
    
    /**
     * participantSplits - Maps each customer to their order items
     * PURPOSE: Tracks which items belong to which customer for bill splitting
     * TYPE: Map<Customer, List<OrderItem>>
     * VISIBILITY: private
     * UML SPEC: Required field
     * STRUCTURE: Key = Customer, Value = List of OrderItems they added
     * USAGE: Used by splitBill() to calculate each customer's portion
     */
    private Map<Customer, List<OrderItem>> participantSplits;
    
    /**
     * shareLink - Unique URL for sharing group order
     * PURPOSE: Allows customers to share order link so others can join
     * TYPE: String
     * VISIBILITY: private
     * UML SPEC: Required field
     * FORMAT: "zest.group/{orderId}-{timestamp}"
     * USAGE: Shared with other customers to join the order
     */
    private String shareLink;
    
    /**
     * Constructor - Creates new GroupOrder
     * 
     * PURPOSE:
     * Creates new GroupOrder with empty participant splits and generates share link.
     * Calls parent Order constructor with PENDING status.
     * 
     * @param userId User ID who created the group order
     */
    public GroupOrder(int userId) {
        super(userId, 0.0, "PENDING"); // Call parent constructor with PENDING status
        this.participantSplits = new HashMap<>(); // Initialize empty participant splits map
        this.shareLink = generateShareLink(); // Generate unique share link
    }
    
    /**
     * UML SPEC METHODS:
     * These methods are required by the UML specification
     */
    
    /**
     * addToGroup() - Adds customer and their order item to group
     * 
     * PURPOSE:
     * Adds a customer and their order item to the group order.
     * Tracks which items belong to which customer for bill splitting.
     * 
     * UML SPEC: Required method
     * 
     * ADDITION FLOW:
     * 1. Validate customer and item are not null
     * 2. Add item to customer's list in participantSplits map
     * 3. Add item to parent Order's items list (inherited from Order)
     * 
     * BILL SPLITTING:
     * - Items are tracked per customer in participantSplits
     * - splitBill() uses this to calculate each customer's portion
     * 
     * @param c The Customer adding the item
     * @param item The OrderItem they're adding
     */
    public void addToGroup(Customer c, OrderItem item) {
        /**
         * VALIDATION: Check customer and item are valid
         * Only add non-null customers and items
         */
        if (c != null && item != null) {
            /**
             * ADD TO PARTICIPANT SPLITS:
             * Use computeIfAbsent to create list if customer doesn't exist
             * Then add item to customer's list
             */
            participantSplits.computeIfAbsent(c, k -> new ArrayList<>()).add(item);
            
            /**
             * ADD TO PARENT ORDER:
             * Also add item to parent Order's items list
             * This allows Order.calculateTotal() to work correctly
             */
            addOrderItem(item); // Call parent method to add item
        }
    }
    
    /**
     * splitBill() - Splits bill among participants
     * 
     * PURPOSE:
     * Calculates how much each customer owes based on their items.
     * Returns a map showing each customer's portion of the bill.
     * 
     * UML SPEC: Required method
     * 
     * BILL SPLITTING FLOW:
     * 1. Loop through each customer in participantSplits
     * 2. Get customer's list of OrderItems
     * 3. Calculate total for customer's items (sum of OrderItem.calculateTotal())
     * 4. Add customer and their total to billSplit map
     * 5. Return map with each customer's portion
     * 
     * CALCULATION:
     * - Uses OrderItem.calculateTotal() which uses priceAtPurchase (Snapshot Pattern)
     * - Each customer pays only for their items
     * - Total of all customer portions equals order total
     * 
     * @return Map of Customer to their portion of the bill (amount they owe)
     */
    public Map<Customer, Double> splitBill() {
        /**
         * CREATE BILL SPLIT MAP:
         * Map will store each customer and their portion
         */
        Map<Customer, Double> billSplit = new HashMap<>();
        
        /**
         * LOOP THROUGH PARTICIPANTS:
         * For each customer, calculate their portion
         */
        for (Map.Entry<Customer, List<OrderItem>> entry : participantSplits.entrySet()) {
            Customer customer = entry.getKey(); // Get customer
            List<OrderItem> items = entry.getValue(); // Get customer's items
            
            /**
             * CALCULATE CUSTOMER TOTAL:
             * Sum all OrderItem totals for this customer
             * Uses OrderItem.calculateTotal() which uses priceAtPurchase
             */
            double customerTotal = items.stream()
                .mapToDouble(OrderItem::calculateTotal) // Get total for each item
                .sum(); // Sum all item totals
            
            /**
             * ADD TO BILL SPLIT:
             * Store customer and their portion in map
             */
            billSplit.put(customer, customerTotal); // Add customer portion
        }
        
        return billSplit; // Return bill split map
    }
    
    /**
     * generateShareLink() - Generates unique share link
     * 
     * PURPOSE:
     * Creates a unique share link for this group order.
     * Used to allow other customers to join the order.
     * 
     * LINK FORMAT:
     * "zest.group/{orderId}-{timestamp}"
     * 
     * UNIQUENESS:
     * - Uses order ID (from parent Order)
     * - Uses current timestamp in milliseconds
     * - Ensures each link is unique
     * 
     * @return Unique share link string
     */
    private String generateShareLink() {
        /**
         * GENERATE LINK:
         * Combine order ID and timestamp for uniqueness
         * Format: "zest.group/{orderId}-{timestamp}"
         */
        return "zest.group/" + getId() + "-" + System.currentTimeMillis(); // Generate unique link
    }
    
    /**
     * GETTER AND SETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getParticipantSplits() - Gets participant splits map
     * 
     * PURPOSE:
     * Returns a copy of the participant splits map.
     * Returns copy to prevent external modification.
     * 
     * @return Copy of participant splits map
     */
    public Map<Customer, List<OrderItem>> getParticipantSplits() {
        return new HashMap<>(participantSplits); // Return copy of map
    }
    
    /**
     * setParticipantSplits() - Sets participant splits map
     * 
     * PURPOSE:
     * Updates the participant splits map.
     * Creates copy to prevent external modification.
     * 
     * @param participantSplits New participant splits map (can be null)
     */
    public void setParticipantSplits(Map<Customer, List<OrderItem>> participantSplits) {
        // Create copy to prevent external modification
        this.participantSplits = participantSplits != null ? new HashMap<>(participantSplits) : new HashMap<>();
    }
    
    /**
     * getShareLink() - Gets share link
     * 
     * PURPOSE:
     * Returns the share link for this group order.
     * 
     * @return Share link string
     */
    public String getShareLink() {
        return shareLink; // Return share link
    }
    
    /**
     * setShareLink() - Sets share link
     * 
     * PURPOSE:
     * Updates the share link for this group order.
     * 
     * @param shareLink New share link string
     */
    public void setShareLink(String shareLink) {
        this.shareLink = shareLink; // Set share link
    }
}
