package com.hotel_lagbe.client.controllers;

import com.hotel_lagbe.client.network.ServerConnection;
import com.hotel_lagbe.shared.models.Booking;
import com.hotel_lagbe.shared.models.StandardRoom;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

public class BookingConfirmationController {

    @FXML private Label hotelNameLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label datesLabel;
    @FXML private Label totalPriceLabel;
    @FXML private Label statusLabel;

    public static String selectedRoomType;
    public static double selectedRoomPricePerNight;

    private long totalNights;
    private double totalPrice;

    @FXML
    public void initialize() {
        if (SearchController.selectedHotel == null || LocationInputController.currentSearchCriteria == null) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Error: Missing booking details.");
            return;
        }

        hotelNameLabel.setText(SearchController.selectedHotel.getName());
        roomTypeLabel.setText("Room: " + selectedRoomType);

        var checkIn = LocationInputController.currentSearchCriteria.getCheckInDate();
        var checkOut = LocationInputController.currentSearchCriteria.getCheckOutDate();

        datesLabel.setText("Dates: " + checkIn + " to " + checkOut);

        totalNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (totalNights <= 0) totalNights = 1; // Minimum 1 night just in case

        totalPrice = totalNights * selectedRoomPricePerNight;
        totalPriceLabel.setText(String.format("Total Price: ৳ %,.0f (%d nights)", totalPrice, totalNights));
    }

    @FXML
    private void handleConfirmBooking(ActionEvent event) {
        if (LoginController.loggedInUser == null) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("You must be logged in to book a room!");
            return;
        }

        StandardRoom bookedRoom = new StandardRoom(selectedRoomType, selectedRoomPricePerNight, 1);

        var checkIn = LocationInputController.currentSearchCriteria.getCheckInDate();
        var checkOut = LocationInputController.currentSearchCriteria.getCheckOutDate();
        String hotelName = SearchController.selectedHotel.getName();

        Booking newBooking = new Booking(LoginController.loggedInUser, bookedRoom, hotelName, checkIn, checkOut, totalPrice);
        Request bookingRequest = new Request(MessageType.BOOK_ROOM, newBooking);

        Response serverReply = ServerConnection.getInstance().sendRequest(bookingRequest);

        if (serverReply.isSuccess()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/MyBookingsView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Booking saved, but couldn't load the dashboard.");
            }
        } else {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Booking Failed: " + serverReply.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/HotelDetailsView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}