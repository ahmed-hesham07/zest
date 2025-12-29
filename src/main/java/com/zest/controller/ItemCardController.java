package com.zest.controller;
import com.zest.model.MenuItem;
import com.zest.logic.CartManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemCardController {

    @FXML
    private ImageView itemImage; 

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;
    
    private MenuItem currentItem;
    private CartManager cartManager = CartManager.getInstance();

    public void setData(MenuItem item) {
        this.currentItem = item;
        nameLabel.setText(item.getName());
        priceLabel.setText(String.format("%.2f EGP", item.getPrice()));

        // Handle missing images gracefully
        try {
            String imagePath = "/images/menu/" + item.getImageUrl();
            // Check if resource exists before creating Image to avoid NPE
            if (getClass().getResource(imagePath) != null) {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                itemImage.setImage(image);
                itemImage.setFitWidth(200);
                itemImage.setFitHeight(150);
                itemImage.setPreserveRatio(true);
            } else {
                System.err.println("Image missing: " + imagePath);
                // Optional: Set a default "placeholder" image here
            }
        } catch (Exception e) {
            System.err.println("Error loading image for " + item.getName());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleAddToCart() {
        if (currentItem == null) {
            return;
        }
        
        // Check if item can be added (same restaurant or empty cart)
        if (!cartManager.canAddItem(currentItem)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Different Restaurant");
            alert.setHeaderText(null);
            alert.setContentText("Your cart contains items from a different restaurant. Please complete or clear your current order first.");
            alert.showAndWait();
            return;
        }
        
        // Add item to cart
        boolean added = cartManager.addItem(currentItem);
        if (added) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Added to Cart");
            alert.setHeaderText(null);
            alert.setContentText(currentItem.getName() + " added to cart!");
            alert.showAndWait();
        }
    }
}