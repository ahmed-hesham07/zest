package com.zest.controller;

/**
 * ItemCardController - Menu Item Card Controller
 * 
 * PURPOSE:
 * This controller manages individual menu item cards displayed on the menu screen.
 * Each card shows item image, name, price, and an "Add to Cart" button.
 * 
 * UML CLASSES USED:
 * - MenuItem: Displays MenuItem with name, currentPrice, imageUrl
 * 
 * CART INTEGRATION:
 * - Uses CartManager to add items to cart
 * - Validates that items from same restaurant are added together
 * - Shows success message when item added
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Customer Experience & Menu Display
 */

import com.zest.model.MenuItem; // UML Class: Menu item model
import com.zest.logic.CartManager; // Singleton cart manager
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.scene.control.Alert; // Shows popup messages
import javafx.scene.control.Label; // Text labels
import javafx.scene.image.Image; // Image class for loading images
import javafx.scene.image.ImageView; // ImageView for displaying images

/**
 * Controller for menu item card
 * Displays individual menu item and handles adding to cart
 */
public class ItemCardController {

    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from ItemCard.fxml when card loads
     */
    
    /**
     * itemImage - ImageView displaying menu item image
     * PURPOSE: Shows food item image
     * CONNECTS TO: ItemCard.fxml fx:id="itemImage"
     */
    @FXML
    private ImageView itemImage;
    
    /**
     * nameLabel - Label displaying menu item name
     * PURPOSE: Shows food item name
     * CONNECTS TO: ItemCard.fxml fx:id="nameLabel"
     */
    @FXML
    private Label nameLabel;
    
    /**
     * priceLabel - Label displaying menu item price
     * PURPOSE: Shows food item price
     * CONNECTS TO: ItemCard.fxml fx:id="priceLabel"
     */
    @FXML
    private Label priceLabel;
    
    /**
     * finalPriceLabel - Label displaying final price
     * PURPOSE: Shows total price (same as priceLabel, kept for consistency)
     * CONNECTS TO: ItemCard.fxml fx:id="finalPriceLabel"
     */
    @FXML
    private Label finalPriceLabel;
    
    /**
     * currentItem - MenuItem object being displayed
     * PURPOSE: Stores the MenuItem data for this card
     */
    private MenuItem currentItem;
    
    /**
     * cartManager - Singleton cart manager instance
     * PURPOSE: Manages adding items to cart
     */
    private CartManager cartManager = CartManager.getInstance();

    /**
     * setData() - Sets menu item data for this card
     * 
     * PURPOSE:
     * Called by HomeController to populate card with menu item data.
     * Sets item name, price, and loads image.
     * 
     * UML CLASSES USED:
     * - MenuItem: Gets item name, price, imageUrl
     * 
     * @param item The MenuItem object to display
     */
    public void setData(MenuItem item) {
        this.currentItem = item; // Store MenuItem reference
        updateDisplay(); // Update labels with item data

        /**
         * LOAD ITEM IMAGE:
         * Load image from resources folder based on item's imageUrl
         */
        try {
            String imagePath = "/images/menu/" + item.getImageUrl(); // Build image path
            // Check if resource exists before creating Image to avoid NPE
            if (getClass().getResource(imagePath) != null) {
                Image image = new Image(getClass().getResourceAsStream(imagePath)); // Load image
                itemImage.setImage(image); // Set image in ImageView
                itemImage.setFitWidth(200); // Set image width
                itemImage.setFitHeight(150); // Set image height
                itemImage.setPreserveRatio(true); // Maintain aspect ratio
            } else {
                /**
                 * ERROR HANDLING:
                 * If image not found, log error (imageView will be empty)
                 */
                System.err.println("Image missing: " + imagePath);
                // ImageView remains empty (no image displayed)
            }
        } catch (Exception e) {
            /**
             * ERROR HANDLING:
             * If error loading image, log error
             */
            System.err.println("Error loading image for " + item.getName());
            e.printStackTrace();
        }
    }
    
    /**
     * updateDisplay() - Updates card display with item data
     * 
     * PURPOSE:
     * Updates labels to show item name and price.
     * Called when item data is set.
     * 
     * UML CLASSES USED:
     * - MenuItem: Gets item name and price
     */
    private void updateDisplay() {
        if (currentItem == null) return; // Exit if no item set
        
        /**
         * UPDATE LABELS:
         * Set name and price labels with item data
         */
        nameLabel.setText(currentItem.getName()); // Set item name
        double price = currentItem.getPrice(); // Get item price
        priceLabel.setText(String.format("%.2f EGP", price)); // Format and set price
        
        /**
         * UPDATE FINAL PRICE LABEL:
         * Set final price (same as price for single item)
         */
        finalPriceLabel.setText(String.format("Total: %.2f EGP", price)); // Format and set final price
    }
    
    /**
     * handleAddToCart() - Event handler for "Add to Cart" button
     * 
     * PURPOSE:
     * Validates item can be added to cart and adds it.
     * Shows success message when item added.
     * 
     * UML CLASSES USED:
     * - MenuItem: Adds MenuItem to cart
     * 
     * VALIDATION:
     * - Checks if cart is empty or contains items from same restaurant
     * - If cart has items from different restaurant, shows warning
     * 
     * FLOW:
     * 1. User clicks "Add to Cart" button
     * 2. Validate item can be added (same restaurant check)
     * 3. Add item to cart using CartManager
     * 4. Show success message
     */
    @FXML
    private void handleAddToCart() {
        /**
         * VALIDATION: Check if item is set
         * If no item, exit early
         */
        if (currentItem == null) {
            return; // Exit if no item
        }
        
        /**
         * VALIDATION: Check if item can be added to cart
         * CartManager.canAddItem() checks if:
         * - Cart is empty (can add any item)
         * - Cart has items from same restaurant (can add)
         * - Cart has items from different restaurant (cannot add)
         */
        if (!cartManager.canAddItem(currentItem)) {
            /**
             * ERROR: Cart has items from different restaurant
             * Show warning and don't add item
             */
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Different Restaurant");
            alert.setHeaderText(null);
            alert.setContentText("Your cart contains items from a different restaurant. Please complete or clear your current order first.");
            alert.showAndWait(); // Show warning popup
            return; // Exit method early
        }
        
        /**
         * ADD ITEM TO CART:
         * Use CartManager to add item to cart
         * Returns true if added successfully
         */
        boolean added = cartManager.addItem(currentItem);
        if (added) {
            /**
             * SUCCESS: Item added to cart
             * Show success message to user
             */
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added to Cart");
            alert.setHeaderText(null);
            alert.setContentText(currentItem.getName() + " added to cart!");
            alert.showAndWait(); // Show success popup
        }
    }
}
