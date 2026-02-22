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

public class SignUpController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleSignUp(ActionEvent event) {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields.", false);
            return;
        }

        // 1. Create the new User object with all THREE pieces of data
        User newUser = new User(fullName, username, password);
        Request signUpRequest = new Request(MessageType.SIGN_UP, newUser);

        // 2. Send through the tunnel
        ServerConnection connection = ServerConnection.getInstance();
        Response serverReply = connection.sendRequest(signUpRequest);

        // 3. Check what the server said
        if (serverReply.isSuccess()) {
            showMessage("Account created! You can now go back and log in.", true);
            // Optionally clear the fields so it looks nice
            fullNameField.clear();
            usernameField.clear();
            passwordField.clear();
        } else {
            showMessage(serverReply.getMessage(), false);
        }
    }

    @FXML
    public void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading Login page.", false);
        }
    }

    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        if (isSuccess) {
            messageLabel.setTextFill(Color.GREEN);
        } else {
            messageLabel.setTextFill(Color.RED);
        }
    }
}