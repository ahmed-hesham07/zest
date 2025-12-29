package com.zest.controller;

/**
 * RegisterController - User Registration Controller
 * 
 * PURPOSE:
 * This controller handles new user registration. Users enter their name,
 * email, and password to create a new account in the system.
 * 
 * UML CLASSES USED:
 * - User: Abstract class - new users are created as User instances
 * - Customer: Extends User - new users are registered as Customer type
 * 
 * REGISTRATION FLOW:
 * 1. User enters name, email, and password
 * 2. System validates all fields are filled
 * 3. System checks if email already exists
 * 4. If email is new, creates user account in database
 * 5. If email exists, shows error message
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: User Management & Registration
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.dao.DataService; // Data access layer for user registration
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.scene.control.Label; // Text label for error messages
import javafx.scene.control.PasswordField; // Password input field (hides characters)
import javafx.scene.control.TextField; // Text input field for name and email
import java.io.IOException; // Exception handling

/**
 * Controller for registration screen
 * Handles new user account creation
 */
public class RegisterController {

    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from Register.fxml when screen loads
     */
    
    /**
     * nameField - Text field for user name input
     * PURPOSE: User enters their full name
     * CONNECTS TO: Register.fxml fx:id="nameField"
     */
    @FXML private TextField nameField;
    
    /**
     * emailField - Text field for user email input
     * PURPOSE: User enters their email address (must be unique)
     * CONNECTS TO: Register.fxml fx:id="emailField"
     */
    @FXML private TextField emailField;
    
    /**
     * passwordField - Password field for user password input
     * PURPOSE: User enters their password (characters are hidden)
     * CONNECTS TO: Register.fxml fx:id="passwordField"
     */
    @FXML private PasswordField passwordField;
    
    /**
     * errorLabel - Label for displaying error messages
     * PURPOSE: Shows error messages if registration fails or validation fails
     * CONNECTS TO: Register.fxml fx:id="errorLabel"
     */
    @FXML private Label errorLabel;

    /**
     * dataService - Data access object for database operations
     * PURPOSE: Handles user registration queries against database
     */
    private DataService dataService;

    /**
     * Constructor - Initializes data service
     * PURPOSE: Sets up data service when controller is created
     */
    public RegisterController() {
        this.dataService = new DataService(); // Create data service for database access
    }

    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Called automatically when Register.fxml loads.
     * Ensures data service is initialized.
     * 
     * FLOW:
     * 1. JavaFX loads Register.fxml
     * 2. JavaFX creates RegisterController instance
     * 3. JavaFX injects @FXML fields
     * 4. JavaFX calls initialize()
     * 5. We ensure data service is ready
     */
    @FXML
    public void initialize() {
        // Initialize DataService if not already done (safety check)
        if (dataService == null) {
            this.dataService = new DataService();
        }
    }

    /**
     * handleRegister() - Event handler for register button click
     * 
     * PURPOSE:
     * Validates user input, checks if email exists, creates new user account,
     * and redirects to login screen if successful.
     * 
     * UML CLASSES USED:
     * - User: Abstract class - new users inherit from User
     * - Customer: Extends User - new users are created as Customer type
     * 
     * REGISTRATION FLOW:
     * 1. Get name, email, and password from input fields
     * 2. Validate that all fields are filled
     * 3. Call DataService.registerUser() to create account
     * 4. If successful, show success message and redirect to login
     * 5. If failed (email exists), show error message
     * 
     * FLOW:
     * 1. User clicks register button
     * 2. Get name, email, password from fields
     * 3. Validate fields are not empty
     * 4. Call database to register user
     * 5. If successful, show success and redirect
     * 6. If failed, show error message
     */
    @FXML
    private void handleRegister() {
        /**
         * STEP 1: GET USER INPUT
         * Extract name, email, and password from input fields
         */
        String name = nameField.getText(); // Get name from text field
        String email = emailField.getText(); // Get email from text field
        String password = passwordField.getText(); // Get password from password field

        /**
         * STEP 2: VALIDATE INPUT
         * Check that all fields (name, email, password) are provided
         * If any field is empty, show error and exit
         */
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required."); // Show error message
            return; // Exit method early - don't proceed with registration
        }

        /**
         * STEP 3: REGISTER USER IN DATABASE
         * Call DataService.registerUser() to create new user account.
         * This method:
         * - Checks if email already exists
         * - If email is new, creates user record in database
         * - If email exists, returns false
         * 
         * UML CLASS USAGE:
         * - Creates Customer instance (extends User)
         * - Stores passwordHash (not plain password)
         */
        boolean success = dataService.registerUser(name, email, password);
        
        /**
         * STEP 4: HANDLE REGISTRATION RESULT
         * If registration successful, show success message and redirect to login.
         * If registration failed (email exists), show error message.
         */
        if (success) {
            /**
             * SUCCESS: User registered successfully
             * Show success message and redirect to login screen
             */
            System.out.println("User registered: " + email);
            errorLabel.setText("Registration successful! Redirecting to login...");
            
            /**
             * SMALL DELAY BEFORE REDIRECT:
             * Give user time to read success message before redirecting
             */
            try {
                Thread.sleep(1000); // Wait 1 second
            } catch (InterruptedException e) {
                // If thread interrupted, restore interrupt status
                Thread.currentThread().interrupt();
            }
            
            // Navigate to login screen
            handleBackToLogin();
        } else {
            /**
             * FAILURE: Registration failed
             * Email already exists in database
             * Show error message to user
             */
            errorLabel.setText("Registration failed. Email may already exist.");
        }
    }

    /**
     * handleBackToLogin() - Event handler for back to login button/link
     * 
     * PURPOSE:
     * Navigates user back to login screen if they already have an account.
     * 
     * FLOW:
     * 1. User clicks back to login link/button
     * 2. Navigate to Login.fxml screen
     */
    @FXML
    private void handleBackToLogin() {
        try {
            Main.switchScene("/fxml/Login.fxml"); // Navigate to login screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error
             */
            e.printStackTrace();
        }
    }
}
