package com.zest.controller;

/**
 * HomeController - Menu Display Controller
 * 
 * PURPOSE:
 * This controller manages the menu display screen where users can browse menu items
 * from a selected restaurant. It displays menu items as cards and allows navigation
 * to cart and order history.
 * 
 * UML CLASSES USED:
 * - MenuItem: Displays menu items from the selected restaurant
 * - Restaurant: Gets selected restaurant ID to filter menu items
 * 
 * TEAM MEMBER: Hamdy Ashraf (The Chef)
 * RESPONSIBILITY: Customer Experience & Menu Specialist
 */

import com.zest.dao.DataService; // Data access layer for database operations
import com.zest.model.MenuItem; // UML Class: Represents a menu item with name, currentPrice, isAvailable
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.fxml.FXMLLoader; // Loads FXML files dynamically
import javafx.scene.control.Alert; // Shows popup messages to user
import javafx.scene.layout.FlowPane; // Container that arranges items in a flow layout
import javafx.scene.layout.VBox; // Vertical box layout for menu item cards
import java.util.List; // Java List interface for collections

/**
 * Controller class for the Home/Menu screen
 * This screen shows all menu items from the selected restaurant
 */
public class HomeController {

    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated by JavaFX when the FXML file loads.
     * The @FXML annotation tells JavaFX to inject the UI elements from Home.fxml
     */
    
    /**
     * menuContainer - FlowPane container for menu item cards
     * PURPOSE: Displays all menu items in a flowing grid layout
     * CONNECTS TO: Home.fxml fx:id="menuContainer"
     */
    @FXML
    private FlowPane menuContainer;
    
    /**
     * searchField - Text field for searching menu items
     * PURPOSE: Allows users to search/filter menu items (future feature)
     * CONNECTS TO: Home.fxml fx:id="searchField"
     */
    @FXML
    private javafx.scene.control.TextField searchField;
    
    /**
     * historyBtn - Button to view order history
     * PURPOSE: Navigates to the order history screen
     * CONNECTS TO: Home.fxml fx:id="historyBtn"
     */
    @FXML
    private javafx.scene.control.Button historyBtn;
    
    /**
     * cartBtn - Button to view shopping cart
     * PURPOSE: Navigates to the cart screen
     * CONNECTS TO: Home.fxml fx:id="cartBtn"
     */
    @FXML
    private javafx.scene.control.Button cartBtn;

    /**
     * dataService - Data access object for database operations
     * PURPOSE: Handles all database queries (getting menu items, restaurants, etc.)
     * PATTERN: Single instance created per controller
     */
    private DataService dataService = new DataService();

    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Automatically called by JavaFX when the FXML file is loaded.
     * This is where we set up the initial state of the screen.
     * 
     * FLOW:
     * 1. JavaFX loads Home.fxml
     * 2. JavaFX creates HomeController instance
     * 3. JavaFX injects @FXML fields from FXML
     * 4. JavaFX calls initialize() automatically
     * 5. We load menu items from database
     */
    @FXML
    public void initialize() {
        loadMenu(); // Load and display menu items for selected restaurant
    }
    
    /**
     * handleViewCart() - Event handler for cart button click
     * 
     * PURPOSE:
     * Navigates the user to the shopping cart screen when they click the cart button.
     * 
     * FLOW:
     * 1. User clicks cart button in UI
     * 2. JavaFX calls this method
     * 3. We switch to Cart.fxml screen
     * 
     * ERROR HANDLING:
     * If navigation fails, prints error to console (user won't see error)
     */
    @FXML
    private void handleViewCart() {
        try {
            // Switch to cart screen using Main's scene switcher
            com.zest.Main.switchScene("/fxml/Cart.fxml");
        } catch (java.io.IOException e) {
            // Log error if navigation fails
            e.printStackTrace();
        }
    }
    
    /**
     * handleBackToRestaurants() - Event handler for back button
     * 
     * PURPOSE:
     * Returns user to restaurant selection screen to choose a different restaurant.
     * 
     * FLOW:
     * 1. User clicks back button
     * 2. Navigate to RestaurantSelection.fxml
     */
    @FXML
    private void handleBackToRestaurants() {
        try {
            // Switch back to restaurant selection screen
            com.zest.Main.switchScene("/fxml/RestaurantSelection.fxml");
        } catch (java.io.IOException e) {
            // Log error if navigation fails
            e.printStackTrace();
        }
    }
    
    /**
     * handleViewHistory() - Event handler for history button click
     * 
     * PURPOSE:
     * Navigates user to order history screen to view past orders.
     * 
     * FLOW:
     * 1. User clicks history button
     * 2. Navigate to History.fxml screen
     */
    @FXML
    private void handleViewHistory() {
        try {
            // Switch to order history screen
            com.zest.Main.switchScene("/fxml/History.fxml");
        } catch (java.io.IOException e) {
            // Log error if navigation fails
            e.printStackTrace();
        }
    }

    /**
     * loadMenu() - Loads and displays menu items for selected restaurant
     * 
     * PURPOSE:
     * Fetches menu items from database for the selected restaurant and displays
     * them as cards in the menuContainer FlowPane.
     * 
     * UML CLASSES USED:
     * - MenuItem: Each menu item displayed uses the MenuItem class
     * - Restaurant: Gets selected restaurant ID to filter menu items
     * 
     * MULTI-THREADING:
     * Uses background thread to load data without blocking UI thread.
     * This prevents the screen from freezing while loading menu items.
     * 
     * FLOW:
     * 1. Get selected restaurant ID from RestaurantSelectionController
     * 2. If no restaurant selected, show warning and go back to restaurant selection
     * 3. Create background thread to load menu items from database
     * 4. Update UI on JavaFX thread (Platform.runLater)
     * 5. For each menu item, load ItemCard.fxml and display it
     */
    private void loadMenu() {
        // Get selected restaurant ID from RestaurantSelectionController
        // This ID was set when user selected a restaurant
        Integer restaurantId = RestaurantSelectionController.getSelectedRestaurantId();
        
        // VALIDATION: Check if restaurant was selected
        if (restaurantId == null) {
            // No restaurant selected - show warning and redirect
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Restaurant Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a restaurant first.");
            alert.showAndWait(); // Show popup and wait for user to click OK
            
            // Navigate back to restaurant selection screen
            handleBackToRestaurants();
            return; // Exit method early
        }
        
        /**
         * MULTI-THREADING PATTERN:
         * Load data in background thread to avoid blocking UI thread.
         * If we loaded data directly here, the screen would freeze.
         */
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                /**
                 * DATABASE OPERATION:
                 * Get menu items for selected restaurant from database.
                 * This runs in background thread so UI doesn't freeze.
                 */
                List<MenuItem> items = dataService.getMenuItemsByRestaurant(restaurantId);
                System.out.println("HomeController received " + items.size() + " items for restaurant " + restaurantId);

                /**
                 * UI UPDATE ON JAVA FX THREAD:
                 * All UI updates MUST happen on JavaFX Application Thread.
                 * Platform.runLater() schedules code to run on UI thread.
                 */
                javafx.application.Platform.runLater(() -> {
                    // Clear existing menu items from container
                    menuContainer.getChildren().clear();
                    
                    try {
                        /**
                         * CREATE MENU ITEM CARDS:
                         * For each MenuItem, create a card using ItemCard.fxml template.
                         * Each card is a VBox loaded from FXML file.
                         */
                        for (MenuItem item : items) {
                            // Load ItemCard.fxml template
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemCard.fxml"));
                            VBox card = loader.load(); // Load FXML and create VBox

                            // Get controller from loaded FXML
                            ItemCardController cardController = loader.getController();
                            
                            // Set menu item data in the card controller
                            // This populates the card with item name, price, image, etc.
                            cardController.setData(item);

                            // Add card to FlowPane container
                            // FlowPane automatically arranges cards in a grid
                            menuContainer.getChildren().add(card);
                            System.out.println("Added card for " + item.getName() + " to FlowPane.");
                        }
                    } catch (Exception e) {
                        // Error loading ItemCard.fxml - log and continue
                        System.err.println("Error loading ItemCard.fxml:");
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                // Error loading menu items from database - log error
                System.err.println("Error loading menu items:");
                e.printStackTrace();
            } finally {
                // Always shutdown executor thread when done
                executor.shutdown();
            }
        });
    }
}
