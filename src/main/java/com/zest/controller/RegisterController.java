package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel; // Add this label to your FXML too!

    private DataService dataService;

    public RegisterController() {
        this.dataService = new DataService();
    }

    @FXML
    public void initialize() {
        // Initialize DataService if not already done
        if (dataService == null) {
            this.dataService = new DataService();
        }
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        // Register user in database
        boolean success = dataService.registerUser(name, email, password);
        
        if (success) {
            System.out.println("User registered: " + email);
            errorLabel.setText("Registration successful! Redirecting to login...");
            // Small delay before redirecting
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            handleBackToLogin();
        } else {
            errorLabel.setText("Registration failed. Email may already exist.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            Main.switchScene("/fxml/Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}