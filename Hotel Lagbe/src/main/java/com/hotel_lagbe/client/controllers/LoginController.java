//package com.hotel_lagbe.client.controllers;
//
//import com.hotel_lagbe.client.network.ServerConnection;
//import com.hotel_lagbe.shared.models.User;
//import com.hotel_lagbe.shared.network.MessageType;
//import com.hotel_lagbe.shared.network.Request;
//import com.hotel_lagbe.shared.network.Response;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class LoginController {
//
//    public static User loggedInUser;
//
//    @FXML private TextField usernameField;
//    @FXML private PasswordField passwordField;
//    @FXML private Label messageLabel;
//
//    @FXML
//    public void handleLogin(ActionEvent event) {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showMessage("Please enter both username and password.", false);
//            return;
//        }
//
//        User loginUser = new User(username, password);
//        Request loginRequest = new Request(MessageType.LOGIN, loginUser);
//
//        ServerConnection connection = ServerConnection.getInstance();
//        Response serverReply = connection.sendRequest(loginRequest);
//
//        if (serverReply.isSuccess()) {
//            loggedInUser = loginUser;
//
//            showMessage("Login Successful!", true);
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/LocationInputView.fxml"));
//                Parent root = loader.load();
//                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//                showMessage("Error loading Location Input page.", false);
//            }
//        } else {
//            showMessage(serverReply.getMessage(), false);
//        }
//    }
//
//    @FXML
//    public void switchToSignUp(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SignUpView.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showMessage("Error loading Sign Up page.", false);
//        }
//    }
//
//    private void showMessage(String message, boolean isSuccess) {
//        messageLabel.setText(message);
//        if (isSuccess) {
//            messageLabel.setTextFill(Color.GREEN);
//        } else {
//            messageLabel.setTextFill(Color.RED);
//        }
//    }
//}
package com.hotel_lagbe.client.controllers;

import com.hotel_lagbe.client.network.ServerConnection;
import com.hotel_lagbe.shared.models.User;
import com.hotel_lagbe.shared.network.MessageType;
import com.hotel_lagbe.shared.network.Request;
import com.hotel_lagbe.shared.network.Response;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

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

    public static User loggedInUser;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password.", false);
            return;
        }

        User loginUser = new User(username, password);
        Request loginRequest = new Request(MessageType.LOGIN, loginUser);

        ServerConnection connection = ServerConnection.getInstance();
        Response serverReply = connection.sendRequest(loginRequest);

        if (serverReply.isSuccess()) {
            loggedInUser = loginUser;
            showMessage("Login Successful!", true);

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

            if (serverReply.getMessage().contains("wait 30 seconds")) {

                PauseTransition delay = new PauseTransition(Duration.seconds(30));

                delay.setOnFinished(e -> {
                    if (messageLabel.getText().equals(serverReply.getMessage())) {
                        showMessage("You can try logging in again.", true);
                    }
                });

                delay.play();
            }
        }
    }

    @FXML
    public void switchToSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SignUpView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading Sign Up page.", false);
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