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
    @FXML private Label hotelPriceLabel;
    @FXML private Button viewDetailsButton;

    private HotelResult hotelResult;

    public void setHotelData(HotelResult hotel) {
        this.hotelResult = hotel;

        hotelNameLabel.setText(hotel.getName() != null ? hotel.getName() : "Unknown Hotel");
        hotelAddressLabel.setText(hotel.getAddress() != null ? hotel.getAddress() : "Address not available");

        // Improved Rating Display
        if (hotel.getRating() > 0) {
            hotelRatingLabel.setText("⭐ " + String.format("%.1f", hotel.getRating()));
            hotelRatingLabel.setStyle("-fx-text-fill: #FF9500; -fx-font-weight: bold;");
        } else {
            hotelRatingLabel.setText("⭐ New Property");
            hotelRatingLabel.setStyle("-fx-text-fill: #888888;");
        }

        // Status
        if (hotel.isOpenNow()) {
            hotelStatusLabel.setText("✅ Open Now");
            hotelStatusLabel.setStyle("-fx-text-fill: #27AE60;");
        } else {
            hotelStatusLabel.setText("❌ Closed");
            hotelStatusLabel.setStyle("-fx-text-fill: #E74C3C;");
        }

        hotelPriceLabel.setText(getPriceLabel(hotel.getPriceLevel()));

        // Optimized Image Loading for smoother scrolling
        loadOptimizedImage(hotel);
    }

    private void loadOptimizedImage(HotelResult hotel) {
        String photoUrl = hotel.getPhotoUrl();

        // Try Geoapify photo
        if (photoUrl != null && !photoUrl.isEmpty()) {
            try {
                Image img = new Image(photoUrl, 190, 160, false, true, true);
                if (!img.isError()) {
                    hotelImageView.setImage(img);
                    return;
                }
            } catch (Exception ignored) {}
        }

        // Reliable fallback using Picsum (fast + cached)
        String fallbackUrl = "https://picsum.photos/id/" + getImageId(hotel.getName()) + "/400/300";
        try {
            Image fallback = new Image(fallbackUrl, 190, 160, false, true, true);
            hotelImageView.setImage(fallback);
        } catch (Exception e) {
            hotelImageView.setStyle("-fx-background-color: #E5E4E2;");
        }
    }

    private int getImageId(String hotelName) {
        if (hotelName == null || hotelName.isEmpty()) return 1015;
        return 100 + (Math.abs(hotelName.hashCode()) % 900);
    }

    private String getPriceLabel(int priceLevel) {
        return switch (priceLevel) {
            case 1 -> "৳ 2,000 - 4,000 / night";
            case 2 -> "৳ 4,000 - 8,000 / night";
            case 3 -> "৳ 8,000 - 15,000 / night";
            case 4 -> "৳ 15,000+ / night";
            default -> "Price on request";
        };
    }

    @FXML
    private void handleViewDetails(javafx.event.ActionEvent event) {
        if (hotelResult != null) {
            System.out.println("Navigating to details for: " + hotelResult.getName());
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