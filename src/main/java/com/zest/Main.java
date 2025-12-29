package com.zest;

/**
 * Main - Application Entry Point
 * 
 * PURPOSE:
 * This is the main entry point for the Zest Food Ordering System JavaFX application.
 * It initializes the JavaFX application, sets up the main window, and handles
 * scene navigation between different screens (Login, Register, Menu, Cart, etc.).
 * 
 * APPLICATION FLOW:
 * 1. Application starts with main() method
 * 2. JavaFX calls start() method
 * 3. Main window (Stage) is created and configured
 * 4. Initial scene (Login.fxml) is loaded
 * 5. User navigates between screens using switchScene()
 * 
 * NAVIGATION SYSTEM:
 * - Uses switchScene() method to navigate between FXML screens
 * - Handles both initial scene creation and scene switching
 * - Maintains single Stage (window) throughout application lifecycle
 */

import javafx.application.Application; // JavaFX application base class
import javafx.fxml.FXMLLoader; // Loads FXML files
import javafx.scene.Parent; // Root node for scene graph
import javafx.scene.Scene; // Container for scene graph
import javafx.stage.Stage; // Top-level JavaFX container (window)
import java.io.IOException; // Exception handling

/**
 * Main application class extending JavaFX Application
 * Handles application initialization and scene navigation
 */
public class Main extends Application {
    
    /**
     * stg - Static reference to the main application window (Stage)
     * 
     * PURPOSE:
     * Stores reference to the primary Stage so it can be accessed
     * from static methods (like switchScene()) for scene navigation.
     * 
     * SCOPE: Static so it can be accessed from static methods
     * LIFETIME: Exists for entire application lifecycle
     */
    private static Stage stg;

    /**
     * start() - JavaFX application entry point
     * 
     * PURPOSE:
     * Called by JavaFX when application launches.
     * Initializes the main window and loads the initial screen (Login).
     * 
     * FLOW:
     * 1. JavaFX calls start() when application launches
     * 2. Store reference to primary Stage
     * 3. Configure window properties (title, resizable)
     * 4. Load initial scene (Login.fxml)
     * 5. Show window to user
     * 
     * @param primaryStage The primary Stage (window) created by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        stg = primaryStage; // Store reference to primary Stage
        
        /**
         * CONFIGURE WINDOW PROPERTIES:
         * Set window title and disable resizing for consistent UI
         */
        primaryStage.setResizable(false); // Prevent window resizing
        primaryStage.setTitle("Zest - Food Ordering System"); // Set window title
        
        try {
            /**
             * LOAD INITIAL SCREEN:
             * Load Login.fxml as the first screen users see
             * switchScene() handles creating the scene and setting it on the stage
             */
            switchScene("/fxml/Login.fxml"); // Load login screen
            primaryStage.show(); // Display the window to user
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If Login.fxml cannot be loaded, log error
             * Application will not start if this fails
             */
            e.printStackTrace();
            System.err.println("CRITICAL: Could not load Login.fxml. Check the file path!");
        }
    }

    /**
     * switchScene() - Navigates between FXML screens
     * 
     * PURPOSE:
     * Handles navigation between different screens in the application.
     * Can be called from any controller to switch to a different screen.
     * 
     * NAVIGATION FLOW:
     * 1. Load FXML file using FXMLLoader
     * 2. Get Parent node from loaded FXML
     * 3. If Stage has no Scene (first run), create new Scene
     * 4. If Stage has Scene (navigation), replace root node
     * 5. Scene is automatically displayed
     * 
     * USAGE:
     * Called from controllers like:
     * - Main.switchScene("/fxml/Home.fxml") - Navigate to menu
     * - Main.switchScene("/fxml/Cart.fxml") - Navigate to cart
     * - Main.switchScene("/fxml/Login.fxml") - Navigate to login
     * 
     * @param fxml Path to FXML file (e.g., "/fxml/Login.fxml")
     * @throws IOException If FXML file cannot be loaded
     */
    public static void switchScene(String fxml) throws IOException {
        /**
         * STEP 1: LOAD FXML FILE
         * Use FXMLLoader to load FXML file from resources
         * FXML files are in src/main/resources/fxml/ directory
         */
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml)); // Create loader
        Parent pane = loader.load(); // Load FXML and get root node

        /**
         * STEP 2: CHECK IF SCENE EXISTS
         * If Stage has no Scene, this is the first screen (initial load)
         * If Stage has Scene, this is navigation (switching screens)
         */
        if (stg.getScene() == null) {
            /**
             * FIRST RUN: Create new Scene
             * Stage has no Scene yet, so create one with loaded FXML
             */
            Scene scene = new Scene(pane); // Create new Scene with loaded FXML
            stg.setScene(scene); // Set Scene on Stage
        } else {
            /**
             * NAVIGATION: Replace root node
             * Stage already has Scene, just replace the root node
             * This is more efficient than creating new Scene
             */
            stg.getScene().setRoot(pane); // Replace root node with new FXML
        }
    }

    /**
     * main() - Application entry point
     * 
     * PURPOSE:
     * Standard Java entry point. Launches JavaFX application.
     * 
     * FLOW:
     * 1. Java calls main() method
     * 2. launch() starts JavaFX application
     * 3. JavaFX calls start() method
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
