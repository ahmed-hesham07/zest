package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

   
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private DataService dataService;

    public LoginController() {
        this.dataService = new DataService();
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        //Validation
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }

        //check the DB
        boolean isAuthenticated = dataService.login(email, password);

        if (isAuthenticated) {
            // Store current user email for session management
            HistoryController.setCurrentUserEmail(email);
            try {
                System.out.println("Login Successful! Opening Restaurant Selection...");
                // Navigate to restaurant selection first
                Main.switchScene("/fxml/RestaurantSelection.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("CRITICAL: Could not load RestaurantSelection.fxml");
            }
        } else {
            // for wrong credentials
            errorLabel.setText("Invalid email or password.");
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            Main.switchScene("/fxml/Register.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load Register.fxml");
        }
    }
}