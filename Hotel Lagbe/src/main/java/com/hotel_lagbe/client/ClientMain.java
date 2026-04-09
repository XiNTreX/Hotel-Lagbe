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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/LoginView.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Hotel-Lagbe");
            Scene scene = new Scene(root, 900, 600);

            primaryStage.setScene(scene);

            primaryStage.setResizable(false);

            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Could not load the Log4" +
                    "" +
                    "in view. Check the FXML path.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}