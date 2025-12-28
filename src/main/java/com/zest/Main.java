package com.zest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static Stage stg; // Static stage to allow switching from anywhere

    @Override
    public void start(Stage primaryStage) {
        stg = primaryStage;
        primaryStage.setResizable(false);
        try {
            // Start at Login
            switchScene("/fxml/Login.fxml");
            primaryStage.setTitle("Zest - Food Ordering System");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The "Engine" for navigation
     */
    public static void switchScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Main.class.getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}