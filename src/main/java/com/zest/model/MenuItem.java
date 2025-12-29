package com.zest.model;

/**
 * MenuItem - Menu Item Model Class
 * 
 * PURPOSE:
 * This class represents a menu item (food item) that belongs to a restaurant.
 * It stores information about the item including name, price, availability, and display details.
 * 
 * UML SPECIFICATION:
 * - Fields: name (String), currentPrice (double), isAvailable (boolean)
 * - Method: setPrice(double p)
 * 
 * PRICE MANAGEMENT:
 * - currentPrice: Current price of the item (can change)
 * - Used in OrderItem with Snapshot Pattern to capture price at purchase time
 * 
 * AVAILABILITY:
 * - isAvailable: Indicates if item is currently available for order
 * - Can be set by Merchant to temporarily disable items
 * 
 * RESTAURANT ASSOCIATION:
 * - restaurantId: Links item to its restaurant
 * - Ensures items are grouped by restaurant
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Menu Management & Item Display
 */

/**
 * MenuItem class
 * Represents a food item on a restaurant's menu
 */
public class MenuItem {
    
    /**
     * id - Unique identifier for menu item
     * PURPOSE: Primary key in database, unique identifier
     * TYPE: int
     * VISIBILITY: private
     * DATABASE: Auto-generated primary key
     */
    private int id;
    
    /**
     * restaurantId - ID of restaurant this item belongs to
     * PURPOSE: Links menu item to its restaurant
     * TYPE: int
     * VISIBILITY: private
     * RELATIONSHIP: Foreign key to Restaurant table
     */
    private int restaurantId;
    
    /**
     * name - Name of the menu item
     * PURPOSE: Display name shown to customers
     * TYPE: String
     * VISIBILITY: private
     * UML SPEC: Required field
     * EXAMPLE: "Cheese Burger", "Pizza Margherita"
     */
    private String name;
    
    /**
     * currentPrice - Current price of the menu item
     * PURPOSE: Price shown to customers, can be updated by Merchant
     * TYPE: double
     * VISIBILITY: private
     * UML SPEC: Required field (named currentPrice)
     * CURRENCY: EGP (Egyptian Pounds)
     * NOTE: This is the live price - OrderItem captures priceAtPurchase
     */
    private double currentPrice;
    
    /**
     * isAvailable - Availability status of the menu item
     * PURPOSE: Indicates if item can be ordered (true) or is unavailable (false)
     * TYPE: boolean
     * VISIBILITY: private
     * UML SPEC: Required field
     * DEFAULT: true (available by default)
     * USAGE: Merchant can set to false to temporarily disable items
     */
    private boolean isAvailable;
    
    /**
     * description - Description of the menu item
     * PURPOSE: Additional information about the item (ingredients, etc.)
     * TYPE: String
     * VISIBILITY: private
     * OPTIONAL: Can be null or empty
     * DISPLAY: Shown in item details
     */
    private String description;
    
    /**
     * imageUrl - Path to item image file
     * PURPOSE: Image file name for displaying item image
     * TYPE: String
     * VISIBILITY: private
     * PATH: Relative to /images/menu/ directory
     * EXAMPLE: "burger.jpg", "pizza.png"
     */
    private String imageUrl;

    /**
     * Constructor - Creates MenuItem with default availability
     * 
     * PURPOSE:
     * Creates new MenuItem with all fields. Sets isAvailable to true by default.
     * 
     * @param id Unique identifier for menu item
     * @param restaurantId ID of restaurant this item belongs to
     * @param name Display name of the item
     * @param price Current price of the item
     * @param description Item description (can be null)
     * @param imageUrl Image file name (relative to /images/menu/)
     */
    public MenuItem(int id, int restaurantId, String name, double price, String description, String imageUrl) {
        this.id = id; // Set unique ID
        this.restaurantId = restaurantId; // Set restaurant ID
        this.name = name; // Set item name
        this.currentPrice = price; // Set current price
        this.isAvailable = true; // Default to available
        this.description = description; // Set description
        this.imageUrl = imageUrl; // Set image URL
    }
    
    /**
     * Constructor - Creates MenuItem with specified availability
     * 
     * PURPOSE:
     * Creates new MenuItem with all fields including availability status.
     * Used when loading from database where availability is stored.
     * 
     * @param id Unique identifier for menu item
     * @param restaurantId ID of restaurant this item belongs to
     * @param name Display name of the item
     * @param price Current price of the item
     * @param isAvailable Availability status (true = available, false = unavailable)
     * @param description Item description (can be null)
     * @param imageUrl Image file name (relative to /images/menu/)
     */
    public MenuItem(int id, int restaurantId, String name, double price, boolean isAvailable, String description, String imageUrl) {
        this.id = id; // Set unique ID
        this.restaurantId = restaurantId; // Set restaurant ID
        this.name = name; // Set item name
        this.currentPrice = price; // Set current price
        this.isAvailable = isAvailable; // Set availability status
        this.description = description; // Set description
        this.imageUrl = imageUrl; // Set image URL
    }

    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getId() - Gets menu item ID
     * 
     * PURPOSE:
     * Returns the unique identifier for this menu item.
     * 
     * @return Menu item ID
     */
    public int getId() { 
        return id; // Return ID
    }
    
    /**
     * getRestaurantId() - Gets restaurant ID
     * 
     * PURPOSE:
     * Returns the ID of the restaurant this item belongs to.
     * Used to filter items by restaurant.
     * 
     * @return Restaurant ID
     */
    public int getRestaurantId() { 
        return restaurantId; // Return restaurant ID
    }
    
    /**
     * getName() - Gets menu item name
     * 
     * PURPOSE:
     * Returns the display name of this menu item.
     * 
     * UML SPEC: Required getter
     * 
     * @return Item name
     */
    public String getName() { 
        return name; // Return name
    }
    
    /**
     * getPrice() - Gets current price (backward compatibility)
     * 
     * PURPOSE:
     * Returns the current price of this menu item.
     * This method is kept for backward compatibility.
     * 
     * @return Current price
     */
    public double getPrice() { 
        return currentPrice; // Return current price
    }
    
    /**
     * getCurrentPrice() - Gets current price (UML spec method)
     * 
     * PURPOSE:
     * Returns the current price of this menu item.
     * This is the UML-specified method name.
     * 
     * UML SPEC: Required getter
     * 
     * @return Current price
     */
    public double getCurrentPrice() { 
        return currentPrice; // Return current price
    }
    
    /**
     * isAvailable() - Gets availability status
     * 
     * PURPOSE:
     * Returns whether this menu item is currently available for order.
     * 
     * UML SPEC: Required getter
     * 
     * @return true if available, false if unavailable
     */
    public boolean isAvailable() { 
        return isAvailable; // Return availability status
    }
    
    /**
     * getDescription() - Gets item description
     * 
     * PURPOSE:
     * Returns the description of this menu item.
     * 
     * @return Item description (can be null)
     */
    public String getDescription() { 
        return description; // Return description
    }
    
    /**
     * getImageUrl() - Gets image file name
     * 
     * PURPOSE:
     * Returns the image file name for this menu item.
     * 
     * @return Image file name (relative to /images/menu/)
     */
    public String getImageUrl() { 
        return imageUrl; // Return image URL
    }
    
    /**
     * UML SPEC METHOD:
     * This method is required by UML specification
     */
    
    /**
     * setPrice() - Sets current price of menu item
     * 
     * PURPOSE:
     * Updates the current price of this menu item.
     * Used by Merchant to update prices.
     * 
     * UML SPEC: Required method
     * PRICE UPDATE: Changes currentPrice, not priceAtPurchase in OrderItems
     * 
     * @param p New price for the menu item
     */
    public void setPrice(double p) {
        this.currentPrice = p; // Update current price
    }
}
