package com.hotel_lagbe.client.controllers;

import javafx.fxml.FXMLLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel_lagbe.shared.models.SearchCriteria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LocationInputController {

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker checkin;

    @FXML
    private DatePicker checkout;

    @FXML
    private CheckBox familiesCheck;

    @FXML
    private CheckBox couplesCheck;

    @FXML
    private CheckBox businessCheck;

    @FXML
    private CheckBox soloCheck;

    @FXML
    private Button searchButton;

    @FXML
    private Label errorLabel;

    private Popup suggestionPopup;
    private ListView<String> suggestionListView;
    private ObservableList<String> allLocations;

    // Static holder for search criteria to pass to next view
    public static SearchCriteria currentSearchCriteria;

    @FXML
    public void initialize() {
        // Fetch locations from API asynchronously
        CompletableFuture.supplyAsync(this::fetchLocationsFromAPI)
            .thenAccept(locations -> Platform.runLater(() -> {
                allLocations = FXCollections.observableArrayList(locations);
                System.out.println("Loaded " + locations.size() + " locations from API.");
            }))
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    // Fallback to static list if API fails
                    List<String> fallbackLocations = List.of(
                        "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Sylhet", "Barisal", "Rangpur",
                        "Cox's Bazar", "Bandarban", "Saint Martin", "Kuakata", "Sundarbans"
                    );
                    allLocations = FXCollections.observableArrayList(fallbackLocations);
                    System.out.println("API failed, using fallback locations. Error: " + ex.getMessage());
                });
                return null;
            });

        // Setup suggestion popup
        suggestionListView = new ListView<>();
        suggestionPopup = new Popup();
        suggestionPopup.getContent().add(suggestionListView);
        suggestionPopup.setAutoHide(true);

        // Listener for text changes in locationField
        locationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (allLocations == null || newValue.isEmpty()) {
                suggestionPopup.hide();
            } else {
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
            }
        });

        // Handle selection from suggestions
        suggestionListView.setOnMouseClicked(event -> {
            String selected = suggestionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                locationField.setText(selected);
                suggestionPopup.hide();
            }
        });

        // Handle Enter key to select first suggestion
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
        return new ArrayList<>(); // Return empty if failed
    }

//    @FXML
//    private void handleSearch() {
//        String location = locationField.getText().trim();
//        LocalDate checkInDate = checkin.getValue();
//        LocalDate checkOutDate = checkout.getValue();
//
//        // Collect selected travel types
//        List<String> travelTypes = new ArrayList<>();
//        if (familiesCheck.isSelected()) travelTypes.add("Families");
//        if (couplesCheck.isSelected()) travelTypes.add("Couples");
//        if (businessCheck.isSelected()) travelTypes.add("Business");
//        if (soloCheck.isSelected()) travelTypes.add("Solo");
//
//        // Validation
//        if (location.isEmpty()) {
//            errorLabel.setText("Please enter a location.");
//            return;
//        }
//        if (checkInDate == null) {
//            errorLabel.setText("Please select a check-in date.");
//            return;
//        }
//        if (checkOutDate == null) {
//            errorLabel.setText("Please select a check-out date.");
//            return;
//        }
//        if (checkInDate.isBefore(LocalDate.now())) {
//            errorLabel.setText("Check-in date cannot be in the past.");
//            return;
//        }
//        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
//            errorLabel.setText("Check-out date must be after check-in date.");
//            return;
//        }
//
//        // Create and store search criteria
//        currentSearchCriteria = new SearchCriteria(location, checkInDate, checkOutDate, travelTypes);
//
//        // Clear error and proceed to search results view
//        errorLabel.setText("");
//        System.out.println("Search criteria: " + location + ", " + checkInDate + " to " + checkOutDate + ", Types: " + travelTypes);
//
//        // Switch to search results view (don't implement it yet)
//        try {
//            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SearchView.fxml"));
//            javafx.scene.Parent root = loader.load();
//            javafx.stage.Stage stage = (javafx.stage.Stage) searchButton.getScene().getWindow();
//            javafx.scene.Scene scene = new javafx.scene.Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//            errorLabel.setText("Error loading search results page.");
//        }
//    }
//}
@FXML
private void handleSearch() {
    String location = locationField.getText().trim();
    LocalDate checkInDate = checkin.getValue();
    LocalDate checkOutDate = checkout.getValue();

    List<String> travelTypes = new ArrayList<>();
    if (familiesCheck.isSelected()) travelTypes.add("Families");
    if (couplesCheck.isSelected()) travelTypes.add("Couples");
    if (businessCheck.isSelected()) travelTypes.add("Business");
    if (soloCheck.isSelected()) travelTypes.add("Solo");

    // Validation
    if (location.isEmpty()) {
        errorLabel.setText("Please enter a location.");
        return;
    }
    if (checkInDate == null) {
        errorLabel.setText("Please select a check-in date.");
        return;
    }
    if (checkOutDate == null) {
        errorLabel.setText("Please select a check-out date.");
        return;
    }
    if (checkInDate.isBefore(LocalDate.now())) {
        errorLabel.setText("Check-in date cannot be in the past.");
        return;
    }
    if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
        errorLabel.setText("Check-out date must be after check-in date.");
        return;
    }

    // Save search criteria
    currentSearchCriteria = new SearchCriteria(location, checkInDate, checkOutDate, travelTypes);

    errorLabel.setText("");

    // Navigate to SearchView (Results Page)
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hotel_lagbe/views/SearchView.fxml"));
        javafx.scene.Parent root = loader.load();
        javafx.stage.Stage stage = (javafx.stage.Stage) searchButton.getScene().getWindow();
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (java.io.IOException e) {
        e.printStackTrace();
        errorLabel.setText("Error loading search results page.");
    }
}
}