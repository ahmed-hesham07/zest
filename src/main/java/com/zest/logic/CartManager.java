package com.zest.logic;

import com.zest.model.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cart Item with quantity
 */
class CartItem {
    MenuItem item;
    int quantity;
    
    CartItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}

public class CartManager {
    private static CartManager instance;
    private Map<MenuItem, Integer> itemQuantities; // Map item to quantity
    private Integer currentRestaurantId; // Track which restaurant the cart belongs to

    private CartManager() {
        itemQuantities = new HashMap<>();
        currentRestaurantId = null;
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    /**
     * Add item to cart. Enforces single restaurant rule.
     * @param item The menu item to add
     * @return true if added successfully, false if item is from different restaurant
     */
    public boolean addItem(MenuItem item) {
        // If cart is empty, set the restaurant
        if (currentRestaurantId == null) {
            currentRestaurantId = item.getRestaurantId();
            itemQuantities.put(item, 1);
            return true;
        }
        
        // If item is from same restaurant, add it
        if (currentRestaurantId == item.getRestaurantId()) {
            itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + 1);
            return true;
        }
        
        // Item is from different restaurant - reject it
        return false;
    }
    
    /**
     * Set quantity for an item
     * @param item The menu item
     * @param quantity The new quantity (must be > 0)
     */
    public void setItemQuantity(MenuItem item, int quantity) {
        if (quantity <= 0) {
            removeItem(item);
        } else {
            itemQuantities.put(item, quantity);
        }
    }
    
    /**
     * Get quantity for an item
     * @param item The menu item
     * @return The quantity of this item in cart
     */
    public int getItemQuantity(MenuItem item) {
        return itemQuantities.getOrDefault(item, 0);
    }
    
    /**
     * Check if an item can be added (same restaurant or empty cart)
     * @param item The menu item to check
     * @return true if item can be added
     */
    public boolean canAddItem(MenuItem item) {
        return currentRestaurantId == null || currentRestaurantId == item.getRestaurantId();
    }
    
    /**
     * Get the current restaurant ID in the cart
     * @return Restaurant ID or null if cart is empty
     */
    public Integer getCurrentRestaurantId() {
        return currentRestaurantId;
    }
    
    /**
     * Clear the cart (also clears restaurant selection)
     */
    public void clear() {
        itemQuantities.clear();
        currentRestaurantId = null;
    }
    
    public void removeItem(MenuItem item) {
        itemQuantities.remove(item);
        // If cart becomes empty, clear restaurant selection
        if (itemQuantities.isEmpty()) {
            currentRestaurantId = null;
        }
    }

    /**
     * Get all items in cart (for iteration)
     * @return List of all items (may contain duplicates if quantity > 1)
     */
    public List<MenuItem> getItems() {
        List<MenuItem> items = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : itemQuantities.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                items.add(entry.getKey());
            }
        }
        return items;
    }
    
    /**
     * Get unique items with their quantities
     * @return Map of items to quantities
     */
    public Map<MenuItem, Integer> getItemQuantities() {
        return new HashMap<>(itemQuantities);
    }

    public double getTotalPrice() {
        // Calculate total: sum of (item price * quantity) for each unique item
        return itemQuantities.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }
    
    /**
     * Get subtotal (before VAT and delivery)
     * @return Subtotal amount
     */
    public double getSubtotal() {
        return getTotalPrice();
    }
}