package com.zest.controller;

import com.zest.dao.DataService;
import com.zest.model.MenuItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class HomeController {

    @FXML
    private FlowPane menuContainer; // Connects to Hamdy's fx:id

    private DataService dataService = new DataService();

    @FXML
    public void initialize() {
        // Automatically runs when the window opens
        loadMenu();
    }
    
    @FXML
    private void handleViewCart() {
        try {
            // Use the new Navigator to go to Hamdy's Cart
            com.zest.Main.switchScene("/fxml/Cart.fxml");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBackToRestaurants() {
        try {
            com.zest.Main.switchScene("/fxml/RestaurantSelection.fxml");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() {
        // Get selected restaurant ID
        Integer restaurantId = RestaurantSelectionController.getSelectedRestaurantId();
        
        if (restaurantId == null) {
            // No restaurant selected, go back to selection
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Restaurant Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a restaurant first.");
            alert.showAndWait();
            handleBackToRestaurants();
            return;
        }
        
        // Load menu items for selected restaurant only
        List<MenuItem> items = dataService.getMenuItemsByRestaurant(restaurantId);
        System.out.println("HomeController received " + items.size() + " items for restaurant " + restaurantId);

        menuContainer.getChildren().clear();
        
        try {
            for (MenuItem item : items) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemCard.fxml"));
                VBox card = loader.load();

                ItemCardController cardController = loader.getController();
                cardController.setData(item);

                menuContainer.getChildren().add(card);
                System.out.println("Added card for " + item.getName() + " to FlowPane.");
            }
        } catch (Exception e) {
            System.err.println("Error loading ItemCard.fxml:");
            e.printStackTrace();
        }
    }
}