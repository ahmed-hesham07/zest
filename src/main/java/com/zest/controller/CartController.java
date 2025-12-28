package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService;
import com.zest.logic.CartManager;
import com.zest.logic.CashPayment;
import com.zest.logic.PaymentStrategy;
import com.zest.model.MenuItem;
import com.zest.model.Order;
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
    
    private DataService dataService;

    public CartController() {
        this.dataService = new DataService();
    }

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
        
        // Get current user ID
        String userEmail = HistoryController.getCurrentUserEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Error");
            alert.setHeaderText(null);
            alert.setContentText("Please log in again.");
            alert.showAndWait();
            try {
                Main.switchScene("/fxml/Login.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        int userId = dataService.getUserIdByEmail(userEmail);
        if (userId == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User not found.");
            alert.showAndWait();
            return;
        }
        
        // Create order
        double totalPrice = cart.getTotalPrice();
        Order order = new Order(userId, totalPrice, "PENDING");
        
        // Process payment using Strategy Pattern
        PaymentStrategy payment = new CashPayment(); // Default to cash payment
        payment.pay(totalPrice);
        
        // Save order to database
        dataService.saveOrder(order);
        
        // Clear cart
        cart.getItems().clear();
        
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been placed successfully!");
        alert.showAndWait();
        
        // Return to home
        try {
            Main.switchScene("/fxml/Home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
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