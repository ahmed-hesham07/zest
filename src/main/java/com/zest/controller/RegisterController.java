package com.zest.controller;

import com.zest.Main;
import com.zest.dao.DataService; // You'll need to add a registerUser method here later!
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

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }


        System.out.println("User registered: " + email);
        handleBackToLogin();
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