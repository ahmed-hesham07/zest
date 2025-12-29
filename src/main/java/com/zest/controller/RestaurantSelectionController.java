package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService;
import com.zest.model.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.List;

/**
 * Restaurant Selection Controller
 * Allows user to select a restaurant before viewing menu
 */
public class RestaurantSelectionController {
    
    @FXML
    private FlowPane restaurantContainer;
    
    private DataService dataService = new DataService();
    private static Integer selectedRestaurantId; // Store selected restaurant for HomeController
    
    @FXML
    public void initialize() {
        loadRestaurants();
    }
    
    /**
     * Load all restaurants and display them
     */
    private void loadRestaurants() {
        List<Restaurant> restaurants = dataService.getAllRestaurants();
        restaurantContainer.getChildren().clear();
        
        try {
            for (Restaurant restaurant : restaurants) {
                VBox card = createRestaurantCard(restaurant);
                restaurantContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            System.err.println("Error loading restaurants:");
            e.printStackTrace();
        }
    }
    
    /**
     * Create a visual card for a restaurant
     */
    private VBox createRestaurantCard(Restaurant restaurant) {
        VBox card = new VBox(10);
        card.setStyle("-fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-color: white;");
        card.setPrefWidth(200);
        card.setPrefHeight(250);
        
        // Restaurant Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(170);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        
        try {
            String imagePath = "/images/restaurants/" + restaurant.getImageUrl();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Image not found: " + restaurant.getImageUrl());
            // Set default image or placeholder
        }
        
        // Restaurant Name
        Label nameLabel = new Label(restaurant.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Select Button
        Button selectBtn = new Button("Select");
        selectBtn.setStyle("-fx-background-color: #ff9f1c; -fx-text-fill: white; -fx-font-weight: bold;");
        selectBtn.setOnAction(e -> handleSelectRestaurant(restaurant));
        
        card.getChildren().addAll(imageView, nameLabel, selectBtn);
        return card;
    }
    
    /**
     * Handle restaurant selection
     */
    private void handleSelectRestaurant(Restaurant restaurant) {
        selectedRestaurantId = restaurant.getId();
        
        // Clear cart when selecting new restaurant
        com.zest.logic.CartManager.getInstance().clear();
        
        try {
            // Navigate to menu screen
            Main.switchScene("/fxml/Home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load menu screen.");
            alert.showAndWait();
        }
    }
    
    /**
     * Get the selected restaurant ID
     * @return Selected restaurant ID or null
     */
    public static Integer getSelectedRestaurantId() {
        return selectedRestaurantId;
    }
    
    /**
     * Clear restaurant selection (for logout or cart checkout)
     */
    public static void clearSelection() {
        selectedRestaurantId = null;
    }
    
    @FXML
    private void handleLogout() {
        clearSelection();
        com.zest.logic.CartManager.getInstance().clear();
        try {
            Main.switchScene("/fxml/Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

