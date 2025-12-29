package com.zest.controller;

import com.zest.Main;
import com.zest.logic.CartManager;
import com.zest.model.MenuItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class CartController {
    @FXML private VBox cartItemsContainer; // Hamdy's VBox inside the ScrollPane
    @FXML private Label totalPriceLabel; // Hamdy's price label

    @FXML
    public void initialize() {
        // Runs as soon as the FXML is loaded
        refreshCart();
    }
    
    @FXML
    private void handleCheckout() {
        CartManager cart = CartManager.getInstance();
        
        if (cart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Add items before checkout.");
            alert.showAndWait();
            return;
        }
        
        // Navigate to checkout screen
        try {
            Main.switchScene("/fxml/Checkout.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load checkout screen.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleBackToHome() {
        try {
            // Use the engine to switch back to the Menu
            Main.switchScene("/fxml/Home.fxml");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("CRITICAL: Could not load Home.fxml from Cart.");
        }
    }

    /**
     * Clears the list and re-populates it based on current CartManager data
     */
    public void refreshCart() {
        // 1. Wipe the current UI clean
        cartItemsContainer.getChildren().clear();
        
        CartManager cart = CartManager.getInstance();
        
        try {
            // 2. Loop through unique items with their quantities
            for (MenuItem item : cart.getItemQuantities().keySet()) {
                
                // 3. Load cart item card template
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CartItemCard.fxml"));
                HBox card = (HBox) loader.load();

                // 4. Inject the specific food data into that card's controller
                CartItemController itemController = loader.getController();
                itemController.setData(item, this); // Passing 'this' allows the card to call refreshCart()

                // 5. Add the finished card to the container
                cartItemsContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            System.err.println("Architect! The CartItemCard.fxml could not be loaded.");
            e.printStackTrace();
        }
        
        // 6. Update the total price label
        totalPriceLabel.setText(String.format("%.2f EGP", cart.getTotalPrice()));
    }
}