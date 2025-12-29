package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService;
import com.zest.logic.CartManager;
import com.zest.logic.CashPayment;
import com.zest.logic.PayPalAdapter;
import com.zest.logic.PaymentStrategy;
import com.zest.model.MenuItem;
import com.zest.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Map;

/**
 * Checkout Controller
 * Handles order review, payment method selection, and order placement
 */
public class CheckoutController {
    
    @FXML
    private VBox orderItemsContainer;
    
    @FXML
    private Label subtotalLabel;
    
    @FXML
    private Label vatLabel;
    
    @FXML
    private Label deliveryLabel;
    
    @FXML
    private Label totalLabel;
    
    @FXML
    private RadioButton cashRadio;
    
    @FXML
    private RadioButton paypalRadio;
    
    @FXML
    private ToggleGroup paymentMethodGroup;
    
    @FXML
    private javafx.scene.control.TextField addressField;
    
    @FXML
    private javafx.scene.control.TextField cityField;
    
    @FXML
    private javafx.scene.control.TextField phoneField;
    
    private DataService dataService;
    private CartManager cart;
    
    public CheckoutController() {
        this.dataService = new DataService();
        this.cart = CartManager.getInstance();
    }
    
    @FXML
    public void initialize() {
        // Set default payment method to Cash
        paymentMethodGroup = new ToggleGroup();
        cashRadio.setToggleGroup(paymentMethodGroup);
        paypalRadio.setToggleGroup(paymentMethodGroup);
        cashRadio.setSelected(true);
        
        loadOrderSummary();
    }
    
    /**
     * Load and display order summary
     */
    private void loadOrderSummary() {
        orderItemsContainer.getChildren().clear();
        
        // Get unique items with quantities
        Map<MenuItem, Integer> itemQuantities = cart.getItemQuantities();
        
        // Display items with quantities
        for (Map.Entry<MenuItem, Integer> entry : itemQuantities.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            double price = item.getPrice();
            double itemTotal = price * quantity;
            
            Label itemLabel = new Label();
            if (quantity > 1) {
                itemLabel.setText(String.format("%s x%d - %.2f EGP each = %.2f EGP", 
                    item.getName(), quantity, price, itemTotal));
            } else {
                itemLabel.setText(String.format("%s - %.2f EGP", item.getName(), price));
            }
            itemLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
            orderItemsContainer.getChildren().add(itemLabel);
        }
        
        // Calculate totals
        double subtotal = cart.getSubtotal();
        double vat = subtotal * 0.14; // 14% VAT
        double deliveryCost = calculateDeliveryCost(subtotal);
        double total = subtotal + vat + deliveryCost;
        
        subtotalLabel.setText(String.format("%.2f EGP", subtotal));
        vatLabel.setText(String.format("%.2f EGP", vat));
        deliveryLabel.setText(String.format("%.2f EGP", deliveryCost));
        totalLabel.setText(String.format("%.2f EGP", total));
    }
    
    /**
     * Calculate delivery cost based on order size
     * Small orders (< 100 EGP): 35 EGP
     * Medium orders (100-200 EGP): 25 EGP
     * Large orders (> 200 EGP): 15 EGP
     * @param subtotal Order subtotal
     * @return Delivery cost
     */
    private double calculateDeliveryCost(double subtotal) {
        if (subtotal < 100) {
            return 35.0;
        } else if (subtotal <= 200) {
            return 25.0;
        } else {
            return 15.0;
        }
    }
    
    @FXML
    private void handlePlaceOrder() {
        // Validate cart
        if (cart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Please add items before placing an order.");
            alert.showAndWait();
            try {
                Main.switchScene("/fxml/Cart.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        // Validate address fields
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (address.isEmpty() || city.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all address fields (Address, City, Phone).");
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
        
        // Get selected payment method
        PaymentStrategy paymentStrategy;
        String paymentMethod;
        
        if (paypalRadio.isSelected()) {
            paymentStrategy = new PayPalAdapter();
            paymentMethod = "PayPal";
        } else {
            paymentStrategy = new CashPayment();
            paymentMethod = "Cash";
        }
        
        // Calculate final total with VAT and delivery
        double subtotal = cart.getSubtotal();
        double vat = subtotal * 0.14;
        double deliveryCost = calculateDeliveryCost(subtotal);
        double totalPrice = subtotal + vat + deliveryCost;
        
        // Process payment using Strategy Pattern
        paymentStrategy.pay(totalPrice);
        
        // Create and save order
        Order order = new Order(userId, totalPrice, "PENDING");
        dataService.saveOrder(order);
        
        // Clear cart and restaurant selection
        cart.clear();
        RestaurantSelectionController.clearSelection();
        
        // Show success message with delivery address
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed Successfully!");
        alert.setHeaderText(null);
        alert.setContentText(String.format(
            "Your order has been placed successfully!\n\n" +
            "Payment Method: %s\n" +
            "Subtotal: %.2f EGP\n" +
            "VAT (14%%): %.2f EGP\n" +
            "Delivery: %.2f EGP\n" +
            "Total Amount: %.2f EGP\n\n" +
            "Delivery Address:\n%s, %s\nPhone: %s\n\n" +
            "Thank you for your order!",
            paymentMethod, subtotal, vat, deliveryCost, totalPrice,
            address, city, phone
        ));
        alert.showAndWait();
        
        // Return to restaurant selection
        try {
            Main.switchScene("/fxml/RestaurantSelection.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBackToCart() {
        try {
            Main.switchScene("/fxml/Cart.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

