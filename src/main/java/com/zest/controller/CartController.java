package com.zest.controller;

import com.zest.logic.CartManager;
import com.zest.model.MenuItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class CartController {
    @FXML private VBox cartItemsContainer; // Hamdy's VBox inside the ScrollPane
    @FXML private Label totalPriceLabel; // Hamdy's price label

    @FXML
    public void initialize() {
        // Runs as soon as the FXML is loaded
        refreshCart();
    }
    @FXML
    private void handleBackToHome() {
        try {
            // Use the engine to switch back to the Menu
            com.zest.Main.switchScene("/fxml/Home.fxml");
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
        
        try {
            // 2. Loop through every item in the user's cart
            for (MenuItem item : CartManager.getInstance().getItems()) {
                
                // 3. Load Hamdy's thin horizontal card template
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
        totalPriceLabel.setText(String.format("%.2f EGP", CartManager.getInstance().getTotalPrice()));
    }
}