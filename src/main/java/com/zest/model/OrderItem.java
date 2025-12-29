package com.zest.model;

/**
 * OrderItem - Snapshot Pattern Implementation
 * 
 * PURPOSE:
 * This class implements the Snapshot Pattern to capture the price of a menu item
 * at the moment of purchase. This ensures historical orders show correct prices
 * even if menu prices change later.
 * 
 * UML SPECIFICATION:
 * - Fields: itemReference (MenuItem), quantity (int), priceAtPurchase (double)
 * - Method: calculateTotal() -> double
 * 
 * DESIGN PATTERN:
 * - Snapshot Pattern: Captures immutable snapshot of price at purchase time
 * 
 * CRITICAL CONCEPT:
 * - priceAtPurchase: Frozen price at time of order (DOES NOT CHANGE)
 * - itemReference.currentPrice: Live price (CAN CHANGE)
 * - OrderItem ALWAYS uses priceAtPurchase, NEVER currentPrice
 * 
 * WHY SNAPSHOT PATTERN:
 * - Menu prices may change after order is placed
 * - Historical orders must show price at time of purchase
 * - Prevents price discrepancies in order history
 * - Ensures accurate financial records
 * 
 * EXAMPLE:
 * - Customer orders burger at 50 EGP (priceAtPurchase = 50)
 * - Merchant later changes burger price to 60 EGP
 * - OrderItem still shows 50 EGP (priceAtPurchase)
 * - New orders show 60 EGP (currentPrice)
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Order Processing & Snapshot Pattern Implementation
 */

/**
 * OrderItem class
 * Represents an item in an order with frozen price (Snapshot Pattern)
 */
public class OrderItem {
    
    /**
     * UML SPEC FIELDS:
     * These fields are required by the UML specification
     */
    
    /**
     * itemReference - Reference to the menu item
     * PURPOSE: Links OrderItem to MenuItem for item details (name, description, etc.)
     * TYPE: MenuItem
     * VISIBILITY: private
     * UML SPEC: Required field
     * NOTE: We use itemReference.getName() for display, but NOT itemReference.getPrice()
     */
    private MenuItem itemReference;
    
    /**
     * quantity - Quantity of this item ordered
     * PURPOSE: Number of units of this item in the order
     * TYPE: int
     * VISIBILITY: private
     * UML SPEC: Required field
     * EXAMPLE: 2 burgers, 3 pizzas
     */
    private int quantity;
    
    /**
     * priceAtPurchase - CRITICAL: Price at moment of purchase (frozen)
     * PURPOSE: Captures price snapshot at order time (DOES NOT CHANGE)
     * TYPE: double
     * VISIBILITY: private
     * UML SPEC: Required field
     * CRITICAL: Must be set during constructor
     * SNAPSHOT: This is the frozen price, not the live price
     * NOTE: This price NEVER changes, even if MenuItem.currentPrice changes
     */
    private double priceAtPurchase;
    
    /**
     * Constructor - CRITICAL: priceAtPurchase MUST be set during construction
     * 
     * PURPOSE:
     * Creates new OrderItem with item reference, quantity, and frozen price.
     * The priceAtPurchase is captured at this moment and never changes.
     * 
     * UML SPEC: Required constructor
     * SNAPSHOT PATTERN: Captures price snapshot at construction time
     * 
     * CONSTRUCTION FLOW:
     * 1. Store reference to MenuItem
     * 2. Store quantity ordered
     * 3. CRITICAL: Capture current price as priceAtPurchase (snapshot)
     * 
     * @param itemReference Reference to the MenuItem object
     * @param quantity Number of units ordered
     * @param priceAtPurchase The price at the moment of purchase (CRITICAL: snapshot this value)
     */
    public OrderItem(MenuItem itemReference, int quantity, double priceAtPurchase) {
        this.itemReference = itemReference; // Store item reference
        this.quantity = quantity; // Store quantity
        this.priceAtPurchase = priceAtPurchase; // CRITICAL: Snapshot the price NOW
    }
    
    /**
     * calculateTotal() - Calculates total for this order item
     * 
     * PURPOSE:
     * Calculates total price for this order item using frozen price.
     * This is the UML-specified calculation method.
     * 
     * UML SPEC: Required method
     * SNAPSHOT PATTERN: Uses priceAtPurchase, NOT itemReference.currentPrice
     * 
     * CRITICAL:
     * - ALWAYS uses priceAtPurchase (frozen price)
     * - NEVER uses itemReference.getPrice() (live price)
     * - This ensures historical accuracy
     * 
     * CALCULATION:
     * Total = priceAtPurchase * quantity
     * 
     * @return Total amount for this order item (priceAtPurchase * quantity)
     */
    public double calculateTotal() {
        /**
         * CRITICAL: Use priceAtPurchase, NOT itemReference.currentPrice
         * priceAtPurchase is frozen at purchase time
         * itemReference.currentPrice may have changed since purchase
         */
        return priceAtPurchase * quantity; // Calculate using frozen price
    }
    
    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getItemReference() - Gets reference to menu item
     * 
     * PURPOSE:
     * Returns the MenuItem reference for this order item.
     * Used to get item name, description, etc. (but NOT price).
     * 
     * @return Reference to MenuItem object
     */
    public MenuItem getItemReference() {
        return itemReference; // Return item reference
    }
    
    /**
     * getQuantity() - Gets quantity ordered
     * 
     * PURPOSE:
     * Returns the quantity of this item in the order.
     * 
     * @return Quantity ordered
     */
    public int getQuantity() {
        return quantity; // Return quantity
    }
    
    /**
     * getPriceAtPurchase() - Gets frozen price at purchase
     * 
     * PURPOSE:
     * Returns the price that was captured at purchase time.
     * This is the frozen price that never changes.
     * 
     * CRITICAL: This is the price used for calculations, not currentPrice
     * 
     * @return Price at purchase time (frozen)
     */
    public double getPriceAtPurchase() {
        return priceAtPurchase; // Return frozen price
    }
    
    /**
     * SETTER METHODS:
     * These methods allow modification of fields
     */
    
    /**
     * setQuantity() - Sets quantity ordered
     * 
     * PURPOSE:
     * Updates the quantity of this item in the order.
     * 
     * NOTE: priceAtPurchase has no setter - it's immutable (snapshot)
     * 
     * @param quantity New quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity; // Set quantity
    }
}
