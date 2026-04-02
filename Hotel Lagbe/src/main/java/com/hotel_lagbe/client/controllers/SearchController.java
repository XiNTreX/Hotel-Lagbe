package com.hotel_lagbe.client.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel_lagbe.client.services.HotelSearchService;
import com.hotel_lagbe.shared.models.HotelResult;
import com.hotel_lagbe.shared.models.SearchCriteria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SearchController {

    @FXML private TextField locationField;
    @FXML private DatePicker checkin;
    @FXML private DatePicker checkout;

    @FXML private ScrollPane hotellistcontainer;
    @FXML private VBox cardContainer;
    @FXML private Label statusLabel;

    // Suggestion Popup
    private Popup suggestionPopup;
    private ListView<String> suggestionListView;
    private ObservableList<String> allLocations;

    public static HotelResult selectedHotel;

    private final HotelSearchService hotelSearchService = new HotelSearchService();

    @FXML
    public void initialize() {
        // Load locations for suggestion
        loadLocationSuggestions();

        // Pre-fill from previous search
        SearchCriteria criteria = LocationInputController.currentSearchCriteria;
        if (criteria != null) {
            locationField.setText(criteria.getLocation());
            checkin.setValue(criteria.getCheckInDate());
            checkout.setValue(criteria.getCheckOutDate());
            loadHotels(criteria.getLocation());
        } else {
            showStatus("No search criteria found.");
        }

        // Setup suggestion popup
        setupSuggestionPopup();
    }

    private void loadLocationSuggestions() {
        CompletableFuture.supplyAsync(this::fetchLocationsFromAPI)
                .thenAccept(locations -> Platform.runLater(() -> {
                    allLocations = FXCollections.observableArrayList(locations);
                    System.out.println("Loaded " + locations.size() + " locations for suggestions on Search page.");
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        allLocations = FXCollections.observableArrayList(
                                "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Sylhet", "Barisal", "Rangpur",
                                "Cox's Bazar", "Bandarban", "Saint Martin", "Kuakata", "Sundarbans"
                        );
                    });
                    return null;
                });
    }

    private void setupSuggestionPopup() {
        suggestionListView = new ListView<>();
        suggestionPopup = new Popup();
        suggestionPopup.getContent().add(suggestionListView);
        suggestionPopup.setAutoHide(true);

        // Text change listener for suggestions
        locationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (allLocations == null || newValue.isEmpty()) {
                suggestionPopup.hide();
                return;
            }

            List<String> filtered = allLocations.stream()
                    .filter(loc -> loc.toLowerCase().startsWith(newValue.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());

            if (!filtered.isEmpty()) {
                suggestionListView.setItems(FXCollections.observableArrayList(filtered));
                suggestionListView.setPrefWidth(locationField.getWidth());
                suggestionListView.setPrefHeight(Math.min(filtered.size() * 24, 120));

                double x = locationField.localToScreen(locationField.getBoundsInLocal()).getMinX();
                double y = locationField.localToScreen(locationField.getBoundsInLocal()).getMaxY();
                suggestionPopup.show(locationField.getScene().getWindow(), x, y);
            } else {
                suggestionPopup.hide();
            }
        });

        // Click on suggestion
        suggestionListView.setOnMouseClicked(event -> {
            String selected = suggestionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                locationField.setText(selected);
                suggestionPopup.hide();
            }
        });

        // Enter key selects first suggestion
        locationField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !suggestionListView.getItems().isEmpty()) {
                locationField.setText(suggestionListView.getItems().get(0));
                suggestionPopup.hide();
            }
        });
    }

    private List<String> fetchLocationsFromAPI() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://countriesnow.space/api/v0.1/countries/cities/q?country=bangladesh"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode data = root.get("data");
                if (data != null && data.isArray()) {
                    List<String> locations = new ArrayList<>();
                    for (JsonNode city : data) {
                        locations.add(city.asText());
                    }
                    return locations;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @FXML
    private void handleSearch() {
        String location = locationField.getText().trim();
        LocalDate checkInDate = checkin.getValue();
        LocalDate checkOutDate = checkout.getValue();

        if (location.isEmpty()) {
            showStatus("Please enter a location.");
            return;
        }
        if (checkInDate == null || checkOutDate == null) {
            showStatus("Please select check-in and check-out dates.");
            return;
        }
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            showStatus("Check-out must be after check-in.");
            return;
        }

        // Update criteria
        if (LocationInputController.currentSearchCriteria != null) {
            LocationInputController.currentSearchCriteria = new SearchCriteria(
                    location, checkInDate, checkOutDate,
                    LocationInputController.currentSearchCriteria.getTravelTypes());
        }

        loadHotels(location);
    }

    private void loadHotels(String location) {
        cardContainer.getChildren().clear();
        showStatus("Searching for hotels in " + location + "...");

        CompletableFuture.supplyAsync(() -> hotelSearchService.searchHotels(location, 15000))
                .thenAccept(hotels -> Platform.runLater(() -> displayHotels(hotels)))
                .exceptionally(ex -> {
                    Platform.runLater(() -> showStatus("Error loading hotels. Please try again."));
                    ex.printStackTrace();
                    return null;
                });
    }

    private void displayHotels(List<HotelResult> hotels) {
        cardContainer.getChildren().clear();

        if (hotels == null || hotels.isEmpty()) {
            showStatus("No hotels found in this area. Try another location.");
            return;
        }

        statusLabel.setVisible(false);

        for (HotelResult hotel : hotels) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/hotel_lagbe/views/HotelCard.fxml"));
                Node cardNode = loader.load();

                HotelCardController cardController = loader.getController();
                cardController.setHotelData(hotel);

                VBox wrapper = new VBox(cardNode);
                wrapper.setStyle("-fx-padding: 8 0 8 0;");
                cardContainer.getChildren().add(wrapper);

            } catch (IOException e) {
                System.err.println("Failed to load card for: " + hotel.getName());
            }
        }
    }

    private void showStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        }
    }
    @FXML
    private void goToMyBookings(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/MyBookingsView.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}