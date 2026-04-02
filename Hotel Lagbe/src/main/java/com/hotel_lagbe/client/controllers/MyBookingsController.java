package com.hotel_lagbe.client.controllers;

import com.hotel_lagbe.client.network.ServerConnection;
import com.hotel_lagbe.shared.models.Booking;
import com.hotel_lagbe.shared.network.MessageType;
import com.hotel_lagbe.shared.network.Request;
import com.hotel_lagbe.shared.network.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MyBookingsController {

    @FXML private VBox bookingsContainer;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        if (LoginController.loggedInUser == null) {
            statusLabel.setText("You must be logged in to view bookings.");
            return;
        }
        //test
        // Ask the server for the user's bookings
        Request req = new Request(MessageType.GET_MY_BOOKINGS, LoginController.loggedInUser.getUsername());
        Response serverReply = ServerConnection.getInstance().sendRequest(req);

        if (serverReply.isSuccess()) {
            List<Booking> myBookings = (List<Booking>) serverReply.getPayload();
            displayBookings(myBookings);
        } else {
            statusLabel.setText("Failed to load bookings: " + serverReply.getMessage());
        }
    }

    private void displayBookings(List<Booking> bookings) {
        bookingsContainer.getChildren().clear();

        if (bookings == null || bookings.isEmpty()) {
            Label noBookings = new Label("You have no bookings yet. Time to plan a trip!");
            noBookings.setFont(Font.font("Nirmala UI", 16));
            bookingsContainer.getChildren().add(noBookings);
            return;
        }

        for (Booking booking : bookings) {
            HBox card = new HBox(20);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
            card.setAlignment(Pos.CENTER_LEFT);

            VBox infoBox = new VBox(8);
            Label hotelLabel = new Label(booking.getHotelName());
            hotelLabel.setFont(Font.font("Nirmala UI", FontWeight.BOLD, 20));
            hotelLabel.setStyle("-fx-text-fill: #000080;");

            Label datesLabel = new Label("Dates: " + booking.getCheckInDate() + " to " + booking.getCheckOutDate());
            datesLabel.setFont(Font.font("Nirmala UI", 14));

            Label roomLabel = new Label("Room: " + booking.getBookedRoom().getRoomDetails());
            roomLabel.setFont(Font.font("Nirmala UI", 14));

            infoBox.getChildren().addAll(hotelLabel, datesLabel, roomLabel);
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            VBox priceBox = new VBox(10); // Added spacing for the button
            priceBox.setAlignment(Pos.CENTER_RIGHT);

            Label priceLabel = new Label(String.format("Total: ৳ %,.0f", booking.getTotalPrice()));
            priceLabel.setFont(Font.font("Nirmala UI", FontWeight.BOLD, 18));
            priceLabel.setStyle("-fx-text-fill: #27AE60;");

            // --- NEW CANCEL BUTTON ---
            javafx.scene.control.Button cancelBtn = new javafx.scene.control.Button("Cancel Booking");
            cancelBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #e74c3c; -fx-text-fill: #e74c3c; -fx-border-radius: 5; -fx-cursor: hand;");
            cancelBtn.setOnAction(e -> sendCancelRequest(booking.getBookingId()));

            priceBox.getChildren().addAll(priceLabel, cancelBtn);
            card.getChildren().addAll(infoBox, priceBox);

            bookingsContainer.getChildren().add(card);
        }
    }

    // --- NEW CANCEL HANDLER METHOD ---
    private void sendCancelRequest(String bookingId) {
        Request req = new Request(MessageType.CANCEL_BOOKING, bookingId);
        Response serverReply = ServerConnection.getInstance().sendRequest(req);

        if (serverReply.isSuccess()) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Booking cancelled successfully!");
            // Reload the page to show updated list
            initialize();
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Failed to cancel: " + serverReply.getMessage());
        }
    }

    @FXML
    private void handleBackToSearch(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SearchView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}