package com.zest.controller;

/**
 * MerchantDashboardController - Merchant Dashboard Controller
 * 
 * PURPOSE:
 * This controller manages the merchant dashboard where merchants can:
 * - View and manage orders for their restaurant
 * - View and manage menu items (add, update price, toggle availability)
 * - Update order status (PENDING -> PREPARING -> READY -> DELIVERED)
 * - View restaurant statistics
 * 
 * UML CLASSES USED:
 * - Merchant: Represents the logged-in merchant
 * - Restaurant: Gets merchant's restaurant
 * - MenuItem: Displays and manages menu items
 * - Order: Displays and manages orders
 * - OrderStatus: Updates order status
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Merchant Dashboard & Restaurant Management
 */

import com.zest.Main;
import com.zest.dao.DataService;
import com.zest.model.MenuItem;
import com.zest.model.Order;
import com.zest.model.Restaurant;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Controller for merchant dashboard screen
 * Handles all merchant operations for restaurant management
 */
public class MerchantDashboardController {
    
    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from MerchantDashboard.fxml when screen loads
     */
    
    @FXML private Label restaurantNameLabel;
    @FXML private Label ordersCountLabel;
    @FXML private Label menuItemsCountLabel;
    @FXML private ListView<VBox> ordersListView;
    @FXML private ListView<VBox> menuItemsListView;
    @FXML private TextField itemNameField;
    @FXML private TextField itemPriceField;
    @FXML private TextField itemDescriptionField;
    @FXML private Button addItemButton;
    @FXML private Button logoutButton;
    @FXML private Button uploadPhotoButton;
    @FXML private Label photoLabel;
    
    /**
     * selectedImagePath - Path to selected image file
     * PURPOSE: Stores the path of the selected image for menu item
     */
    private String selectedImagePath;
    
    /**
     * dataService - Data access object for database operations
     * PURPOSE: Handles all database queries for merchant operations
     */
    private DataService dataService;
    
    /**
     * currentMerchantEmail - Static variable storing logged-in merchant email
     * PURPOSE: Session management - stores which merchant is logged in
     */
    private static String currentMerchantEmail;
    
    /**
     * restaurant - Restaurant owned by merchant
     * PURPOSE: Stores merchant's restaurant for operations
     */
    private Restaurant restaurant;
    
    /**
     * Constructor - Initializes data service
     */
    public MerchantDashboardController() {
        this.dataService = new DataService();
    }
    
    /**
     * setCurrentMerchantEmail() - Sets current merchant email (session management)
     * 
     * PURPOSE:
     * Stores the logged-in merchant's email for use in dashboard operations.
     * Called from LoginController when merchant logs in.
     * 
     * @param email Merchant's email address
     */
    public static void setCurrentMerchantEmail(String email) {
        currentMerchantEmail = email;
    }
    
    /**
     * getCurrentMerchantEmail() - Gets current merchant email
     * 
     * @return Current merchant email
     */
    public static String getCurrentMerchantEmail() {
        return currentMerchantEmail;
    }
    
    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Automatically called by JavaFX when the FXML file is loaded.
     * Loads merchant's restaurant and displays dashboard data.
     * 
     * FLOW:
     * 1. Get merchant's restaurant from database
     * 2. Load orders for restaurant
     * 3. Load menu items for restaurant
     * 4. Display statistics
     */
    @FXML
    public void initialize() {
        if (currentMerchantEmail == null || currentMerchantEmail.isEmpty()) {
            showError("No merchant logged in. Please log in again.");
            try {
                Main.switchScene("/fxml/Login.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        // Load merchant's restaurant
        restaurant = dataService.getRestaurantByMerchantEmail(currentMerchantEmail);
        if (restaurant == null) {
            showError("No restaurant found for this merchant.");
            return;
        }
        
        // Display restaurant name
        restaurantNameLabel.setText(restaurant.getName());
        
        // Load dashboard data
        loadOrders();
        loadMenuItems();
        updateStatistics();
    }
    
    /**
     * loadOrders() - Loads and displays orders for restaurant
     * 
     * PURPOSE:
     * Fetches all orders for merchant's restaurant and displays them.
     * Orders are displayed with status and action buttons.
     */
    private void loadOrders() {
        if (restaurant == null) return;
        
        List<Order> orders = dataService.getOrdersByRestaurant(restaurant.getId());
        ordersListView.getItems().clear();
        
        if (orders.isEmpty()) {
            VBox emptyBox = new VBox();
            Label emptyLabel = new Label("No orders yet");
            emptyLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            emptyBox.getChildren().add(emptyLabel);
            ordersListView.getItems().add(emptyBox);
            return;
        }
        
        for (Order order : orders) {
            VBox orderCard = createOrderCard(order);
            ordersListView.getItems().add(orderCard);
        }
    }
    
    /**
     * createOrderCard() - Creates UI card for an order
     * 
     * PURPOSE:
     * Creates a visual card displaying order information with status update buttons.
     * 
     * @param order The Order object to display
     * @return VBox containing order card UI
     */
    private VBox createOrderCard(Order order) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        
        // Order ID and Total
        HBox headerBox = new HBox(10);
        Label orderIdLabel = new Label("Order #" + order.getId());
        orderIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label totalLabel = new Label(String.format("Total: %.2f EGP", order.getTotalAmount()));
        totalLabel.setStyle("-fx-font-size: 14px;");
        headerBox.getChildren().addAll(orderIdLabel, totalLabel);
        
        // Status Label
        Label statusLabel = new Label("Status: " + order.getStatus());
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Status update buttons
        HBox buttonBox = new HBox(10);
        String currentStatus = order.getStatus();
        
        if ("PENDING".equals(currentStatus)) {
            Button preparingBtn = new Button("Start Preparing");
            preparingBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            preparingBtn.setOnAction(e -> updateOrderStatus(order.getId(), "PREPARING"));
            buttonBox.getChildren().add(preparingBtn);
        } else if ("PREPARING".equals(currentStatus)) {
            Button readyBtn = new Button("Mark Ready");
            readyBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
            readyBtn.setOnAction(e -> updateOrderStatus(order.getId(), "READY"));
            buttonBox.getChildren().add(readyBtn);
        } else if ("READY".equals(currentStatus)) {
            Button deliveredBtn = new Button("Mark Delivered");
            deliveredBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
            deliveredBtn.setOnAction(e -> updateOrderStatus(order.getId(), "DELIVERED"));
            buttonBox.getChildren().add(deliveredBtn);
        } else {
            Label completedLabel = new Label("Completed");
            completedLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
            buttonBox.getChildren().add(completedLabel);
        }
        
        card.getChildren().addAll(headerBox, statusLabel, buttonBox);
        return card;
    }
    
    /**
     * loadMenuItems() - Loads and displays menu items for restaurant
     * 
     * PURPOSE:
     * Fetches all menu items for merchant's restaurant and displays them.
     * Each item shows name, price, availability, and edit buttons.
     */
    private void loadMenuItems() {
        if (restaurant == null) return;
        
        List<MenuItem> items = dataService.getMenuItemsByRestaurant(restaurant.getId());
        menuItemsListView.getItems().clear();
        
        if (items.isEmpty()) {
            VBox emptyBox = new VBox();
            Label emptyLabel = new Label("No menu items yet. Add your first item!");
            emptyLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            emptyBox.getChildren().add(emptyLabel);
            menuItemsListView.getItems().add(emptyBox);
            return;
        }
        
        for (MenuItem item : items) {
            VBox itemCard = createMenuItemCard(item);
            menuItemsListView.getItems().add(itemCard);
        }
    }
    
    /**
     * createMenuItemCard() - Creates UI card for a menu item
     * 
     * PURPOSE:
     * Creates a visual card displaying menu item information with edit buttons.
     * 
     * @param item The MenuItem object to display
     * @return VBox containing menu item card UI
     */
    private VBox createMenuItemCard(MenuItem item) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        
        // Item name and price
        HBox headerBox = new HBox(10);
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label priceLabel = new Label(String.format("%.2f EGP", item.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 14px;");
        headerBox.getChildren().addAll(nameLabel, priceLabel);
        
        // Availability status
        Label availabilityLabel = new Label(item.isAvailable() ? "Available" : "Unavailable");
        availabilityLabel.setStyle(item.isAvailable() 
            ? "-fx-text-fill: green; -fx-font-size: 12px;" 
            : "-fx-text-fill: red; -fx-font-size: 12px;");
        
        // Action buttons
        HBox buttonBox = new HBox(10);
        
        // Edit price button
        Button editPriceBtn = new Button("Edit Price");
        editPriceBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        editPriceBtn.setOnAction(e -> showEditPriceDialog(item));
        
        // Toggle availability button
        Button toggleBtn = new Button(item.isAvailable() ? "Set Unavailable" : "Set Available");
        toggleBtn.setStyle(item.isAvailable() 
            ? "-fx-background-color: #f44336; -fx-text-fill: white;"
            : "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        toggleBtn.setOnAction(e -> toggleItemAvailability(item));
        
        buttonBox.getChildren().addAll(editPriceBtn, toggleBtn);
        
        card.getChildren().addAll(headerBox, availabilityLabel, buttonBox);
        return card;
    }
    
    /**
     * showEditPriceDialog() - Shows dialog to edit menu item price
     * 
     * PURPOSE:
     * Displays a dialog for merchant to enter new price for menu item.
     * 
     * @param item The MenuItem to update
     */
    private void showEditPriceDialog(MenuItem item) {
        TextField priceField = new TextField(String.valueOf(item.getCurrentPrice()));
        priceField.setPromptText("Enter new price");
        
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Edit Price");
        dialog.setHeaderText("Update price for " + item.getName());
        dialog.setContentText("Enter new price:");
        dialog.getDialogPane().setContent(priceField);
        
        dialog.showAndWait().ifPresent(response -> {
            try {
                double newPrice = Double.parseDouble(priceField.getText());
                if (newPrice > 0) {
                    if (dataService.updateMenuItemPrice(item.getId(), newPrice)) {
                        showSuccess("Price updated successfully!");
                        loadMenuItems();
                        updateStatistics();
                    } else {
                        showError("Failed to update price.");
                    }
                } else {
                    showError("Price must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                showError("Invalid price format.");
            }
        });
    }
    
    /**
     * toggleItemAvailability() - Toggles menu item availability
     * 
     * PURPOSE:
     * Switches menu item availability between available and unavailable.
     * 
     * @param item The MenuItem to toggle
     */
    private void toggleItemAvailability(MenuItem item) {
        boolean newAvailability = !item.isAvailable();
        if (dataService.updateMenuItemAvailability(item.getId(), newAvailability)) {
            showSuccess("Availability updated successfully!");
            loadMenuItems();
            updateStatistics();
        } else {
            showError("Failed to update availability.");
        }
    }
    
    /**
     * handleUploadPhoto() - Event handler for upload photo button
     * 
     * PURPOSE:
     * Opens file chooser to select an image file and copies it to resources directory.
     * 
     * FLOW:
     * 1. Open file chooser dialog
     * 2. User selects image file
     * 3. Copy file to resources/images/menu/ directory
     * 4. Store file name for use in addMenuItem
     */
    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Menu Item Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        
        // Get the stage from any control
        Stage stage = (Stage) uploadPhotoButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            try {
                // Create images/menu directory if it doesn't exist
                Path imagesDir = Paths.get("src/main/resources/images/menu");
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }
                
                // Generate unique filename using timestamp
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = imagesDir.resolve(fileName);
                
                // Copy file to resources directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                
                // Store the image path (relative to resources/images/menu/)
                selectedImagePath = fileName;
                photoLabel.setText("Photo: " + fileName);
                photoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
                
                showSuccess("Photo uploaded successfully!");
            } catch (IOException e) {
                System.err.println("Error copying image file:");
                e.printStackTrace();
                showError("Failed to upload photo. Please try again.");
            }
        }
    }
    
    /**
     * handleAddMenuItem() - Event handler for add menu item button
     * 
     * PURPOSE:
     * Adds a new menu item to the restaurant's menu.
     * Validates input and saves to database with photo.
     * 
     * PHOTO HANDLING:
     * - If photo is uploaded, uses uploaded image
     * - If no photo, uses default_item.png
     * - Photo is saved to database and visible to customers
     */
    @FXML
    private void handleAddMenuItem() {
        String name = itemNameField.getText().trim();
        String priceText = itemPriceField.getText().trim();
        String description = itemDescriptionField.getText().trim();
        
        if (name.isEmpty()) {
            showError("Please enter item name.");
            return;
        }
        
        if (priceText.isEmpty()) {
            showError("Please enter item price.");
            return;
        }
        
        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                showError("Price must be greater than 0.");
                return;
            }
            
            if (restaurant == null) {
                showError("Restaurant not found.");
                return;
            }
            
            // Use uploaded photo if available, otherwise use default
            String imageUrl = (selectedImagePath != null && !selectedImagePath.isEmpty()) 
                ? selectedImagePath 
                : "default_item.png";
            
            int itemId = dataService.addMenuItem(
                restaurant.getId(),
                name,
                price,
                description.isEmpty() ? null : description,
                imageUrl
            );
            
            if (itemId > 0) {
                showSuccess("Menu item added successfully!");
                itemNameField.clear();
                itemPriceField.clear();
                itemDescriptionField.clear();
                selectedImagePath = null;
                photoLabel.setText("No photo selected");
                photoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                loadMenuItems();
                updateStatistics();
            } else {
                showError("Failed to add menu item.");
            }
        } catch (NumberFormatException e) {
            showError("Invalid price format.");
        }
    }
    
    /**
     * updateOrderStatus() - Updates order status
     * 
     * PURPOSE:
     * Updates the status of an order in the database.
     * 
     * @param orderId The ID of the order
     * @param newStatus The new status to set
     */
    private void updateOrderStatus(int orderId, String newStatus) {
        if (dataService.updateOrderStatus(orderId, newStatus)) {
            showSuccess("Order status updated successfully!");
            loadOrders();
            updateStatistics();
        } else {
            showError("Failed to update order status.");
        }
    }
    
    /**
     * updateStatistics() - Updates dashboard statistics
     * 
     * PURPOSE:
     * Refreshes the statistics displayed on the dashboard
     * (order count, menu item count).
     */
    private void updateStatistics() {
        if (restaurant == null) return;
        
        List<Order> orders = dataService.getOrdersByRestaurant(restaurant.getId());
        List<MenuItem> items = dataService.getMenuItemsByRestaurant(restaurant.getId());
        
        ordersCountLabel.setText("Orders: " + orders.size());
        menuItemsCountLabel.setText("Menu Items: " + items.size());
    }
    
    /**
     * handleLogout() - Event handler for logout button
     * 
     * PURPOSE:
     * Logs out merchant and returns to login screen.
     */
    @FXML
    private void handleLogout() {
        currentMerchantEmail = null;
        try {
            Main.switchScene("/fxml/Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * showSuccess() - Shows success message
     * 
     * @param message Success message to display
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * showError() - Shows error message
     * 
     * @param message Error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

