package com.zest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static Stage stg; // Static reference to the main window

    @Override
    public void start(Stage primaryStage) {
        stg = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("Zest - Food Ordering System");
        
        try {
            // Correctly load the initial Login screen
            switchScene("/fxml/Login.fxml");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("CRITICAL: Could not load Login.fxml. Check the file path!");
        }
    }

    /**
     * The Smart Navigator: Handles both initial load and page switching
     */
    public static void switchScene(String fxml) throws IOException {
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        Parent pane = loader.load();

        if (stg.getScene() == null) {
            // FIRST RUN: The stage has no scene, so we create it.
            Scene scene = new Scene(pane);
            stg.setScene(scene);
        } else {
            // NAVIGATION: The scene exists, so we just replace the content (root).
            stg.getScene().setRoot(pane);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}