package com.zest.logic;

/**
 * CartManager - Shopping Cart Manager Singleton
 * 
 * PURPOSE:
 * This class manages the shopping cart for the current user session using the Singleton Pattern.
 * It tracks items, quantities, and enforces business rules (single restaurant per cart).
 * 
 * DESIGN PATTERNS:
 * - Singleton Pattern: Only one cart instance exists per application
 * 
 * CART MANAGEMENT:
 * - itemQuantities: Maps MenuItem to quantity
 * - currentRestaurantId: Tracks which restaurant items belong to
 * - Enforces single restaurant rule (can't mix restaurants)
 * 
 * BUSINESS RULES:
 * - Cart can only contain items from one restaurant at a time
 * - Adding item from different restaurant is rejected
 * - Cart is cleared when restaurant selection changes
 * 
 * QUANTITY MANAGEMENT:
 * - Tracks quantity for each unique item
 * - Supports increase/decrease quantity
 * - Supports item removal
 * 
 * PRICE CALCULATION:
 * - getTotalPrice(): Calculates total from all items
 * - getSubtotal(): Same as total (before VAT/delivery)
 * - Uses MenuItem.getPrice() for current prices
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Cart Management & Shopping Experience
 */

import com.zest.model.MenuItem; // UML Class: Menu item model
import java.util.ArrayList; // For items list
import java.util.HashMap; // For itemQuantities map
import java.util.List; // List interface
import java.util.Map; // Map interface

/**
 * CartItem - Internal helper class for cart items
 * 
 * PURPOSE:
 * Internal class to represent cart item with quantity.
 * Used internally by CartManager (not exposed externally).
 * 
 * NOTE: This is a package-private helper class.
 */
class CartItem {
    MenuItem item; // Menu item reference
    int quantity; // Quantity of this item
    
    /**
     * Constructor - Creates cart item
     * @param item Menu item
     * @param quantity Quantity
     */
    CartItem(MenuItem item, int quantity) {
        this.item = item; // Set item
        this.quantity = quantity; // Set quantity
    }
}

/**
 * CartManager class
 * Singleton that manages shopping cart (one cart per application)
 */
public class CartManager {
    
    /**
     * instance - Single CartManager instance (Singleton Pattern)
     * PURPOSE: Stores the only instance of CartManager
     * TYPE: static CartManager
     * VISIBILITY: private
     * SINGLETON: Only one cart exists in the entire application
     */
    private static CartManager instance;
    
    /**
     * itemQuantities - Maps MenuItem to quantity
     * PURPOSE: Stores items and their quantities in cart
     * TYPE: Map<MenuItem, Integer>
     * VISIBILITY: private
     * STRUCTURE: Key = MenuItem, Value = Quantity
     * USAGE: Efficient lookup and quantity management
     */
    private Map<MenuItem, Integer> itemQuantities;
    
    /**
     * currentRestaurantId - ID of restaurant items belong to
     * PURPOSE: Tracks which restaurant the cart items are from
     * TYPE: Integer (nullable)
     * VISIBILITY: private
     * BUSINESS RULE: Cart can only contain items from one restaurant
     * NULL: Cart is empty (no restaurant selected)
     */
    private Integer currentRestaurantId;

    /**
     * Private constructor - Singleton Pattern
     * 
     * PURPOSE:
     * Private constructor prevents external instantiation.
     * Only getInstance() can create CartManager instance.
     * 
     * INITIALIZATION:
     * - Creates empty itemQuantities map
     * - Sets currentRestaurantId to null (empty cart)
     */
    private CartManager() {
        itemQuantities = new HashMap<>(); // Initialize empty map
        currentRestaurantId = null; // Cart is empty (no restaurant)
    }

    /**
     * getInstance() - Gets singleton instance
     * 
     * PURPOSE:
     * Returns the single CartManager instance. Creates it if it doesn't exist.
     * 
     * SINGLETON PATTERN:
     * - First call creates instance
     * - Subsequent calls return same instance
     * - Ensures only one cart exists
     * 
     * @return The single CartManager instance
     */
    public static CartManager getInstance() {
        /**
         * SINGLETON CREATION:
         * If instance doesn't exist, create it
         * If instance exists, return it
         */
        if (instance == null) {
            instance = new CartManager(); // Create single instance
        }
        return instance; // Return instance
    }

    /**
     * addItem() - Adds item to cart
     * 
     * PURPOSE:
     * Adds a menu item to the cart. Enforces single restaurant rule.
     * 
     * BUSINESS RULE:
     * - Cart can only contain items from one restaurant
     * - First item sets the restaurant
     * - Items from different restaurant are rejected
     * 
     * ADDITION FLOW:
     * 1. If cart is empty, set restaurant and add item
     * 2. If item is from same restaurant, add/increment quantity
     * 3. If item is from different restaurant, reject (return false)
     * 
     * @param item The MenuItem to add to cart
     * @return true if added successfully, false if rejected (different restaurant)
     */
    public boolean addItem(MenuItem item) {
        /**
         * CASE 1: CART IS EMPTY
         * If cart is empty, set restaurant and add first item
         */
        if (currentRestaurantId == null) {
            currentRestaurantId = item.getRestaurantId(); // Set restaurant
            itemQuantities.put(item, 1); // Add item with quantity 1
            return true; // Success
        }
        
        /**
         * CASE 2: ITEM FROM SAME RESTAURANT
         * If item is from same restaurant, add/increment quantity
         */
        if (currentRestaurantId == item.getRestaurantId()) {
            /**
             * INCREMENT QUANTITY:
             * Get current quantity (or 0 if new), add 1, put back
             */
            itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + 1);
            return true; // Success
        }
        
        /**
         * CASE 3: ITEM FROM DIFFERENT RESTAURANT
         * Reject item - cart can only contain items from one restaurant
         */
        return false; // Rejected - different restaurant
    }
    
    /**
     * setItemQuantity() - Sets quantity for an item
     * 
     * PURPOSE:
     * Updates the quantity of an item in the cart.
     * If quantity is 0 or negative, removes item.
     * 
     * QUANTITY UPDATE FLOW:
     * 1. Check if quantity is <= 0
     * 2. If <= 0, remove item from cart
     * 3. If > 0, update quantity
     * 
     * @param item The MenuItem to update
     * @param quantity The new quantity (must be > 0, or item will be removed)
     */
    public void setItemQuantity(MenuItem item, int quantity) {
        /**
         * VALIDATION: Check quantity
         * If quantity <= 0, remove item instead of setting quantity
         */
        if (quantity <= 0) {
            removeItem(item); // Remove item if quantity <= 0
        } else {
            itemQuantities.put(item, quantity); // Update quantity
        }
    }
    
    /**
     * getItemQuantity() - Gets quantity for an item
     * 
     * PURPOSE:
     * Returns the quantity of an item in the cart.
     * Returns 0 if item is not in cart.
     * 
     * @param item The MenuItem to check
     * @return Quantity of item in cart (0 if not in cart)
     */
    public int getItemQuantity(MenuItem item) {
        return itemQuantities.getOrDefault(item, 0); // Get quantity or 0 if not found
    }
    
    /**
     * canAddItem() - Checks if item can be added
     * 
     * PURPOSE:
     * Checks if an item can be added to cart based on restaurant rule.
     * 
     * VALIDATION:
     * - Cart is empty: Can add any item
     * - Item from same restaurant: Can add
     * - Item from different restaurant: Cannot add
     * 
     * @param item The MenuItem to check
     * @return true if item can be added, false otherwise
     */
    public boolean canAddItem(MenuItem item) {
        /**
         * CHECK RESTAURANT RULE:
         * Can add if cart is empty OR item is from same restaurant
         */
        return currentRestaurantId == null || currentRestaurantId == item.getRestaurantId();
    }
    
    /**
     * getCurrentRestaurantId() - Gets current restaurant ID
     * 
     * PURPOSE:
     * Returns the ID of the restaurant items belong to.
     * Returns null if cart is empty.
     * 
     * @return Restaurant ID or null if cart is empty
     */
    public Integer getCurrentRestaurantId() {
        return currentRestaurantId; // Return restaurant ID
    }
    
    /**
     * clear() - Clears the cart
     * 
     * PURPOSE:
     * Removes all items from cart and clears restaurant selection.
     * Called after order is placed or cart is reset.
     * 
     * CLEAR FLOW:
     * 1. Clear all items from map
     * 2. Clear restaurant selection (set to null)
     */
    public void clear() {
        itemQuantities.clear(); // Clear all items
        currentRestaurantId = null; // Clear restaurant selection
    }

    /**
     * removeItem() - Removes item from cart
     * 
     * PURPOSE:
     * Completely removes an item from the cart.
     * If cart becomes empty, clears restaurant selection.
     * 
     * REMOVAL FLOW:
     * 1. Remove item from map
     * 2. If cart is empty, clear restaurant selection
     * 
     * @param item The MenuItem to remove
     */
    public void removeItem(MenuItem item) {
        itemQuantities.remove(item); // Remove item from map
        
        /**
         * CHECK IF CART IS EMPTY:
         * If cart becomes empty, clear restaurant selection
         */
        if (itemQuantities.isEmpty()) {
            currentRestaurantId = null; // Clear restaurant selection
        }
    }

    /**
     * getItems() - Gets all items in cart
     * 
     * PURPOSE:
     * Returns a list of all items in cart (may contain duplicates if quantity > 1).
     * Used for iteration and display.
     * 
     * LIST STRUCTURE:
     * - Each item appears quantity times
     * - Example: Item with quantity 3 appears 3 times in list
     * 
     * @return List of all items (duplicates for quantity > 1)
     */
    public List<MenuItem> getItems() {
        List<MenuItem> items = new ArrayList<>(); // Create list
        
        /**
         * LOOP THROUGH ITEMS:
         * For each item, add it quantity times to list
         */
        for (Map.Entry<MenuItem, Integer> entry : itemQuantities.entrySet()) {
            MenuItem item = entry.getKey(); // Get item
            int quantity = entry.getValue(); // Get quantity
            
            /**
             * ADD ITEM QUANTITY TIMES:
             * Add item to list for each unit
             */
            for (int i = 0; i < quantity; i++) {
                items.add(item); // Add item to list
            }
        }
        return items; // Return list
    }
    
    /**
     * getItemQuantities() - Gets unique items with quantities
     * 
     * PURPOSE:
     * Returns a map of unique items to their quantities.
     * Used for efficient quantity lookup and display.
     * 
     * MAP STRUCTURE:
     * - Key: MenuItem (unique)
     * - Value: Integer (quantity)
     * - Returns copy to prevent external modification
     * 
     * @return Copy of itemQuantities map
     */
    public Map<MenuItem, Integer> getItemQuantities() {
        return new HashMap<>(itemQuantities); // Return copy of map
    }

    /**
     * getTotalPrice() - Calculates total cart price
     * 
     * PURPOSE:
     * Calculates total price of all items in cart.
     * Uses current prices from MenuItem (not priceAtPurchase).
     * 
     * CALCULATION:
     * - For each item: price * quantity
     * - Sum all item totals
     * - Uses MenuItem.getPrice() (current price)
     * 
     * NOTE: This uses current prices, not snapshot prices.
     * OrderItem uses priceAtPurchase (Snapshot Pattern).
     * 
     * @return Total price of all items in cart
     */
    public double getTotalPrice() {
        /**
         * CALCULATE TOTAL:
         * For each item, multiply price by quantity
         * Sum all item totals using Stream API
         */
        return itemQuantities.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue()) // Calculate item total
            .sum(); // Sum all totals
    }
    
    /**
     * getSubtotal() - Gets subtotal (before VAT and delivery)
     * 
     * PURPOSE:
     * Returns subtotal amount (same as total price).
     * Used in checkout to calculate VAT and delivery.
     * 
     * NOTE: Currently same as getTotalPrice().
     * In future, could include discounts, promotions, etc.
     * 
     * @return Subtotal amount (before VAT and delivery)
     */
    public double getSubtotal() {
        return getTotalPrice(); // Subtotal is same as total price
    }
}
