package com.hotel_lagbe.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Login screen as the very first thing the user sees
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/LoginView.fxml"));
            Parent root = loader.load();

            // Set the title of the Mac window and size it to match our FXML
            primaryStage.setTitle("Hotel-Lagbe");
            Scene scene = new Scene(root, 900, 600);

            primaryStage.setScene(scene);

            // Prevent resizing so our beautiful layout doesn't get messed up
            primaryStage.setResizable(false);

            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Could not load the Login view. Check the FXML path.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // This is the trigger that starts the JavaFX application lifecycle
        launch(args);
    }
}