package com.zest.controller;

import com.zest.model.MenuItem;
import com.zest.logic.CartManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartItemController {
    @FXML private ImageView itemImage; // Match fx:id
    @FXML private Label nameLabel;     // Match fx:id
    @FXML private Label priceLabel;    // Match fx:id

    private MenuItem item;
    private CartController parentController;

    public void setData(MenuItem item, CartController parent) {
        this.item = item;
        this.parentController = parent;
        nameLabel.setText(item.getName());
        priceLabel.setText(item.getPrice() + " EGP");
        
        try {
            String path = "/images/" + item.getImageUrl();
            itemImage.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            System.err.println("Cart image not found: " + item.getName());
        }
    }

    @FXML
    private void handleRemove() {
        // Remove from the Singleton memory
        CartManager.getInstance().removeItem(item);
        // Tell the main Cart screen to refresh the list
        parentController.refreshCart();
    }
}