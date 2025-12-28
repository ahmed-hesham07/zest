package com.zest.controller;
import com.zest.model.MenuItem;
import javafx.fxml.FXML;
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

    public void setData(MenuItem item) {
        nameLabel.setText(item.getName());
        priceLabel.setText(item.getPrice() + " EGP");

        // Fix: Handle missing images gracefully
        try {
            String imagePath = "/images/" + item.getImageUrl();
            // Check if resource exists before creating Image to avoid NPE
            if (getClass().getResource(imagePath) != null) {
                itemImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
            } else {
                System.err.println("Image missing: " + imagePath);
                // Optional: Set a default "placeholder" image here
            }
        } catch (Exception e) {
            System.err.println("Error loading image for " + item.getName());
            e.printStackTrace();
        }
    }
}