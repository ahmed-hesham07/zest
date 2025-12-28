package com.zest.controller;

import com.zest.dao.DataService;
import com.zest.model.MenuItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class HomeController {

    @FXML
    private FlowPane menuContainer; // Connects to Hamdy's fx:id

    private DataService dataService = new DataService();

    @FXML
    public void initialize() {
        // Automatically runs when the window opens
        loadMenu();
    }
    @FXML
    private void handleViewCart() {
        try {
            // Use the new Navigator to go to Hamdy's Cart
            com.zest.Main.switchScene("/fxml/Cart.fxml");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() {
        // 1. Fetch items from MySQL
        List<MenuItem> items = dataService.getAllMenuItems();

        try {
            for (MenuItem item : items) {
                // 2. Load Hamdy's Card template
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemCard.fxml"));
                VBox card = loader.load();

                // 3. Inject the Whopper/Pizza data into the card
                ItemCardController cardController = loader.getController();
                cardController.setData(item);

                // 4. Add to the grid
                menuContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}