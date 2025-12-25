package com.zest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Entry Point for Zest Application
 * JavaFX Application class that initializes the UI
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Login FXML file (created by Hamdy)
            // Note: This file may not exist yet, so we catch the exception
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("Zest - Food Ordering System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            // If Login.fxml doesn't exist yet, print a helpful message
            System.err.println("Waiting for Hamdy to finish Login.fxml");
            System.err.println("Error details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to launch the JavaFX application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
