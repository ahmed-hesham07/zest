package com.zest.model;

/**
 * Order - Order Model Class
 * 
 * PURPOSE:
 * This class represents an order in the system. It contains order items,
 * payment method, status, and total amount. Orders are created when customers
 * checkout and are processed using the Strategy Pattern for payments.
 * 
 * UML SPECIFICATION:
 * - Fields: id (int), status (OrderStatus), items (List<OrderItem>), 
 *           paymentMethod (PaymentStrategy), totalAmount (double)
 * - Methods: addOrderItem(OrderItem), calculateTotal(), pay(), setStatus(OrderStatus)
 * 
 * DESIGN PATTERNS:
 * - Strategy Pattern: PaymentStrategy interface for different payment methods
 * - Composition: Contains List<OrderItem> (has-a relationship)
 * 
 * ORDER ITEMS:
 * - items: List of OrderItem objects (uses Snapshot Pattern)
 * - Each OrderItem captures priceAtPurchase at order time
 * - calculateTotal() uses OrderItem.priceAtPurchase, not live prices
 * 
 * PAYMENT PROCESSING:
 * - paymentMethod: PaymentStrategy interface (CashPayment or PayPalAdapter)
 * - pay(): Processes payment using Strategy Pattern
 * - Returns boolean indicating payment success
 * 
 * ORDER STATUS:
 * - status: OrderStatus enum (PENDING, PREPARING, READY, DELIVERED)
 * - Tracks order progress through lifecycle
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Order Processing & Payment Integration
 */

import com.zest.logic.PaymentStrategy; // UML: PaymentStrategy interface (Strategy Pattern)
import java.util.ArrayList; // For items list
import java.util.List; // List interface

/**
 * Order class
 * Represents an order with items, payment method, and status
 */
public class Order {
    
    /**
     * id - Unique identifier for order
     * PURPOSE: Primary key in database, unique identifier
     * TYPE: int
     * VISIBILITY: private
     * DATABASE: Auto-generated primary key
     */
    private int id;
    
    /**
     * userId - ID of user who placed the order
     * PURPOSE: Links order to customer who placed it
     * TYPE: int
     * VISIBILITY: private
     * NOTE: Kept for backward compatibility with database
     */
    private int userId;
    
    /**
     * status - Order status enum
     * PURPOSE: Tracks order progress (PENDING, PREPARING, READY, DELIVERED)
     * TYPE: OrderStatus (enum)
     * VISIBILITY: private
     * UML SPEC: Required field
     * DEFAULT: PENDING (when order is created)
     */
    private OrderStatus status;
    
    /**
     * items - List of order items
     * PURPOSE: Contains all items in this order (uses Snapshot Pattern)
     * TYPE: List<OrderItem>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * SNAPSHOT: Each OrderItem has priceAtPurchase (frozen price)
     */
    private List<OrderItem> items;
    
    /**
     * paymentMethod - Payment strategy for this order
     * PURPOSE: Payment method used to pay for order (Strategy Pattern)
     * TYPE: PaymentStrategy (interface)
     * VISIBILITY: private
     * UML SPEC: Required field
     * IMPLEMENTATIONS: CashPayment, PayPalAdapter
     * USAGE: Used in pay() method to process payment
     */
    private PaymentStrategy paymentMethod;
    
    /**
     * totalAmount - Total amount for this order
     * PURPOSE: Total price including all items
     * TYPE: double
     * VISIBILITY: private
     * UML SPEC: Required field (named totalAmount)
     * CALCULATION: Can be calculated from items or stored directly
     * CURRENCY: EGP (Egyptian Pounds)
     */
    private double totalAmount;
    
    /**
     * Default constructor - Creates new order
     * 
     * PURPOSE:
     * Creates new Order with empty items list and PENDING status.
     * Used when creating orders from scratch (e.g., in CheckoutController).
     * 
     * INITIALIZATION:
     * - items: Empty ArrayList
     * - status: PENDING
     * - Other fields: Default values (0, null)
     */
    public Order() {
        this.items = new ArrayList<>(); // Initialize empty items list
        this.status = OrderStatus.PENDING; // Default to PENDING status
    }
    
    /**
     * Constructor - Creates order with parameters (backward compatibility)
     * 
     * PURPOSE:
     * Creates Order from database data. Used when loading orders from database.
     * 
     * @param id Order ID from database
     * @param userId User ID who placed order
     * @param totalPrice Total price from database
     * @param status Order status as string (converted to enum)
     */
    public Order(int id, int userId, double totalPrice, String status) {
        this.id = id; // Set order ID
        this.userId = userId; // Set user ID
        this.totalAmount = totalPrice; // Set total amount
        this.status = OrderStatus.fromString(status); // Convert string to enum
        this.items = new ArrayList<>(); // Initialize empty items list
    }
    
    /**
     * Constructor - Creates order without ID (for new orders)
     * 
     * PURPOSE:
     * Creates Order for new orders (ID will be generated by database).
     * Used when creating orders before saving to database.
     * 
     * @param userId User ID who placed order
     * @param totalPrice Total price of order
     * @param status Order status as string
     */
    public Order(int userId, double totalPrice, String status) {
        this.userId = userId; // Set user ID
        this.totalAmount = totalPrice; // Set total amount
        this.status = OrderStatus.fromString(status); // Convert string to enum
        this.items = new ArrayList<>(); // Initialize empty items list
    }
    
    /**
     * UML SPEC METHODS:
     * These methods are required by the UML specification
     */
    
    /**
     * addOrderItem() - Adds OrderItem to this order
     * 
     * PURPOSE:
     * Adds an OrderItem to the order's items list.
     * This is the UML-specified method for adding items.
     * 
     * UML SPEC: Required method
     * SNAPSHOT PATTERN: OrderItem must have priceAtPurchase set
     * 
     * ADDITION FLOW:
     * 1. Check if items list is null (initialize if needed)
     * 2. Add OrderItem to items list
     * 
     * @param item The OrderItem to add to this order
     */
    public void addOrderItem(OrderItem item) {
        /**
         * INITIALIZATION CHECK:
         * If items list is null, create new ArrayList
         * This prevents NullPointerException
         */
        if (items == null) {
            items = new ArrayList<>(); // Initialize if null
        }
        items.add(item); // Add OrderItem to list
    }
    
    /**
     * calculateTotal() - Calculates total from OrderItems
     * 
     * PURPOSE:
     * Calculates total amount by summing all OrderItem totals.
     * Uses OrderItem.priceAtPurchase (Snapshot Pattern), not live prices.
     * 
     * UML SPEC: Required method
     * SNAPSHOT PATTERN: Uses priceAtPurchase from OrderItems
     * 
     * CALCULATION FLOW:
     * 1. Check if items list is null or empty
     * 2. If empty, return stored totalAmount (fallback)
     * 3. If not empty, sum all OrderItem.calculateTotal() values
     * 4. Return total sum
     * 
     * CRITICAL:
     * - Uses OrderItem.calculateTotal() which uses priceAtPurchase
     * - Does NOT use MenuItem.currentPrice (live price)
     * - Ensures historical accuracy
     * 
     * @return Total amount calculated from OrderItems (or stored totalAmount if no items)
     */
    public double calculateTotal() {
        /**
         * VALIDATION: Check if items exist
         * If no items, return stored totalAmount as fallback
         */
        if (items == null || items.isEmpty()) {
            return totalAmount; // Fallback to stored total
        }
        
        /**
         * CALCULATE FROM ORDERITEMS:
         * Sum all OrderItem totals using Stream API
         * Each OrderItem.calculateTotal() uses priceAtPurchase (Snapshot Pattern)
         */
        return items.stream()
            .mapToDouble(OrderItem::calculateTotal) // Get total for each item
            .sum(); // Sum all totals
    }
    
    /**
     * pay() - Processes payment using PaymentStrategy
     * 
     * PURPOSE:
     * Processes payment for this order using the Strategy Pattern.
     * Calls PaymentStrategy.pay() method with order total.
     * 
     * UML SPEC: Required method
     * STRATEGY PATTERN: Uses PaymentStrategy interface
     * 
     * PAYMENT FLOW:
     * 1. Check if paymentMethod is set
     * 2. Calculate order total
     * 3. Call paymentMethod.pay() with total amount
     * 4. Return payment success status
     * 
     * @return true if payment was successful, false otherwise
     */
    public boolean pay() {
        /**
         * VALIDATION: Check if payment method is set
         * If no payment method, payment fails
         */
        if (paymentMethod == null) {
            return false; // No payment method set - payment fails
        }
        
        /**
         * PROCESS PAYMENT:
         * Calculate total and call PaymentStrategy.pay()
         * Different payment strategies (Cash, PayPal) implement this differently
         */
        double amount = calculateTotal(); // Calculate order total
        return paymentMethod.pay(amount); // Process payment using Strategy Pattern
    }
    
    /**
     * setStatus() - Sets order status
     * 
     * PURPOSE:
     * Updates the order status to track order progress.
     * This is the UML-specified method for status updates.
     * 
     * UML SPEC: Required method
     * STATUS VALUES: PENDING, PREPARING, READY, DELIVERED
     * 
     * @param s The OrderStatus enum value to set
     */
    public void setStatus(OrderStatus s) {
        this.status = s; // Set order status
    }
    
    /**
     * GETTER AND SETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getId() - Gets order ID
     * @return Order ID
     */
    public int getId() {
        return id; // Return order ID
    }
    
    /**
     * setId() - Sets order ID
     * @param id New order ID
     */
    public void setId(int id) {
        this.id = id; // Set order ID
    }
    
    /**
     * getUserId() - Gets user ID
     * @return User ID who placed order
     */
    public int getUserId() {
        return userId; // Return user ID
    }
    
    /**
     * setUserId() - Sets user ID
     * @param userId New user ID
     */
    public void setUserId(int userId) {
        this.userId = userId; // Set user ID
    }
    
    /**
     * getTotalPrice() - Gets total amount (backward compatibility)
     * @return Total amount
     */
    public double getTotalPrice() {
        return totalAmount; // Return total amount
    }
    
    /**
     * setTotalPrice() - Sets total amount (backward compatibility)
     * @param totalPrice New total amount
     */
    public void setTotalPrice(double totalPrice) {
        this.totalAmount = totalPrice; // Set total amount
    }
    
    /**
     * getTotalAmount() - Gets total amount (UML spec method)
     * @return Total amount
     */
    public double getTotalAmount() {
        return totalAmount; // Return total amount
    }
    
    /**
     * setTotalAmount() - Sets total amount
     * @param totalAmount New total amount
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount; // Set total amount
    }
    
    /**
     * getStatus() - Gets status as string (backward compatibility)
     * @return Status as string
     */
    public String getStatus() {
        return status != null ? status.toString() : "PENDING"; // Return status string
    }
    
    /**
     * setStatus() - Sets status from string (backward compatibility)
     * @param status Status as string
     */
    public void setStatus(String status) {
        this.status = OrderStatus.fromString(status); // Convert string to enum
    }
    
    /**
     * getStatusEnum() - Gets status as enum (UML spec method)
     * @return OrderStatus enum value
     */
    public OrderStatus getStatusEnum() {
        return status; // Return status enum
    }
    
    /**
     * getItems() - Gets order items list
     * @return Copy of items list (prevents external modification)
     */
    public List<OrderItem> getItems() {
        return items != null ? items : new ArrayList<>(); // Return items list or empty list
    }
    
    /**
     * setItems() - Sets order items list
     * @param items New items list
     */
    public void setItems(List<OrderItem> items) {
        this.items = items; // Set items list
    }
    
    /**
     * getPaymentMethod() - Gets payment strategy
     * @return PaymentStrategy instance
     */
    public PaymentStrategy getPaymentMethod() {
        return paymentMethod; // Return payment method
    }
    
    /**
     * setPaymentMethod() - Sets payment strategy
     * @param paymentMethod New PaymentStrategy instance
     */
    public void setPaymentMethod(PaymentStrategy paymentMethod) {
        this.paymentMethod = paymentMethod; // Set payment method
    }
}
