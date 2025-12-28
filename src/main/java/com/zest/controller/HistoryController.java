package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService;
import com.zest.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

/**
 * History Controller
 * Displays order history for the logged-in user
 */
public class HistoryController {
    
    @FXML private ListView<VBox> historyListView;
    @FXML private Label emptyLabel;
    
    private DataService dataService;
    private static String currentUserEmail; // Simple session management
    
    public HistoryController() {
        this.dataService = new DataService();
    }
    
    @FXML
    public void initialize() {
        loadOrderHistory();
    }
    
    /**
     * Set the current logged-in user email
     * Should be called after successful login
     */
    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }
    
    /**
     * Get the current logged-in user email
     */
    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }
    
    /**
     * Load and display order history
     */
    private void loadOrderHistory() {
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            emptyLabel.setText("No user logged in");
            return;
        }
        
        int userId = dataService.getUserIdByEmail(currentUserEmail);
        if (userId == -1) {
            emptyLabel.setText("User not found");
            return;
        }
        
        List<Order> orders = dataService.getOrderHistory(userId);
        
        if (orders.isEmpty()) {
            emptyLabel.setText("No order history found");
            return;
        }
        
        emptyLabel.setText("");
        historyListView.getItems().clear();
        
        for (Order order : orders) {
            VBox orderBox = createOrderCard(order);
            historyListView.getItems().add(orderBox);
        }
    }
    
    /**
     * Create a visual card for an order
     */
    private VBox createOrderCard(Order order) {
        VBox card = new VBox(5);
        card.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");
        
        Label orderIdLabel = new Label("Order #" + order.getId());
        orderIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label priceLabel = new Label("Total: " + String.format("%.2f EGP", order.getTotalPrice()));
        priceLabel.setStyle("-fx-font-size: 12px;");
        
        Label statusLabel = new Label("Status: " + order.getStatus());
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        card.getChildren().addAll(orderIdLabel, priceLabel, statusLabel);
        return card;
    }
    
    @FXML
    private void handleBackToHome() {
        try {
            Main.switchScene("/fxml/Home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load Home.fxml");
        }
    }
}
