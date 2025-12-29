package com.zest.controller;

/**
 * CartController - Shopping Cart Display Controller
 * 
 * PURPOSE:
 * This controller manages the shopping cart screen where users can view
 * items in their cart, modify quantities, remove items, and proceed to checkout.
 * 
 * UML CLASSES USED:
 * - MenuItem: Displays menu items in cart
 * 
 * CART MANAGEMENT:
 * - Uses CartManager singleton to manage cart state
 * - Displays items with quantities and prices
 * - Allows quantity increase/decrease and item removal
 * - Shows total price
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Customer Experience & Cart Management
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.logic.CartManager; // Singleton cart manager
import com.zest.model.MenuItem; // UML Class: Menu item model
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.fxml.FXMLLoader; // Loads FXML files dynamically
import javafx.scene.control.Alert; // Shows popup messages
import javafx.scene.control.Label; // Text label for total price
import javafx.scene.layout.VBox; // Vertical container for cart items
import javafx.scene.layout.HBox; // Horizontal container for cart item cards
import java.io.IOException; // Exception handling

/**
 * Controller for cart screen
 * Displays cart items and handles cart operations
 */
public class CartController {
    
    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from Cart.fxml when screen loads
     */
    
    /**
     * cartItemsContainer - VBox container for cart item cards
     * PURPOSE: Displays all items in cart as individual cards
     * CONNECTS TO: Cart.fxml fx:id="cartItemsContainer"
     */
    @FXML private VBox cartItemsContainer;
    
    /**
     * totalPriceLabel - Label showing total cart price
     * PURPOSE: Displays total price of all items in cart
     * CONNECTS TO: Cart.fxml fx:id="totalPriceLabel"
     */
    @FXML private Label totalPriceLabel;

    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Called automatically when Cart.fxml loads.
     * Refreshes cart display to show current cart contents.
     * 
     * FLOW:
     * 1. JavaFX loads Cart.fxml
     * 2. JavaFX creates CartController instance
     * 3. JavaFX injects @FXML fields
     * 4. JavaFX calls initialize()
     * 5. We refresh cart display
     */
    @FXML
    public void initialize() {
        refreshCart(); // Load and display cart items
    }
    
    /**
     * handleCheckout() - Event handler for checkout button click
     * 
     * PURPOSE:
     * Validates cart is not empty and navigates to checkout screen.
     * 
     * VALIDATION:
     * - Checks if cart has items
     * - If empty, shows warning and stays on cart screen
     * - If not empty, navigates to checkout screen
     * 
     * FLOW:
     * 1. User clicks checkout button
     * 2. Check if cart is empty
     * 3. If empty, show warning
     * 4. If not empty, navigate to checkout
     */
    @FXML
    private void handleCheckout() {
        CartManager cart = CartManager.getInstance(); // Get singleton cart instance
        
        /**
         * VALIDATION: Check if cart is empty
         * If cart is empty, show warning and don't proceed to checkout
         */
        if (cart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Add items before checkout.");
            alert.showAndWait(); // Show warning popup
            return; // Exit method early
        }
        
        /**
         * NAVIGATE TO CHECKOUT SCREEN:
         * Cart has items, proceed to checkout screen
         */
        try {
            Main.switchScene("/fxml/Checkout.fxml"); // Navigate to checkout screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error and show error message
             */
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load checkout screen.");
            alert.showAndWait();
        }
    }
    
    /**
     * handleBackToHome() - Event handler for back button click
     * 
     * PURPOSE:
     * Returns user to menu/home screen to continue shopping.
     * 
     * FLOW:
     * 1. User clicks back button
     * 2. Navigate to Home.fxml screen
     */
    @FXML
    private void handleBackToHome() {
        try {
            Main.switchScene("/fxml/Home.fxml"); // Navigate to menu screen
        } catch (java.io.IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error
             */
            e.printStackTrace();
            System.err.println("CRITICAL: Could not load Home.fxml from Cart.");
        }
    }

    /**
     * refreshCart() - Refreshes cart display
     * 
     * PURPOSE:
     * Clears current display and re-populates it with current cart items.
     * This method is called:
     * - When screen loads (initialize)
     * - When items are added/removed/modified (from CartItemController)
     * 
     * UML CLASSES USED:
     * - MenuItem: Each cart item is a MenuItem
     * 
     * FLOW:
     * 1. Clear existing items from container
     * 2. Get cart items from CartManager
     * 3. For each unique item, create a cart item card
     * 4. Load CartItemCard.fxml for each item
     * 5. Set item data in card controller
     * 6. Add card to container
     * 7. Update total price label
     */
    public void refreshCart() {
        /**
         * STEP 1: CLEAR EXISTING DISPLAY
         * Remove all current cart item cards from container
         */
        cartItemsContainer.getChildren().clear();
        
        CartManager cart = CartManager.getInstance(); // Get singleton cart instance
        
        try {
            /**
             * STEP 2: LOOP THROUGH CART ITEMS
             * CartManager stores items as Map<MenuItem, Integer> where:
             * - Key: MenuItem object
             * - Value: Quantity of that item
             * 
             * We iterate through unique items (keys) to create cards
             */
            for (MenuItem item : cart.getItemQuantities().keySet()) {
                
                /**
                 * STEP 3: LOAD CART ITEM CARD TEMPLATE
                 * Load CartItemCard.fxml which contains the UI for a single cart item
                 * This creates an HBox container with item image, name, price, quantity controls
                 */
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CartItemCard.fxml"));
                HBox card = (HBox) loader.load(); // Load FXML and create HBox

                /**
                 * STEP 4: INJECT ITEM DATA INTO CARD CONTROLLER
                 * Get controller from loaded FXML and set item data
                 * Passing 'this' allows the card controller to call refreshCart() when items change
                 */
                CartItemController itemController = loader.getController();
                itemController.setData(item, this); // Set item data and parent controller reference

                /**
                 * STEP 5: ADD CARD TO CONTAINER
                 * Add the completed cart item card to the VBox container
                 * Cards are displayed vertically in the container
                 */
                cartItemsContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            /**
             * ERROR HANDLING:
             * If CartItemCard.fxml cannot be loaded, log error
             */
            System.err.println("Architect! The CartItemCard.fxml could not be loaded.");
            e.printStackTrace();
        }
        
        /**
         * STEP 6: UPDATE TOTAL PRICE LABEL
         * Calculate and display total price of all items in cart
         */
        totalPriceLabel.setText(String.format("%.2f EGP", cart.getTotalPrice()));
    }
}
