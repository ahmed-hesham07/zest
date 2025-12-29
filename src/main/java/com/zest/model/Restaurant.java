package com.zest.model;

/**
 * Restaurant - Restaurant Model Class
 * 
 * PURPOSE:
 * This class represents a restaurant in the system. Restaurants have an owner (Merchant),
 * a menu of items, customer reviews, and active orders being processed.
 * 
 * UML SPECIFICATION:
 * - Fields: id (int), name (String), owner (Merchant), menu (List<MenuItem>),
 *           reviews (List<Review>), activeOrders (List<Order>)
 * - Methods: getMenu(), receiveOrder(Order o)
 * 
 * RELATIONSHIPS:
 * - Has Merchant owner (composition)
 * - Has List<MenuItem> menu (composition)
 * - Has List<Review> reviews (composition)
 * - Has List<Order> activeOrders (composition)
 * 
 * RESTAURANT MANAGEMENT:
 * - owner: Merchant who owns this restaurant
 * - menu: List of menu items available at this restaurant
 * - reviews: Customer reviews and ratings
 * - activeOrders: Orders currently being processed
 * 
 * ORDER PROCESSING:
 * - receiveOrder(): Adds order to activeOrders list
 * - Restaurant receives orders and processes them
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: Restaurant Management & Business Logic
 */

import java.util.ArrayList; // For menu, reviews, activeOrders lists
import java.util.List; // List interface

/**
 * Restaurant class
 * Represents a restaurant with owner, menu, reviews, and active orders
 */
public class Restaurant {
    
    /**
     * id - Unique identifier for restaurant
     * PURPOSE: Primary key in database, unique identifier
     * TYPE: int
     * VISIBILITY: private
     * DATABASE: Auto-generated primary key
     */
    private int id;
    
    /**
     * name - Restaurant name
     * PURPOSE: Display name of the restaurant
     * TYPE: String
     * VISIBILITY: private
     * UML SPEC: Required field
     * EXAMPLE: "Pizza Palace", "Burger King"
     */
    private String name;
    
    /**
     * owner - Merchant who owns this restaurant
     * PURPOSE: Links restaurant to its owner (Merchant)
     * TYPE: Merchant
     * VISIBILITY: private
     * UML SPEC: Required field
     * RELATIONSHIP: Composition - Restaurant has-a Merchant
     * USAGE: Merchant can manage restaurant menu and orders
     */
    private Merchant owner;
    
    /**
     * menu - List of menu items available at this restaurant
     * PURPOSE: Stores all menu items for this restaurant
     * TYPE: List<MenuItem>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * USAGE: Displayed to customers, managed by Merchant
     */
    private List<MenuItem> menu;
    
    /**
     * reviews - List of customer reviews for this restaurant
     * PURPOSE: Stores customer reviews and ratings
     * TYPE: List<Review>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * USAGE: Displayed to customers, helps with restaurant rating
     */
    private List<Review> reviews;
    
    /**
     * activeOrders - List of orders currently being processed
     * PURPOSE: Tracks orders that are PENDING, PREPARING, or READY
     * TYPE: List<Order>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * USAGE: Restaurant uses this to manage order processing
     */
    private List<Order> activeOrders;
    
    /**
     * imageUrl - Path to restaurant image file
     * PURPOSE: Image file name for displaying restaurant image
     * TYPE: String
     * VISIBILITY: private
     * NOTE: Kept for backward compatibility with UI
     * PATH: Relative to /images/restaurants/ directory
     * EXAMPLE: "pizza_palace.jpg", "burger_king.png"
     */
    private String imageUrl;
    
    /**
     * Constructor - Creates Restaurant with owner (UML spec)
     * 
     * PURPOSE:
     * Creates new Restaurant with owner, initializes empty lists for menu, reviews, and orders.
     * This is the UML-specified constructor.
     * 
     * UML SPEC: Required constructor
     * 
     * @param id Restaurant ID
     * @param name Restaurant name
     * @param owner Restaurant owner (Merchant)
     */
    public Restaurant(int id, String name, Merchant owner) {
        this.id = id; // Set restaurant ID
        this.name = name; // Set restaurant name
        this.owner = owner; // Set restaurant owner
        this.menu = new ArrayList<>(); // Initialize empty menu list
        this.reviews = new ArrayList<>(); // Initialize empty reviews list
        this.activeOrders = new ArrayList<>(); // Initialize empty active orders list
    }
    
    /**
     * Constructor - Creates Restaurant without owner (backward compatibility)
     * 
     * PURPOSE:
     * Creates Restaurant with image URL for backward compatibility with existing code.
     * Owner can be set later using setOwner().
     * 
     * @param id Restaurant ID
     * @param name Restaurant name
     * @param imageUrl Image file name
     */
    public Restaurant(int id, String name, String imageUrl) {
        this.id = id; // Set restaurant ID
        this.name = name; // Set restaurant name
        this.imageUrl = imageUrl; // Set image URL
        this.menu = new ArrayList<>(); // Initialize empty menu list
        this.reviews = new ArrayList<>(); // Initialize empty reviews list
        this.activeOrders = new ArrayList<>(); // Initialize empty active orders list
    }
    
    /**
     * UML SPEC METHODS:
     * These methods are required by the UML specification
     */
    
    /**
     * getMenu() - Gets menu items list
     * 
     * PURPOSE:
     * Returns a copy of the menu items list.
     * Returns copy to prevent external modification of internal list.
     * 
     * UML SPEC: Required method
     * 
     * @return Copy of menu items list
     */
    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu); // Return copy of menu list
    }
    
    /**
     * receiveOrder() - Receives an order for processing
     * 
     * PURPOSE:
     * Adds an order to the activeOrders list when restaurant receives it.
     * This is the UML-specified method for order reception.
     * 
     * UML SPEC: Required method
     * ORDER PROCESSING: Restaurant uses activeOrders to track orders being processed
     * 
     * RECEPTION FLOW:
     * 1. Validate order is not null
     * 2. Add order to activeOrders list
     * 3. Restaurant can now process the order
     * 
     * @param o The Order object to receive
     */
    public void receiveOrder(Order o) {
        /**
         * VALIDATION: Check if order is valid
         * Only add non-null orders to activeOrders
         */
        if (o != null) {
            activeOrders.add(o); // Add order to active orders list
        }
    }
    
    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getId() - Gets restaurant ID
     * @return Restaurant ID
     */
    public int getId() { 
        return id; // Return restaurant ID
    }
    
    /**
     * getName() - Gets restaurant name
     * @return Restaurant name
     */
    public String getName() { 
        return name; // Return restaurant name
    }
    
    /**
     * getOwner() - Gets restaurant owner
     * @return Merchant who owns this restaurant
     */
    public Merchant getOwner() {
        return owner; // Return restaurant owner
    }
    
    /**
     * getReviews() - Gets reviews list
     * @return Copy of reviews list
     */
    public List<Review> getReviews() {
        return new ArrayList<>(reviews); // Return copy of reviews list
    }
    
    /**
     * getActiveOrders() - Gets active orders list
     * @return Copy of active orders list
     */
    public List<Order> getActiveOrders() {
        return new ArrayList<>(activeOrders); // Return copy of active orders list
    }
    
    /**
     * getImageUrl() - Gets image file name
     * @return Image file name
     */
    public String getImageUrl() { 
        return imageUrl; // Return image URL
    }
    
    /**
     * SETTER METHODS:
     * These methods allow modification of fields
     */
    
    /**
     * setId() - Sets restaurant ID
     * @param id New restaurant ID
     */
    public void setId(int id) { 
        this.id = id; // Set restaurant ID
    }
    
    /**
     * setName() - Sets restaurant name
     * @param name New restaurant name
     */
    public void setName(String name) { 
        this.name = name; // Set restaurant name
    }
    
    /**
     * setOwner() - Sets restaurant owner
     * @param owner New restaurant owner (Merchant)
     */
    public void setOwner(Merchant owner) {
        this.owner = owner; // Set restaurant owner
    }
    
    /**
     * setMenu() - Sets menu items list
     * @param menu New menu items list (creates copy to prevent external modification)
     */
    public void setMenu(List<MenuItem> menu) {
        this.menu = menu != null ? new ArrayList<>(menu) : new ArrayList<>(); // Set menu list (create copy)
    }
    
    /**
     * setReviews() - Sets reviews list
     * @param reviews New reviews list (creates copy to prevent external modification)
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews != null ? new ArrayList<>(reviews) : new ArrayList<>(); // Set reviews list (create copy)
    }
    
    /**
     * setActiveOrders() - Sets active orders list
     * @param activeOrders New active orders list (creates copy to prevent external modification)
     */
    public void setActiveOrders(List<Order> activeOrders) {
        this.activeOrders = activeOrders != null ? new ArrayList<>(activeOrders) : new ArrayList<>(); // Set active orders list (create copy)
    }
    
    /**
     * setImageUrl() - Sets image file name
     * @param imageUrl New image file name
     */
    public void setImageUrl(String imageUrl) { 
        this.imageUrl = imageUrl; // Set image URL
    }
}
