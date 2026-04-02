package com.hotel_lagbe.client.controllers;

import com.hotel_lagbe.client.network.ServerConnection;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.MessageType;
import com.hotel_lagbe.shared.network.Request;
import com.hotel_lagbe.shared.network.Response;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    // Track the logged-in user so we can assign bookings to them later
    public static User loggedInUser;

    // Linking directly to the fx:id names in your LoginView.fxml
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    // --- Action: Clicking "Sign in" ---
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password.", false);
            return;
        }

        // 1. Package the login attempt
        User loginUser = new User(username, password);
        Request loginRequest = new Request(MessageType.LOGIN, loginUser);

        // 2. Send through our network tunnel
        ServerConnection connection = ServerConnection.getInstance();
        Response serverReply = connection.sendRequest(loginRequest);

        // 3. React to the Server's response
        if (serverReply.isSuccess()) {
            // Save the user session globally!
            loggedInUser = loginUser;

            showMessage("Login Successful!", true);
            // Switch to location input view
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/LocationInputView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error loading Location Input page.", false);
            }
        } else {
            showMessage(serverReply.getMessage(), false);
        }
    }

    // --- Action: Clicking "Sign Up" ---
    @FXML
    public void switchToSignUp(ActionEvent event) {
        try {
            // 1. Find the new FXML page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SignUpView.fxml"));
            Parent root = loader.load();

            // 2. Get the current window (Stage) from the button that was clicked
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Swap the scene to the new page
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading Sign Up page.", false);
        }
    }

    // Helper method to change the label text and color easily
    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        if (isSuccess) {
            messageLabel.setTextFill(Color.GREEN);
        } else {
            messageLabel.setTextFill(Color.RED);
        }
    }
}