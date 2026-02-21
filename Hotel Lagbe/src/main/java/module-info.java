module com.hotellagbe {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hotel_lagbe.client.controllers to javafx.fxml;

    // Export your main packages
    exports com.hotel_lagbe.client;
    //exports com.hotel_lagbe.server;
    //exports com.hotel_lagbe.shared.models;
}