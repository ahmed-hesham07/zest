package com.zest.controller;

import com.zest.model.MenuItem;
import com.zest.logic.CartManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartItemController {
    @FXML private ImageView itemImage;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Button decreaseBtn;
    @FXML private Button increaseBtn;
    @FXML private Button removeBtn;

    private MenuItem item;
    private CartController parentController;
    private CartManager cartManager = CartManager.getInstance();

    public void setData(MenuItem item, CartController parent) {
        this.item = item;
        this.parentController = parent;
        updateDisplay();
        
        try {
            String path = "/images/menu/" + item.getImageUrl();
            Image image = new Image(getClass().getResourceAsStream(path));
            itemImage.setImage(image);
            itemImage.setFitWidth(80);
            itemImage.setFitHeight(60);
            itemImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Cart image not found: " + item.getName());
        }
    }
    
    private void updateDisplay() {
        int quantity = cartManager.getItemQuantity(item);
        double itemPrice = item.getPrice();
        double totalPrice = itemPrice * quantity;
        
        nameLabel.setText(item.getName());
        priceLabel.setText(String.format("%.2f EGP", totalPrice));
        quantityLabel.setText(String.valueOf(quantity));
    }

    @FXML
    private void handleIncrease() {
        cartManager.addItem(item);
        updateDisplay();
        parentController.refreshCart();
    }
    
    @FXML
    private void handleDecrease() {
        int currentQuantity = cartManager.getItemQuantity(item);
        if (currentQuantity > 1) {
            cartManager.setItemQuantity(item, currentQuantity - 1);
        } else {
            cartManager.removeItem(item);
        }
        updateDisplay();
        parentController.refreshCart();
    }

    @FXML
    private void handleRemove() {
        cartManager.removeItem(item);
        parentController.refreshCart();
    }
}