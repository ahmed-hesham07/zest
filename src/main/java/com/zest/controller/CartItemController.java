package com.zest.controller;

/**
 * CartItemController - Cart Item Card Controller
 * 
 * PURPOSE:
 * This controller manages individual cart item cards displayed in the cart screen.
 * Each card shows item image, name, price, quantity, and controls to modify quantity.
 * 
 * UML CLASSES USED:
 * - MenuItem: Displays MenuItem with name, price, imageUrl
 * 
 * CART OPERATIONS:
 * - Increase quantity: Adds one more of this item
 * - Decrease quantity: Removes one (if quantity > 1) or removes item (if quantity = 1)
 * - Remove item: Completely removes item from cart
 * - Updates parent cart display after changes
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Customer Experience & Cart Management
 */

import com.zest.model.MenuItem; // UML Class: Menu item model
import com.zest.logic.CartManager; // Singleton cart manager
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.scene.control.Label; // Text labels
import javafx.scene.control.Button; // Buttons for quantity controls
import javafx.scene.image.Image; // Image class for loading images
import javafx.scene.image.ImageView; // ImageView for displaying images

/**
 * Controller for cart item card
 * Displays cart item and handles quantity modifications
 */
public class CartItemController {
    
    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from CartItemCard.fxml when card loads
     */
    
    /**
     * itemImage - ImageView displaying menu item image
     * PURPOSE: Shows food item image
     * CONNECTS TO: CartItemCard.fxml fx:id="itemImage"
     */
    @FXML private ImageView itemImage;
    
    /**
     * nameLabel - Label displaying menu item name
     * PURPOSE: Shows food item name
     * CONNECTS TO: CartItemCard.fxml fx:id="nameLabel"
     */
    @FXML private Label nameLabel;
    
    /**
     * priceLabel - Label displaying total price for this item (price * quantity)
     * PURPOSE: Shows total price for this item
     * CONNECTS TO: CartItemCard.fxml fx:id="priceLabel"
     */
    @FXML private Label priceLabel;
    
    /**
     * quantityLabel - Label displaying current quantity
     * PURPOSE: Shows how many of this item are in cart
     * CONNECTS TO: CartItemCard.fxml fx:id="quantityLabel"
     */
    @FXML private Label quantityLabel;
    
    /**
     * decreaseBtn - Button to decrease quantity
     * PURPOSE: Removes one of this item from cart
     * CONNECTS TO: CartItemCard.fxml fx:id="decreaseBtn"
     */
    @FXML private Button decreaseBtn;
    
    /**
     * increaseBtn - Button to increase quantity
     * PURPOSE: Adds one more of this item to cart
     * CONNECTS TO: CartItemCard.fxml fx:id="increaseBtn"
     */
    @FXML private Button increaseBtn;
    
    /**
     * removeBtn - Button to remove item completely
     * PURPOSE: Removes all of this item from cart
     * CONNECTS TO: CartItemCard.fxml fx:id="removeBtn"
     */
    @FXML private Button removeBtn;

    /**
     * item - MenuItem object being displayed
     * PURPOSE: Stores the MenuItem data for this card
     */
    private MenuItem item;
    
    /**
     * parentController - Reference to CartController
     * PURPOSE: Allows calling refreshCart() after quantity changes
     */
    private CartController parentController;
    
    /**
     * cartManager - Singleton cart manager instance
     * PURPOSE: Manages cart operations (add, remove, set quantity)
     */
    private CartManager cartManager = CartManager.getInstance();

    /**
     * setData() - Sets cart item data for this card
     * 
     * PURPOSE:
     * Called by CartController to populate card with menu item data.
     * Sets item name, price, quantity, and loads image.
     * 
     * UML CLASSES USED:
     * - MenuItem: Gets item name, price, imageUrl
     * 
     * @param item The MenuItem object to display
     * @param parent Reference to CartController for refreshing display
     */
    public void setData(MenuItem item, CartController parent) {
        this.item = item; // Store MenuItem reference
        this.parentController = parent; // Store parent controller reference
        updateDisplay(); // Update labels with item data
        
        /**
         * LOAD ITEM IMAGE:
         * Load image from resources folder based on item's imageUrl
         */
        try {
            String path = "/images/menu/" + item.getImageUrl(); // Build image path
            Image image = new Image(getClass().getResourceAsStream(path)); // Load image
            itemImage.setImage(image); // Set image in ImageView
            itemImage.setFitWidth(80); // Set image width (smaller than menu card)
            itemImage.setFitHeight(60); // Set image height
            itemImage.setPreserveRatio(true); // Maintain aspect ratio
        } catch (Exception e) {
            /**
             * ERROR HANDLING:
             * If image not found, log error (imageView will be empty)
             */
            System.err.println("Cart image not found: " + item.getName());
        }
    }
    
    /**
     * updateDisplay() - Updates card display with current cart data
     * 
     * PURPOSE:
     * Updates labels to show current quantity and total price.
     * Called when quantity changes or card is initialized.
     * 
     * UML CLASSES USED:
     * - MenuItem: Gets item name and price
     * 
     * CALCULATION:
     * - Gets quantity from CartManager
     * - Calculates total price = item price * quantity
     */
    private void updateDisplay() {
        /**
         * GET CURRENT QUANTITY AND PRICE:
         * Get quantity from cart and calculate total price
         */
        int quantity = cartManager.getItemQuantity(item); // Get quantity from cart
        double itemPrice = item.getPrice(); // Get item price
        double totalPrice = itemPrice * quantity; // Calculate total price
        
        /**
         * UPDATE LABELS:
         * Set name, price, and quantity labels
         */
        nameLabel.setText(item.getName()); // Set item name
        priceLabel.setText(String.format("%.2f EGP", totalPrice)); // Format and set total price
        quantityLabel.setText(String.valueOf(quantity)); // Set quantity as string
    }

    /**
     * handleIncrease() - Event handler for increase quantity button
     * 
     * PURPOSE:
     * Adds one more of this item to cart and refreshes display.
     * 
     * FLOW:
     * 1. User clicks increase button (+)
     * 2. Add one more item to cart
     * 3. Update display
     * 4. Refresh parent cart display
     */
    @FXML
    private void handleIncrease() {
        cartManager.addItem(item); // Add one more item to cart
        updateDisplay(); // Update this card's display
        parentController.refreshCart(); // Refresh entire cart display (updates total)
    }
    
    /**
     * handleDecrease() - Event handler for decrease quantity button
     * 
     * PURPOSE:
     * Removes one of this item from cart.
     * If quantity is 1, removes item completely.
     * 
     * FLOW:
     * 1. User clicks decrease button (-)
     * 2. Get current quantity
     * 3. If quantity > 1, decrease quantity by 1
     * 4. If quantity = 1, remove item completely
     * 5. Update display
     * 6. Refresh parent cart display
     */
    @FXML
    private void handleDecrease() {
        int currentQuantity = cartManager.getItemQuantity(item); // Get current quantity
        
        if (currentQuantity > 1) {
            // Quantity > 1: Decrease quantity by 1
            cartManager.setItemQuantity(item, currentQuantity - 1);
        } else {
            // Quantity = 1: Remove item completely
            cartManager.removeItem(item);
        }
        
        updateDisplay(); // Update this card's display
        parentController.refreshCart(); // Refresh entire cart display (updates total)
    }

    /**
     * handleRemove() - Event handler for remove item button
     * 
     * PURPOSE:
     * Completely removes this item from cart regardless of quantity.
     * 
     * FLOW:
     * 1. User clicks remove button
     * 2. Remove item from cart
     * 3. Refresh parent cart display
     */
    @FXML
    private void handleRemove() {
        cartManager.removeItem(item); // Remove item from cart
        parentController.refreshCart(); // Refresh entire cart display (card will be removed)
    }
}
