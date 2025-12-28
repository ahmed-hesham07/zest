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
        
        // Try to load image from resources
        try {
            String path = "/images/" + item.getImageUrl();
            itemImage.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            System.out.println("No image found for " + item.getName());
        }
    }
}