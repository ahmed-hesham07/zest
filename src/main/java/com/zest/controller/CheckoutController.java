package com.zest.controller;

/**
 * CheckoutController - Order Placement Controller
 * 
 * PURPOSE:
 * This controller handles the checkout process where users review their order,
 * select payment method, enter delivery address, and place the order.
 * 
 * UML CLASSES USED:
 * - Order: Creates new Order object with OrderStatus, OrderItems, PaymentStrategy
 * - OrderItem: Creates OrderItems with Snapshot Pattern (priceAtPurchase)
 * - OrderStatus: Sets order status to PENDING
 * - PaymentStrategy: Uses Strategy Pattern for payment (CashPayment or PayPalAdapter)
 * - MenuItem: Gets items from cart to create OrderItems
 * 
 * DESIGN PATTERNS:
 * - Snapshot Pattern: OrderItem captures priceAtPurchase at order time
 * - Strategy Pattern: PaymentStrategy interface for different payment methods
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Backend Architect & Order Processing
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.dao.DataService; // Data access layer for saving orders
import com.zest.logic.CartManager; // Singleton cart manager
import com.zest.logic.CashPayment; // UML: PaymentStrategy implementation for cash
import com.zest.logic.PayPalAdapter; // UML: PaymentStrategy implementation for PayPal
import com.zest.logic.PaymentStrategy; // UML: Payment interface (Strategy Pattern)
import com.zest.model.MenuItem; // UML: Menu item model
import com.zest.model.Order; // UML: Order model with OrderItems, PaymentStrategy, OrderStatus
import com.zest.model.OrderItem; // UML: OrderItem with Snapshot Pattern (priceAtPurchase)
import com.zest.model.OrderStatus; // UML: Order status enum (PENDING, PREPARING, READY, DELIVERED)
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.scene.control.Alert; // Popup messages
import javafx.scene.control.Label; // Text labels
import javafx.scene.control.RadioButton; // Radio buttons for payment selection
import javafx.scene.control.ToggleGroup; // Groups radio buttons together
import javafx.scene.layout.VBox; // Vertical container for order items
import java.io.IOException; // Exception handling
import java.util.Map; // Map for item quantities

/**
 * Controller for checkout screen
 * Handles order review, payment selection, and order placement
 */
public class CheckoutController {
    
    /**
     * FXML INJECTED FIELDS:
     * These are automatically populated from Checkout.fxml when screen loads
     */
    
    /**
     * orderItemsContainer - Container displaying order items
     * PURPOSE: Shows list of items in cart with quantities and prices
     * CONNECTS TO: Checkout.fxml fx:id="orderItemsContainer"
     */
    @FXML
    private VBox orderItemsContainer;
    
    /**
     * subtotalLabel - Label showing subtotal before taxes and delivery
     * PURPOSE: Displays subtotal amount
     * CONNECTS TO: Checkout.fxml fx:id="subtotalLabel"
     */
    @FXML
    private Label subtotalLabel;
    
    /**
     * vatLabel - Label showing VAT (14% tax)
     * PURPOSE: Displays VAT amount
     * CONNECTS TO: Checkout.fxml fx:id="vatLabel"
     */
    @FXML
    private Label vatLabel;
    
    /**
     * deliveryLabel - Label showing delivery fee
     * PURPOSE: Displays delivery cost
     * CONNECTS TO: Checkout.fxml fx:id="deliveryLabel"
     */
    @FXML
    private Label deliveryLabel;
    
    /**
     * totalLabel - Label showing final total amount
     * PURPOSE: Displays total amount including VAT and delivery
     * CONNECTS TO: Checkout.fxml fx:id="totalLabel"
     */
    @FXML
    private Label totalLabel;
    
    /**
     * cashRadio - Radio button for cash payment
     * PURPOSE: Allows user to select cash payment method
     * CONNECTS TO: Checkout.fxml fx:id="cashRadio"
     */
    @FXML
    private RadioButton cashRadio;
    
    /**
     * paypalRadio - Radio button for PayPal payment
     * PURPOSE: Allows user to select PayPal payment method
     * CONNECTS TO: Checkout.fxml fx:id="paypalRadio"
     */
    @FXML
    private RadioButton paypalRadio;
    
    /**
     * paymentMethodGroup - Groups radio buttons so only one can be selected
     * PURPOSE: Ensures user selects only one payment method
     * CONNECTS TO: Checkout.fxml fx:id="paymentMethodGroup"
     */
    @FXML
    private ToggleGroup paymentMethodGroup;
    
    /**
     * addressField - Text field for delivery address
     * PURPOSE: User enters street address for delivery
     * CONNECTS TO: Checkout.fxml fx:id="addressField"
     */
    @FXML
    private javafx.scene.control.TextField addressField;
    
    /**
     * cityField - Text field for delivery city
     * PURPOSE: User enters city for delivery
     * CONNECTS TO: Checkout.fxml fx:id="cityField"
     */
    @FXML
    private javafx.scene.control.TextField cityField;
    
    /**
     * phoneField - Text field for contact phone number
     * PURPOSE: User enters phone number for delivery contact
     * CONNECTS TO: Checkout.fxml fx:id="phoneField"
     */
    @FXML
    private javafx.scene.control.TextField phoneField;

    /**
     * dataService - Data access object for database operations
     * PURPOSE: Saves orders to database, gets user ID by email
     */
    private DataService dataService;
    
    /**
     * cart - Singleton cart manager instance
     * PURPOSE: Gets items from cart, calculates totals
     */
    private CartManager cart;
    
    /**
     * Constructor - Initializes data service and cart manager
     * PURPOSE: Sets up dependencies when controller is created
     */
    public CheckoutController() {
        this.dataService = new DataService(); // Create data service for database access
        this.cart = CartManager.getInstance(); // Get singleton cart instance
    }
    
    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Called automatically when Checkout.fxml loads.
     * Sets up payment method selection and loads order summary.
     * 
     * FLOW:
     * 1. Set up radio button group for payment selection
     * 2. Set default payment to Cash
     * 3. Load and display order summary
     */
    @FXML
    public void initialize() {
        // Set up radio button group so only one payment method can be selected
        paymentMethodGroup = new ToggleGroup();
        cashRadio.setToggleGroup(paymentMethodGroup); // Add cash radio to group
        paypalRadio.setToggleGroup(paymentMethodGroup); // Add PayPal radio to group
        cashRadio.setSelected(true); // Default to cash payment
        
        loadOrderSummary(); // Load and display order items and totals
    }
    
    /**
     * loadOrderSummary() - Loads and displays order summary
     * 
     * PURPOSE:
     * Displays all items in cart with quantities and prices,
     * calculates and displays subtotal, VAT, delivery, and total.
     * 
     * UML CLASSES USED:
     * - MenuItem: Gets items from cart to display
     * 
     * FLOW:
     * 1. Clear existing items from container
     * 2. Get items and quantities from cart
     * 3. Display each item with quantity and price
     * 4. Calculate subtotal, VAT (14%), delivery cost
     * 5. Calculate total and update labels
     */
    private void loadOrderSummary() {
        // Clear existing order items from container
        orderItemsContainer.getChildren().clear();
        
        /**
         * GET ITEMS FROM CART:
         * CartManager stores items as Map<MenuItem, Integer> where:
         * - Key: MenuItem object
         * - Value: Quantity of that item
         */
        Map<MenuItem, Integer> itemQuantities = cart.getItemQuantities();
        
        /**
         * DISPLAY EACH ITEM:
         * Loop through cart items and create labels showing:
         * - Item name
         * - Quantity
         * - Price per item
         * - Total price for that item
         */
        for (Map.Entry<MenuItem, Integer> entry : itemQuantities.entrySet()) {
            MenuItem item = entry.getKey(); // Get MenuItem from map
            int quantity = entry.getValue(); // Get quantity from map
            double price = item.getPrice(); // Get price from MenuItem
            double itemTotal = price * quantity; // Calculate total for this item
            
            // Create label to display item information
            Label itemLabel = new Label();
            if (quantity > 1) {
                // Show quantity if more than 1
                itemLabel.setText(String.format("%s x%d - %.2f EGP each = %.2f EGP", 
                    item.getName(), quantity, price, itemTotal));
            } else {
                // Show single item without quantity
                itemLabel.setText(String.format("%s - %.2f EGP", item.getName(), price));
            }
            itemLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
            
            // Add label to container
            orderItemsContainer.getChildren().add(itemLabel);
        }
        
        /**
         * CALCULATE TOTALS:
         * - Subtotal: Sum of all item prices
         * - VAT: 14% of subtotal
         * - Delivery: Based on subtotal amount (see calculateDeliveryCost)
         * - Total: Subtotal + VAT + Delivery
         */
        double subtotal = cart.getSubtotal(); // Get subtotal from cart
        double vat = subtotal * 0.14; // Calculate 14% VAT
        double deliveryCost = calculateDeliveryCost(subtotal); // Calculate delivery cost
        double total = subtotal + vat + deliveryCost; // Calculate final total
        
        // Update labels with calculated amounts
        subtotalLabel.setText(String.format("%.2f EGP", subtotal));
        vatLabel.setText(String.format("%.2f EGP", vat));
        deliveryLabel.setText(String.format("%.2f EGP", deliveryCost));
        totalLabel.setText(String.format("%.2f EGP", total));
    }
    
    /**
     * calculateDeliveryCost() - Calculates delivery fee based on order size
     * 
     * PURPOSE:
     * Returns delivery cost based on order subtotal.
     * Larger orders get cheaper delivery fees.
     * 
     * BUSINESS LOGIC:
     * - Small orders (< 100 EGP): 35 EGP delivery
     * - Medium orders (100-200 EGP): 25 EGP delivery
     * - Large orders (> 200 EGP): 15 EGP delivery
     * 
     * @param subtotal Order subtotal amount
     * @return Delivery cost in EGP
     */
    private double calculateDeliveryCost(double subtotal) {
        if (subtotal < 100) {
            return 35.0; // Small order - higher delivery fee
        } else if (subtotal <= 200) {
            return 25.0; // Medium order - medium delivery fee
        } else {
            return 15.0; // Large order - lower delivery fee
        }
    }
    
    /**
     * handlePlaceOrder() - Event handler for "Place Order" button
     * 
     * PURPOSE:
     * Validates cart and address, creates Order with OrderItems,
     * processes payment using Strategy Pattern, and saves order to database.
     * 
     * UML CLASSES USED:
     * - Order: Creates new Order with OrderStatus, OrderItems, PaymentStrategy
     * - OrderItem: Creates OrderItems with Snapshot Pattern (priceAtPurchase)
     * - OrderStatus: Sets status to PENDING
     * - PaymentStrategy: Uses Strategy Pattern for payment processing
     * - MenuItem: Gets items from cart to create OrderItems
     * 
     * DESIGN PATTERNS:
     * - Snapshot Pattern: OrderItem captures priceAtPurchase at order time
     * - Strategy Pattern: PaymentStrategy interface for different payment methods
     * 
     * FLOW:
     * 1. Validate cart is not empty
     * 2. Validate address fields are filled
     * 3. Get user ID from email
     * 4. Get selected payment method (Cash or PayPal)
     * 5. Create Order object
     * 6. Create OrderItems with Snapshot Pattern (capture prices)
     * 7. Calculate total from OrderItems
     * 8. Process payment using Strategy Pattern
     * 9. Save order to database
     * 10. Clear cart and show success message
     */
    @FXML
    private void handlePlaceOrder() {
        /**
         * VALIDATION STEP 1: Check if cart is empty
         * If cart is empty, show warning and redirect to cart screen
         */
        if (cart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Cart");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Please add items before placing an order.");
            alert.showAndWait();
            try {
                Main.switchScene("/fxml/Cart.fxml"); // Redirect to cart
            } catch (IOException e) {
                e.printStackTrace();
            }
            return; // Exit method early
        }
        
        /**
         * VALIDATION STEP 2: Check if address fields are filled
         * All address fields must be filled for delivery
         */
        String address = addressField.getText().trim(); // Get address, remove whitespace
        String city = cityField.getText().trim(); // Get city, remove whitespace
        String phone = phoneField.getText().trim(); // Get phone, remove whitespace
        
        if (address.isEmpty() || city.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all address fields (Address, City, Phone).");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * VALIDATION STEP 3: Get current user ID
         * Need user ID to associate order with user
         */
        String userEmail = HistoryController.getCurrentUserEmail(); // Get logged-in user email
        if (userEmail == null || userEmail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Error");
            alert.setHeaderText(null);
            alert.setContentText("Please log in again.");
            alert.showAndWait();
            try {
                Main.switchScene("/fxml/Login.fxml"); // Redirect to login
            } catch (IOException e) {
                e.printStackTrace();
            }
            return; // Exit method early
        }
        
        // Get user ID from email using database
        int userId = dataService.getUserIdByEmail(userEmail);
        if (userId == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User not found.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * PAYMENT METHOD SELECTION:
         * Get selected payment method from radio buttons.
         * Uses Strategy Pattern - PaymentStrategy interface.
         */
        PaymentStrategy paymentStrategy; // UML: PaymentStrategy interface
        String paymentMethod; // String for display purposes
        
        if (paypalRadio.isSelected()) {
            // User selected PayPal - use PayPalAdapter (implements PaymentStrategy)
            paymentStrategy = new PayPalAdapter();
            paymentMethod = "PayPal";
        } else {
            // User selected Cash - use CashPayment (implements PaymentStrategy)
            paymentStrategy = new CashPayment();
            paymentMethod = "Cash";
        }
        
        /**
         * CALCULATE FINAL TOTALS:
         * Calculate subtotal, VAT, delivery, and final total
         */
        double subtotal = cart.getSubtotal(); // Get subtotal from cart
        double vat = subtotal * 0.14; // Calculate 14% VAT
        double deliveryCost = calculateDeliveryCost(subtotal); // Calculate delivery
        double totalPrice = subtotal + vat + deliveryCost; // Calculate final total
        
        /**
         * CREATE ORDER OBJECT:
         * Create new Order object with required fields.
         * UML: Order has id, status, items, paymentMethod, totalAmount
         */
        Order order = new Order(); // Create new Order object
        order.setUserId(userId); // Set user who placed order
        order.setStatus(OrderStatus.PENDING); // UML: Set status to PENDING enum
        order.setPaymentMethod(paymentStrategy); // UML: Set PaymentStrategy (Strategy Pattern)
        
        /**
         * CRITICAL: CREATE ORDERITEMS WITH SNAPSHOT PATTERN
         * 
         * SNAPSHOT PATTERN EXPLANATION:
         * When an order is placed, we capture the price of each menu item
         * at that moment (priceAtPurchase). This is critical because:
         * - Menu prices might change later
         * - Historical orders should show the price at time of purchase
         * - OrderItem stores priceAtPurchase, not the live price
         * 
         * FLOW:
         * 1. Get items and quantities from cart
         * 2. For each item, capture current price (priceAtPurchase)
         * 3. Create OrderItem with item, quantity, and frozen price
         * 4. Add OrderItem to Order
         */
        Map<MenuItem, Integer> itemQuantities = cart.getItemQuantities(); // Get items from cart
        for (Map.Entry<MenuItem, Integer> entry : itemQuantities.entrySet()) {
            MenuItem item = entry.getKey(); // Get MenuItem from cart
            int quantity = entry.getValue(); // Get quantity from cart
            double priceAtPurchase = item.getPrice(); // CRITICAL: Capture price NOW (Snapshot Pattern)
            
            // Create OrderItem with Snapshot Pattern
            // OrderItem stores priceAtPurchase, not live price
            OrderItem orderItem = new OrderItem(item, quantity, priceAtPurchase);
            
            // Add OrderItem to Order
            order.addOrderItem(orderItem);
        }
        
        /**
         * CALCULATE TOTAL FROM ORDERITEMS:
         * Order.calculateTotal() uses priceAtPurchase from OrderItems,
         * not the live MenuItem prices. This ensures historical accuracy.
         */
        double calculatedTotal = order.calculateTotal(); // Calculate from OrderItems (uses priceAtPurchase)
        double finalTotal = calculatedTotal + vat + deliveryCost; // Add VAT and delivery
        order.setTotalAmount(finalTotal); // Set final total in Order
        
        /**
         * PROCESS PAYMENT USING STRATEGY PATTERN:
         * Order.pay() calls PaymentStrategy.pay() method.
         * Different payment strategies (Cash, PayPal) implement this differently.
         */
        boolean paymentSuccess = order.pay(); // Process payment (returns true if successful)
        if (!paymentSuccess) {
            // Payment failed - show error and exit
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Payment Failed");
            alert.setHeaderText(null);
            alert.setContentText("Payment processing failed. Please try again.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * SAVE ORDER TO DATABASE:
         * Save Order and OrderItems to database.
         * Returns order ID if successful, -1 if failed.
         */
        int orderId = dataService.saveOrder(order);
        
        /**
         * VALIDATION: Check if order was saved successfully
         * If orderId is -1, order save failed
         */
        if (orderId == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Order Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save order. Please try again.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * CLEANUP AFTER SUCCESSFUL ORDER:
         * Clear cart and restaurant selection since order is complete
         */
        cart.clear(); // Clear shopping cart
        RestaurantSelectionController.clearSelection(); // Clear restaurant selection
        
        /**
         * SHOW SUCCESS MESSAGE:
         * Display order confirmation with order ID and details
         */
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Order Placed Successfully!");
        alert.setHeaderText("Your order has been confirmed!");
        alert.setContentText(String.format(
            "Order #%d\n\n" +
            "Payment Method: %s\n" +
            "Subtotal: %.2f EGP\n" +
            "VAT (14%%): %.2f EGP\n" +
            "Delivery: %.2f EGP\n" +
            "Total Amount: %.2f EGP\n\n" +
            "Delivery Address:\n%s, %s\nPhone: %s\n\n" +
            "Status: PENDING\n" +
            "You can track your order in Order History.\n\n" +
            "Thank you for your order!",
            orderId, paymentMethod, subtotal, vat, deliveryCost, totalPrice,
            address, city, phone
        ));
        alert.showAndWait();
        
        /**
         * NAVIGATE BACK TO RESTAURANT SELECTION:
         * After successful order, return to restaurant selection screen
         */
        try {
            Main.switchScene("/fxml/RestaurantSelection.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * handleBackToCart() - Event handler for back button
     * 
     * PURPOSE:
     * Returns user to cart screen without placing order.
     * 
     * FLOW:
     * 1. User clicks back button
     * 2. Navigate to Cart.fxml screen
     */
    @FXML
    private void handleBackToCart() {
        try {
            Main.switchScene("/fxml/Cart.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
