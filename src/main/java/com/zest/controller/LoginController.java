package com.zest.controller;

/**
 * LoginController - User Authentication Controller
 * 
 * PURPOSE:
 * This controller handles user login functionality. Users enter their email
 * and password, and the system authenticates them against the database.
 * 
 * UML CLASSES USED:
 * - User: Abstract class - authentication uses User.authenticate() method
 * - Customer: Extends User - customers can log in
 * - Merchant: Extends User - merchants can log in
 * 
 * AUTHENTICATION FLOW:
 * 1. User enters email and password
 * 2. System validates credentials against database
 * 3. If valid, user is logged in and redirected to restaurant selection
 * 4. If invalid, error message is displayed
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: User Management & Authentication
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.dao.DataService; // Data access layer for authentication
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.scene.control.Label; // Text label for error messages
import javafx.scene.control.PasswordField; // Password input field (hides characters)
import javafx.scene.control.TextField; // Text input field for email
import java.io.IOException; // Exception handling

/**
 * Controller for login screen
 * Handles user authentication and navigation to restaurant selection
 */
public class LoginController {

    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from Login.fxml when screen loads
     */
    
    /**
     * emailField - Text field for user email input
     * PURPOSE: User enters their email address to log in
     * CONNECTS TO: Login.fxml fx:id="emailField"
     */
    @FXML private TextField emailField;
    
    /**
     * passwordField - Password field for user password input
     * PURPOSE: User enters their password (characters are hidden)
     * CONNECTS TO: Login.fxml fx:id="passwordField"
     */
    @FXML private PasswordField passwordField;
    
    /**
     * errorLabel - Label for displaying error messages
     * PURPOSE: Shows error messages if login fails or validation fails
     * CONNECTS TO: Login.fxml fx:id="errorLabel"
     */
    @FXML private Label errorLabel;

    /**
     * dataService - Data access object for database operations
     * PURPOSE: Handles authentication queries against database
     */
    private DataService dataService;

    /**
     * Constructor - Initializes data service
     * PURPOSE: Sets up data service when controller is created
     */
    public LoginController() {
        this.dataService = new DataService(); // Create data service for database access
    }

    /**
     * handleLogin() - Event handler for login button click
     * 
     * PURPOSE:
     * Validates user input, authenticates credentials against database,
     * and redirects to restaurant selection if successful.
     * 
     * UML CLASSES USED:
     * - User: Authentication uses User.authenticate() method
     * 
     * AUTHENTICATION FLOW:
     * 1. Get email and password from input fields
     * 2. Validate that both fields are filled
     * 3. Call DataService.login() to check credentials in database
     * 4. If valid, store user email in session and redirect
     * 5. If invalid, display error message
     * 
     * FLOW:
     * 1. User clicks login button
     * 2. Get email and password from fields
     * 3. Validate fields are not empty
     * 4. Call database to authenticate
     * 5. If successful, store session and navigate
     * 6. If failed, show error message
     */
    @FXML
    private void handleLogin() {
        /**
         * STEP 1: GET USER INPUT
         * Extract email and password from input fields
         */
        String email = emailField.getText(); // Get email from text field
        String password = passwordField.getText(); // Get password from password field

        /**
         * STEP 2: VALIDATE INPUT
         * Check that both email and password are provided
         * If either is empty, show error and exit
         */
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password."); // Show error message
            return; // Exit method early - don't proceed with authentication
        }

        /**
         * STEP 3: AUTHENTICATE USER
         * Call DataService.login() to check credentials against database.
         * This method queries the users table to find matching email and password.
         * 
         * UML CLASS USAGE:
         * - User.authenticate() is used internally by DataService
         * - Checks passwordHash against entered password
         */
        boolean isAuthenticated = dataService.login(email, password);

        /**
         * STEP 4: HANDLE AUTHENTICATION RESULT
         * If authentication successful, store session and redirect.
         * If authentication failed, show error message.
         */
        if (isAuthenticated) {
            /**
             * SUCCESS: User authenticated successfully
             * Store user email in session for use in other controllers
             * This allows other screens to know which user is logged in
             */
            HistoryController.setCurrentUserEmail(email); // Store email in session
            
            try {
                System.out.println("Login Successful! Opening Restaurant Selection...");
                
                /**
                 * NAVIGATE TO RESTAURANT SELECTION:
                 * After successful login, redirect user to restaurant selection screen
                 * where they can choose a restaurant before viewing menu
                 */
                Main.switchScene("/fxml/RestaurantSelection.fxml");
            } catch (IOException e) {
                /**
                 * ERROR HANDLING:
                 * If navigation fails, log error and show error message
                 */
                e.printStackTrace();
                errorLabel.setText("CRITICAL: Could not load RestaurantSelection.fxml");
            }
        } else {
            /**
             * FAILURE: Authentication failed
             * Credentials don't match any user in database
             * Show error message to user
             */
            errorLabel.setText("Invalid email or password.");
        }
    }

    /**
     * handleGoToRegister() - Event handler for register button/link
     * 
     * PURPOSE:
     * Navigates user to registration screen if they don't have an account.
     * 
     * FLOW:
     * 1. User clicks register link/button
     * 2. Navigate to Register.fxml screen
     */
    @FXML
    private void handleGoToRegister() {
        try {
            Main.switchScene("/fxml/Register.fxml"); // Navigate to registration screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error
             */
            e.printStackTrace();
            System.err.println("Could not load Register.fxml");
        }
    }
}
