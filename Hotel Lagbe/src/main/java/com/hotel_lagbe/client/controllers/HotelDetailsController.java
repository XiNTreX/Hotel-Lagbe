package com.hotel_lagbe.client.controllers;

import com.hotel_lagbe.shared.models.HotelResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class HotelDetailsController {

    @FXML private ImageView hotelImageView;
    @FXML private Label hotelNameLabel;
    @FXML private Label hotelAddressLabel;
    @FXML private Label hotelRatingLabel;
    @FXML private Label facilitiesLabel;
    @FXML private VBox roomsContainer;

    @FXML
    public void initialize() {
        // Fetch the hotel that was clicked on the previous page
        HotelResult selectedHotel = SearchController.selectedHotel;

        if (selectedHotel != null) {
            populateHotelDetails(selectedHotel);
            loadMockRooms(selectedHotel);
        } else {
            hotelNameLabel.setText("Error loading hotel details.");
        }
    }

    private void populateHotelDetails(HotelResult hotel) {
        hotelNameLabel.setText(hotel.getName());
        hotelAddressLabel.setText(hotel.getAddress());

        if (hotel.getRating() > 0) {
            hotelRatingLabel.setText("⭐ " + String.format("%.1f", hotel.getRating()));
        } else {
            hotelRatingLabel.setText("⭐ New Property (Unrated)");
        }

        // Load the image just like in HotelCardController
        String photoUrl = hotel.getPhotoUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            try {
                hotelImageView.setImage(new Image(photoUrl, 280, 200, false, true));
            } catch (Exception e) {
                loadFallbackImage(hotel.getName());
            }
        } else {
            loadFallbackImage(hotel.getName());
        }

        // Randomize facilities slightly based on price level
        if (hotel.getPriceLevel() >= 3) {
            facilitiesLabel.setText("📶 Free WiFi   🏊 Swimming Pool   🍳 Free Breakfast   🏋️ Gym   💆 Spa");
        } else {
            facilitiesLabel.setText("📶 Free WiFi   🍳 Free Breakfast   ❄️ AC   📺 Flat-screen TV");
        }
    }

    private void loadFallbackImage(String name) {
        int id = 100 + (Math.abs(name.hashCode()) % 900);
        String fallbackUrl = "https://picsum.photos/id/" + id + "/400/300";
        hotelImageView.setImage(new Image(fallbackUrl, 280, 200, false, true));
    }

    private void loadMockRooms(HotelResult hotel) {
        // Base price calculation based on the hotel's price level
        double basePrice = hotel.getPriceLevel() * 2500.0;
        if (basePrice == 0) basePrice = 3000.0;

        // Create 3 types of rooms
        roomsContainer.getChildren().add(createRoomCard("Standard Room", "1 Queen Bed • City View", basePrice));
        roomsContainer.getChildren().add(createRoomCard("Deluxe Room", "1 King Bed • Balcony • Mini Fridge", basePrice * 1.5));
        roomsContainer.getChildren().add(createRoomCard("Executive Suite", "1 King Bed • Living Area • Sea/City View", basePrice * 2.5));
    }

    private HBox createRoomCard(String type, String details, double price) {
        HBox card = new HBox(20);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-padding: 15;");
        card.setAlignment(Pos.CENTER_LEFT);

        // Room Info
        VBox infoBox = new VBox(8);
        Label typeLabel = new Label(type);
        typeLabel.setFont(Font.font("Nirmala UI", FontWeight.BOLD, 18));
        typeLabel.setStyle("-fx-text-fill: #000080;");

        Label detailsLabel = new Label(details);
        detailsLabel.setFont(Font.font("Nirmala UI", 14));
        detailsLabel.setStyle("-fx-text-fill: #555555;");

        infoBox.getChildren().addAll(typeLabel, detailsLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Price and Button
        VBox actionBox = new VBox(10);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Label priceLabel = new Label(String.format("৳ %,.0f / night", price));
        priceLabel.setFont(Font.font("Nirmala UI", FontWeight.BOLD, 16));
        priceLabel.setStyle("-fx-text-fill: #27AE60;"); // Green color

        Button bookButton = new Button("Book Now");
        bookButton.setStyle("-fx-background-color: #fad102; -fx-text-fill: #000080; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand; -fx-pref-width: 120; -fx-pref-height: 35;");

        // This is where we will hook up the booking logic later
        bookButton.setOnAction(e -> {
            BookingConfirmationController.selectedRoomType = type;
            BookingConfirmationController.selectedRoomPricePerNight = price;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/BookingConfirmationView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        actionBox.getChildren().addAll(priceLabel, bookButton);
        card.getChildren().addAll(infoBox, actionBox);

        return card;
    }

    @FXML
    private void handleBackToSearch(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SearchView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}