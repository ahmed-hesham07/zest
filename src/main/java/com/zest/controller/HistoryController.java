package com.zest.controller;

/**
 * HistoryController - Order History Display Controller
 * 
 * PURPOSE:
 * This controller displays order history for the logged-in user.
 * It separates current orders (PENDING, PREPARING, READY) from
 * completed orders (DELIVERED) and displays them with status indicators.
 * 
 * UML CLASSES USED:
 * - Order: Displays Order objects with id, status, totalAmount
 * - OrderStatus: Uses OrderStatus enum (PENDING, PREPARING, READY, DELIVERED)
 * 
 * ORDER STATUS DISPLAY:
 * - Current Orders: PENDING, PREPARING, READY (shown with orange styling)
 * - History Orders: DELIVERED (shown with gray styling)
 * - Each order shows order ID, status badge, and total price
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: User Management & Order History
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.dao.DataService; // Data access layer for fetching orders
import com.zest.model.Order; // UML Class: Order model with status, totalAmount
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.geometry.Insets; // Spacing for UI elements
import javafx.scene.control.Button; // Button for actions
import javafx.scene.control.Label; // Text labels
import javafx.scene.control.ListView; // List view for displaying orders
import javafx.scene.control.Separator; // Visual separator between sections
import javafx.scene.layout.HBox; // Horizontal container
import javafx.scene.layout.VBox; // Vertical container for order cards
import java.io.IOException; // Exception handling
import java.util.ArrayList; // ArrayList for order lists
import java.util.List; // List interface

/**
 * Controller for order history screen
 * Displays current and completed orders for logged-in user
 */
public class HistoryController {
    
    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from History.fxml when screen loads
     */
    
    /**
     * historyListView - ListView displaying order cards
     * PURPOSE: Shows all orders (current and history) as cards
     * CONNECTS TO: History.fxml fx:id="historyListView"
     */
    @FXML private ListView<VBox> historyListView;
    
    /**
     * emptyLabel - Label shown when no orders found
     * PURPOSE: Displays message when user has no orders
     * CONNECTS TO: History.fxml fx:id="emptyLabel"
     */
    @FXML private Label emptyLabel;
    
    /**
     * currentOrdersLabel - Label for current orders section header
     * PURPOSE: Shows "Current Orders" header with count
     * CONNECTS TO: History.fxml fx:id="currentOrdersLabel"
     */
    @FXML private Label currentOrdersLabel;
    
    /**
     * historyOrdersLabel - Label for history orders section header
     * PURPOSE: Shows "Order History" header with count
     * CONNECTS TO: History.fxml fx:id="historyOrdersLabel"
     */
    @FXML private Label historyOrdersLabel;
    
    /**
     * dataService - Data access object for database operations
     * PURPOSE: Fetches orders from database for logged-in user
     */
    private DataService dataService;
    
    /**
     * currentUserEmail - Static variable storing logged-in user email
     * PURPOSE: Session management - stores which user is logged in
     * SCOPE: Static so it can be accessed from other controllers
     */
    private static String currentUserEmail;
    
    /**
     * Constructor - Initializes data service
     * PURPOSE: Sets up data service when controller is created
     */
    public HistoryController() {
        this.dataService = new DataService(); // Create data service for database access
    }
    
    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Called automatically when History.fxml loads.
     * Loads and displays order history for logged-in user.
     * 
     * FLOW:
     * 1. JavaFX loads History.fxml
     * 2. JavaFX creates HistoryController instance
     * 3. JavaFX injects @FXML fields
     * 4. JavaFX calls initialize()
     * 5. We load order history
     */
    @FXML
    public void initialize() {
        loadOrderHistory(); // Load and display orders
    }
    
    /**
     * setCurrentUserEmail() - Sets logged-in user email
     * 
     * PURPOSE:
     * Called after successful login to store user session.
     * This allows the controller to know which user's orders to display.
     * 
     * @param email User's email address
     */
    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email; // Store email in static variable
    }
    
    /**
     * getCurrentUserEmail() - Gets logged-in user email
     * 
     * PURPOSE:
     * Returns the email of the currently logged-in user.
     * Used by other controllers to identify the current user.
     * 
     * @return User's email address or null if not logged in
     */
    public static String getCurrentUserEmail() {
        return currentUserEmail; // Return stored email
    }
    
    /**
     * loadOrderHistory() - Loads and displays order history
     * 
     * PURPOSE:
     * Fetches orders from database, separates current orders from history,
     * and displays them with appropriate styling and status indicators.
     * 
     * UML CLASSES USED:
     * - Order: Fetches Order objects from database
     * - OrderStatus: Uses OrderStatus enum values to categorize orders
     * 
     * FLOW:
     * 1. Check if user is logged in
     * 2. Get user ID from email
     * 3. Fetch all orders for user from database
     * 4. Separate current orders (PENDING, PREPARING, READY) from history (DELIVERED)
     * 5. Display current orders section with orange styling
     * 6. Display history orders section with gray styling
     */
    private void loadOrderHistory() {
        /**
         * STEP 1: VALIDATE USER IS LOGGED IN
         * Check if currentUserEmail is set (user is logged in)
         */
        if (currentUserEmail == null || currentUserEmail.isEmpty()) {
            emptyLabel.setText("No user logged in"); // Show message
            currentOrdersLabel.setVisible(false); // Hide current orders header
            historyOrdersLabel.setVisible(false); // Hide history orders header
            return; // Exit method early
        }
        
        /**
         * STEP 2: GET USER ID FROM EMAIL
         * Need user ID to query orders from database
         */
        int userId = dataService.getUserIdByEmail(currentUserEmail);
        if (userId == -1) {
            emptyLabel.setText("User not found"); // Show error message
            currentOrdersLabel.setVisible(false); // Hide headers
            historyOrdersLabel.setVisible(false);
            return; // Exit method early
        }
        
        /**
         * STEP 3: FETCH ORDERS FROM DATABASE
         * Get all orders for this user from database
         * Orders are returned as List<Order>
         */
        List<Order> allOrders = dataService.getOrderHistory(userId);
        
        /**
         * STEP 4: CHECK IF ORDERS EXIST
         * If no orders found, show empty message
         */
        if (allOrders.isEmpty()) {
            emptyLabel.setText("No order history found"); // Show empty message
            currentOrdersLabel.setVisible(false); // Hide headers
            historyOrdersLabel.setVisible(false);
            return; // Exit method early
        }
        
        /**
         * STEP 5: PREPARE DISPLAY
         * Clear existing display and prepare for new orders
         */
        emptyLabel.setText(""); // Clear empty message
        historyListView.getItems().clear(); // Clear existing order cards
        
        /**
         * STEP 6: SEPARATE CURRENT ORDERS FROM HISTORY
         * Categorize orders based on OrderStatus:
         * - Current: PENDING, PREPARING, READY (orders in progress)
         * - History: DELIVERED (completed orders)
         */
        List<Order> currentOrders = new ArrayList<>(); // List for current orders
        List<Order> historyOrders = new ArrayList<>(); // List for history orders
        
        for (Order order : allOrders) {
            String status = order.getStatus().toUpperCase(); // Get order status as string
            
            // Categorize order based on status
            if (status.equals("PENDING") || status.equals("PREPARING") || status.equals("READY")) {
                currentOrders.add(order); // Add to current orders
            } else if (status.equals("DELIVERED")) {
                historyOrders.add(order); // Add to history orders
            }
        }
        
        /**
         * STEP 7: DISPLAY CURRENT ORDERS SECTION
         * Show current orders with orange styling and status indicators
         */
        if (!currentOrders.isEmpty()) {
            currentOrdersLabel.setVisible(true); // Show current orders header
            currentOrdersLabel.setText("📦 Current Orders (" + currentOrders.size() + ")"); // Set header text with count
            currentOrdersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff9f1c; -fx-padding: 10 0 5 0;"); // Orange styling
            
            // Create and add order cards for each current order
            for (Order order : currentOrders) {
                VBox orderBox = createOrderCard(order, true); // Create card (true = current order)
                historyListView.getItems().add(orderBox); // Add to list view
            }
            
            // Add separator between current and history sections
            VBox separatorBox = new VBox();
            separatorBox.setPadding(new Insets(15, 0, 15, 0)); // Add padding
            Separator separator = new Separator(); // Create separator line
            separatorBox.getChildren().add(separator); // Add separator to box
            historyListView.getItems().add(separatorBox); // Add separator to list
        } else {
            currentOrdersLabel.setVisible(false); // Hide header if no current orders
        }
        
        /**
         * STEP 8: DISPLAY HISTORY ORDERS SECTION
         * Show completed orders with gray styling
         */
        if (!historyOrders.isEmpty()) {
            historyOrdersLabel.setVisible(true); // Show history orders header
            historyOrdersLabel.setText("📚 Order History (" + historyOrders.size() + ")"); // Set header text with count
            historyOrdersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #6c757d; -fx-padding: 10 0 5 0;"); // Gray styling
            
            // Create and add order cards for each history order
            for (Order order : historyOrders) {
                VBox orderBox = createOrderCard(order, false); // Create card (false = history order)
                historyListView.getItems().add(orderBox); // Add to list view
            }
        } else {
            historyOrdersLabel.setVisible(false); // Hide header if no history orders
        }
    }
    
    /**
     * createOrderCard() - Creates visual card for an order
     * 
     * PURPOSE:
     * Creates a VBox card displaying order information with status badge
     * and appropriate styling based on whether it's current or completed.
     * 
     * UML CLASSES USED:
     * - Order: Gets order id, status, totalAmount
     * - OrderStatus: Uses status to determine styling and icons
     * 
     * @param order The Order object to display
     * @param isCurrentOrder True if current order, false if delivered
     * @return VBox card containing order information
     */
    private VBox createOrderCard(Order order, boolean isCurrentOrder) {
        /**
         * CREATE CARD CONTAINER:
         * VBox with spacing and padding for order information
         */
        VBox card = new VBox(8); // Create VBox with 8px spacing
        card.setPadding(new Insets(12)); // Add 12px padding
        
        /**
         * APPLY STYLING BASED ON ORDER TYPE:
         * Current orders: Orange border and background
         * History orders: Gray border and background
         */
        if (isCurrentOrder) {
            // Current order styling (orange theme)
            card.setStyle("-fx-background-color: #fff3e0; -fx-border-color: #ff9f1c; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        } else {
            // History order styling (gray theme)
            card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
        }
        
        /**
         * CREATE HEADER WITH ORDER ID AND STATUS:
         * HBox containing order ID label and status badge
         */
        HBox headerBox = new HBox(10); // Create HBox with 10px spacing
        Label orderIdLabel = new Label("Order #" + order.getId()); // Create order ID label
        orderIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;"); // Bold, large font
        
        /**
         * CREATE STATUS BADGE:
         * Badge showing order status with color coding and icon
         */
        String status = order.getStatus().toUpperCase(); // Get status as uppercase string
        String statusText = getStatusDisplayText(status); // Get display text (e.g., "Pending")
        String statusColor = getStatusColor(status); // Get color for badge (e.g., "#ffc107")
        String statusIcon = getStatusIcon(status); // Get icon for status (e.g., "⏳")
        
        Label statusBadge = new Label(statusIcon + " " + statusText); // Create badge label
        statusBadge.setStyle(String.format(
            "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white; " +
            "-fx-background-color: %s; -fx-padding: 4 10 4 10; -fx-background-radius: 12;",
            statusColor
        )); // Style badge with colored background
        
        headerBox.getChildren().addAll(orderIdLabel, statusBadge); // Add labels to header
        
        /**
         * CREATE PRICE LABEL:
         * Label showing total order amount
         */
        Label priceLabel = new Label("Total: " + String.format("%.2f EGP", order.getTotalPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;"); // Bold, dark text
        
        /**
         * ADD INDICATOR BASED ON ORDER TYPE:
         * Current orders: "Order in progress" indicator
         * History orders: "Order completed" indicator with review button
         */
        if (isCurrentOrder) {
            Label indicatorLabel = new Label("⏳ Order in progress"); // Current order indicator
            indicatorLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #ff9f1c; -fx-font-style: italic;"); // Orange, italic
            card.getChildren().addAll(headerBox, priceLabel, indicatorLabel); // Add to card
        } else {
            Label indicatorLabel = new Label("✓ Order completed"); // History order indicator
            indicatorLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #28a745; -fx-font-style: italic;"); // Green, italic
            
            /**
             * CREATE REVIEW BUTTON FOR DELIVERED ORDERS:
             * Allow customers to leave reviews after order is delivered
             */
            Button reviewButton = new Button("Leave Review"); // Create review button
            reviewButton.setStyle("-fx-background-color: #ff9f1c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;"); // Orange styling
            reviewButton.setOnAction(e -> handleLeaveReview(order)); // Set click handler
            
            card.getChildren().addAll(headerBox, priceLabel, indicatorLabel, reviewButton); // Add to card
        }
        
        return card; // Return completed card
    }
    
    /**
     * getStatusDisplayText() - Gets display text for order status
     * 
     * PURPOSE:
     * Converts status string to user-friendly display text.
     * 
     * @param status Order status string (uppercase)
     * @return Display text for status
     */
    private String getStatusDisplayText(String status) {
        switch (status.toUpperCase()) {
            case "PENDING": return "Pending"; // Order placed, waiting to be prepared
            case "PREPARING": return "Preparing"; // Order being prepared
            case "READY": return "Ready"; // Order ready for delivery
            case "DELIVERED": return "Delivered"; // Order delivered
            default: return status; // Return original if unknown
        }
    }
    
    /**
     * getStatusColor() - Gets color for status badge
     * 
     * PURPOSE:
     * Returns hex color code for status badge background.
     * Different colors for different statuses.
     * 
     * @param status Order status string
     * @return Hex color code for badge
     */
    private String getStatusColor(String status) {
        switch (status.toUpperCase()) {
            case "PENDING": return "#ffc107"; // Yellow for pending
            case "PREPARING": return "#17a2b8"; // Blue for preparing
            case "READY": return "#28a745"; // Green for ready
            case "DELIVERED": return "#6c757d"; // Gray for delivered
            default: return "#6c757d"; // Default gray
        }
    }
    
    /**
     * getStatusIcon() - Gets icon for order status
     * 
     * PURPOSE:
     * Returns emoji icon for order status badge.
     * Visual indicator for order status.
     * 
     * @param status Order status string
     * @return Emoji icon string
     */
    private String getStatusIcon(String status) {
        switch (status.toUpperCase()) {
            case "PENDING": return "⏳"; // Hourglass for pending
            case "PREPARING": return "👨‍🍳"; // Chef for preparing
            case "READY": return "✅"; // Checkmark for ready
            case "DELIVERED": return "📦"; // Package for delivered
            default: return "•"; // Default bullet
        }
    }
    
    /**
     * handleBackToHome() - Event handler for back button
     * 
     * PURPOSE:
     * Returns user to menu/home screen.
     * 
     * FLOW:
     * 1. User clicks back button
     * 2. Navigate to Home.fxml screen
     */
    @FXML
    private void handleBackToHome() {
        try {
            Main.switchScene("/fxml/Home.fxml"); // Navigate to menu screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error
             */
            e.printStackTrace();
            System.err.println("Could not load Home.fxml");
        }
    }
    
    /**
     * refresh() - Refreshes order history display
     * 
     * PURPOSE:
     * Can be called after placing new order to update display.
     * Reloads orders from database and updates UI.
     */
    public void refresh() {
        loadOrderHistory(); // Reload orders from database
    }
    
    /**
     * handleLeaveReview() - Event handler for leave review button
     * 
     * PURPOSE:
     * Handles when user clicks "Leave Review" on a delivered order.
     * Gets restaurant ID from order and navigates to reviews screen.
     * 
     * UML CLASSES USED:
     * - Order: Gets order ID to find restaurant
     * 
     * FLOW:
     * 1. User clicks "Leave Review" button
     * 2. Get restaurant ID from order (via order items)
     * 3. Set restaurant ID in ReviewController
     * 4. Navigate to reviews screen
     */
    private void handleLeaveReview(Order order) {
        /**
         * STEP 1: GET RESTAURANT ID FROM ORDER
         * Need to get restaurant ID from order items
         * Query database to find restaurant ID from order items
         */
        int restaurantId = dataService.getRestaurantIdByOrderId(order.getId());
        
        if (restaurantId == -1) {
            /**
             * ERROR: Could not find restaurant
             * Show error message
             */
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not find restaurant for this order.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * STEP 2: SET RESTAURANT ID IN REVIEW CONTROLLER
         * Set restaurant ID so ReviewController can load reviews
         */
        com.zest.controller.ReviewController.setCurrentRestaurantId(restaurantId); // Set restaurant ID
        
        try {
            /**
             * STEP 3: NAVIGATE TO REVIEWS SCREEN
             * Redirect user to reviews screen where they can submit review
             */
            Main.switchScene("/fxml/Reviews.fxml"); // Navigate to reviews screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error and show error message
             */
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load reviews screen.");
            alert.showAndWait();
        }
    }
}
