package com.zest.controller;

/**
 * RestaurantSelectionController - Restaurant Selection Controller
 * 
 * PURPOSE:
 * This controller manages the restaurant selection screen where users choose
 * a restaurant before viewing its menu. Displays all available restaurants
 * as cards with images and names.
 * 
 * UML CLASSES USED:
 * - Restaurant: Displays Restaurant objects with id, name, imageUrl
 * 
 * RESTAURANT SELECTION FLOW:
 * 1. User views all available restaurants
 * 2. User clicks "Select" on a restaurant
 * 3. Restaurant ID is stored for menu filtering
 * 4. Cart is cleared (new restaurant = new order)
 * 5. User is redirected to menu screen
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: Restaurant Management & Selection
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.dao.DataService; // Data access layer for fetching restaurants
import com.zest.model.Restaurant; // UML Class: Restaurant model with id, name, imageUrl
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.scene.control.Alert; // Shows popup messages
import javafx.scene.image.Image; // Image class for loading images
import javafx.scene.image.ImageView; // ImageView for displaying images
import javafx.scene.layout.FlowPane; // Container that arranges restaurants in a flow layout
import javafx.scene.layout.VBox; // Vertical container for restaurant cards
import javafx.scene.control.Label; // Text label for restaurant name
import javafx.scene.control.Button; // Button for selecting restaurant
import java.io.IOException; // Exception handling
import java.util.List; // List interface for restaurant collection

/**
 * Controller for restaurant selection screen
 * Allows user to select a restaurant before viewing menu
 */
public class RestaurantSelectionController {
    
    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from RestaurantSelection.fxml when screen loads
     */
    
    /**
     * restaurantContainer - FlowPane container for restaurant cards
     * PURPOSE: Displays all restaurants in a flowing grid layout
     * CONNECTS TO: RestaurantSelection.fxml fx:id="restaurantContainer"
     */
    @FXML
    private FlowPane restaurantContainer;
    
    /**
     * dataService - Data access object for database operations
     * PURPOSE: Fetches restaurants from database
     */
    private DataService dataService = new DataService();
    
    /**
     * selectedRestaurantId - Static variable storing selected restaurant ID
     * PURPOSE: Stores which restaurant user selected (used by HomeController to filter menu)
     * SCOPE: Static so it can be accessed from other controllers (HomeController)
     */
    private static Integer selectedRestaurantId;
    
    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Called automatically when RestaurantSelection.fxml loads.
     * Loads and displays all restaurants from database.
     * 
     * FLOW:
     * 1. JavaFX loads RestaurantSelection.fxml
     * 2. JavaFX creates RestaurantSelectionController instance
     * 3. JavaFX injects @FXML fields
     * 4. JavaFX calls initialize()
     * 5. We load restaurants from database
     */
    @FXML
    public void initialize() {
        loadRestaurants(); // Load and display restaurants
    }
    
    /**
     * loadRestaurants() - Loads and displays all restaurants
     * 
     * PURPOSE:
     * Fetches restaurants from database and displays them as cards.
     * Each restaurant card shows image, name, and select button.
     * 
     * UML CLASSES USED:
     * - Restaurant: Fetches Restaurant objects from database
     * 
     * FLOW:
     * 1. Fetch all restaurants from database
     * 2. Clear existing cards from container
     * 3. For each restaurant, create a card
     * 4. Add card to container
     */
    private void loadRestaurants() {
        /**
         * STEP 1: FETCH RESTAURANTS FROM DATABASE
         * Get all restaurants from database as List<Restaurant>
         */
        List<Restaurant> restaurants = dataService.getAllRestaurants();
        
        /**
         * STEP 2: CLEAR EXISTING CARDS
         * Remove any existing restaurant cards from container
         */
        restaurantContainer.getChildren().clear();
        
        try {
            /**
             * STEP 3: CREATE CARDS FOR EACH RESTAURANT
             * Loop through restaurants and create a card for each
             */
            for (Restaurant restaurant : restaurants) {
                VBox card = createRestaurantCard(restaurant); // Create card for restaurant
                restaurantContainer.getChildren().add(card); // Add card to container
            }
        } catch (Exception e) {
            /**
             * ERROR HANDLING:
             * If error occurs creating cards, log error
             */
            System.err.println("Error loading restaurants:");
            e.printStackTrace();
        }
    }
    
    /**
     * createRestaurantCard() - Creates visual card for a restaurant
     * 
     * PURPOSE:
     * Creates a VBox card displaying restaurant image, name, and select button.
     * 
     * UML CLASSES USED:
     * - Restaurant: Gets restaurant id, name, imageUrl
     * 
     * @param restaurant The Restaurant object to display
     * @return VBox card containing restaurant information
     */
    private VBox createRestaurantCard(Restaurant restaurant) {
        /**
         * CREATE CARD CONTAINER:
         * VBox with padding, border, and background styling
         */
        VBox card = new VBox(10); // Create VBox with 10px spacing
        card.setStyle("-fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-color: white;"); // Card styling
        card.setPrefWidth(200); // Set preferred width
        card.setPrefHeight(250); // Set preferred height
        
        /**
         * CREATE RESTAURANT IMAGE:
         * ImageView displaying restaurant image from resources
         */
        ImageView imageView = new ImageView(); // Create ImageView
        imageView.setFitWidth(170); // Set image width
        imageView.setFitHeight(150); // Set image height
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        
        try {
            /**
             * LOAD RESTAURANT IMAGE:
             * Load image from resources folder based on restaurant's imageUrl
             */
            String imagePath = "/images/restaurants/" + restaurant.getImageUrl(); // Build image path
            Image image = new Image(getClass().getResourceAsStream(imagePath)); // Load image from resources
            imageView.setImage(image); // Set image in ImageView
        } catch (Exception e) {
            /**
             * ERROR HANDLING:
             * If image not found, log error (imageView will be empty)
             */
            System.err.println("Image not found: " + restaurant.getImageUrl());
            // ImageView remains empty (no image displayed)
        }
        
        /**
         * CREATE RESTAURANT NAME LABEL:
         * Label displaying restaurant name
         */
        Label nameLabel = new Label(restaurant.getName()); // Create label with restaurant name
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Bold, large font
        
        /**
         * CREATE SELECT BUTTON:
         * Button that allows user to select this restaurant
         */
        Button selectBtn = new Button("Select"); // Create button
        selectBtn.setStyle("-fx-background-color: #ff9f1c; -fx-text-fill: white; -fx-font-weight: bold;"); // Orange styling
        selectBtn.setOnAction(e -> handleSelectRestaurant(restaurant)); // Set click handler
        
        /**
         * ADD ELEMENTS TO CARD:
         * Add image, name, and button to card container
         */
        card.getChildren().addAll(imageView, nameLabel, selectBtn); // Add to card
        return card; // Return completed card
    }
    
    /**
     * handleSelectRestaurant() - Event handler for restaurant selection
     * 
     * PURPOSE:
     * Handles when user clicks "Select" on a restaurant.
     * Stores restaurant ID, clears cart, and navigates to menu screen.
     * 
     * UML CLASSES USED:
     * - Restaurant: Gets restaurant id to store selection
     * 
     * FLOW:
     * 1. User clicks "Select" button
     * 2. Store restaurant ID in static variable
     * 3. Clear cart (new restaurant = new order)
     * 4. Navigate to menu screen
     */
    private void handleSelectRestaurant(Restaurant restaurant) {
        /**
         * STEP 1: STORE SELECTED RESTAURANT ID
         * Store restaurant ID so HomeController can filter menu items
         */
        selectedRestaurantId = restaurant.getId(); // Store ID in static variable
        
        /**
         * STEP 2: CLEAR CART
         * When selecting new restaurant, clear cart
         * (User can only order from one restaurant at a time)
         */
        com.zest.logic.CartManager.getInstance().clear(); // Clear cart
        
        try {
            /**
             * STEP 3: NAVIGATE TO MENU SCREEN
             * Redirect user to menu screen where they can view menu items
             */
            Main.switchScene("/fxml/Home.fxml"); // Navigate to menu screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error and show error message
             */
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load menu screen.");
            alert.showAndWait();
        }
    }
    
    /**
     * getSelectedRestaurantId() - Gets selected restaurant ID
     * 
     * PURPOSE:
     * Returns the ID of the restaurant user selected.
     * Used by HomeController to filter menu items.
     * 
     * @return Selected restaurant ID or null if no restaurant selected
     */
    public static Integer getSelectedRestaurantId() {
        return selectedRestaurantId; // Return stored restaurant ID
    }
    
    /**
     * clearSelection() - Clears restaurant selection
     * 
     * PURPOSE:
     * Clears selected restaurant ID.
     * Called after order is placed or user logs out.
     */
    public static void clearSelection() {
        selectedRestaurantId = null; // Clear selection
    }
    
    /**
     * handleLogout() - Event handler for logout button
     * 
     * PURPOSE:
     * Clears restaurant selection and cart, then navigates to login screen.
     * 
     * FLOW:
     * 1. User clicks logout button
     * 2. Clear restaurant selection
     * 3. Clear cart
     * 4. Navigate to login screen
     */
    @FXML
    private void handleLogout() {
        clearSelection(); // Clear restaurant selection
        com.zest.logic.CartManager.getInstance().clear(); // Clear cart
        try {
            Main.switchScene("/fxml/Login.fxml"); // Navigate to login screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error
             */
            e.printStackTrace();
        }
    }
}
