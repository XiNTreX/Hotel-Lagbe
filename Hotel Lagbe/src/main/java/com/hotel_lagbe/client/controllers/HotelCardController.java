package com.hotel_lagbe.client.controllers;

import com.hotel_lagbe.shared.models.HotelResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HotelCardController {

    @FXML private HBox hotelCard;
    @FXML private ImageView hotelImageView;
    @FXML private Label hotelNameLabel;
    @FXML private Label hotelAddressLabel;
    @FXML private Label hotelRatingLabel;
    @FXML private Label hotelStatusLabel;
    @FXML private Button viewDetailsButton; // Price label removed from here

    private HotelResult hotelResult;

    public void setHotelData(HotelResult hotel) {
        this.hotelResult = hotel;

        String hotelName = hotel.getName() != null ? hotel.getName() : "Unknown Hotel";
        hotelNameLabel.setText(hotelName);
        hotelAddressLabel.setText(hotel.getAddress() != null ? hotel.getAddress() : "Address not available");

        int hash = Math.abs(hotelName.hashCode());

        // 1. Dynamic Rating Logic
        if (hotel.getRating() > 0) {
            hotelRatingLabel.setText("⭐ " + String.format("%.1f", hotel.getRating()));
        } else {
            double mockRating = 3.5 + ((hash % 15) / 10.0);
            hotelRatingLabel.setText("⭐ " + String.format("%.1f", mockRating));
        }
        hotelRatingLabel.setStyle("-fx-text-fill: #FF9500; -fx-font-weight: bold;");

        // 2. Dynamic Status
        if (hotel.isOpenNow()) {
            hotelStatusLabel.setText("✅ Open Now");
            hotelStatusLabel.setStyle("-fx-text-fill: #27AE60;");
        } else {
            if (hash % 10 == 0) {
                hotelStatusLabel.setText("❌ Closed");
                hotelStatusLabel.setStyle("-fx-text-fill: #E74C3C;");
            } else {
                hotelStatusLabel.setText("✅ Open Now");
                hotelStatusLabel.setStyle("-fx-text-fill: #27AE60;");
            }
        }

        // 3. Image Loading (Price logic removed completely)
        loadOptimizedImage(hotel, hash);
    }

    private void loadOptimizedImage(HotelResult hotel, int hash) {
        String photoUrl = hotel.getPhotoUrl();

        if (photoUrl != null && !photoUrl.isEmpty()) {
            try {
                Image img = new Image(photoUrl, 190, 160, false, true, true);
                if (!img.isError()) {
                    hotelImageView.setImage(img);
                    return;
                }
            } catch (Exception ignored) {}
        }

        int imageId = 1010 + (hash % 40);
        String fallbackUrl = "https://picsum.photos/id/" + imageId + "/400/300";
        try {
            Image fallback = new Image(fallbackUrl, 190, 160, false, true, true);
            hotelImageView.setImage(fallback);
        } catch (Exception e) {
            hotelImageView.setStyle("-fx-background-color: #E5E4E2;");
        }
    }

    @FXML
    private void handleViewDetails(javafx.event.ActionEvent event) {
        if (hotelResult != null) {
            SearchController.selectedHotel = hotelResult;

            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/HotelDetailsView.fxml"));
                javafx.scene.Parent root = loader.load();

                javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new javafx.scene.Scene(root));
                stage.show();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}