module com.hotellagbe {
    requires javafx.controls;
    requires javafx.fxml;

    // Allow JavaFX to access your new controller package
    opens com.hotel_lagbe.client.controllers to javafx.fxml;

    // Export your new main client package so the JVM can run it
    exports com.hotel_lagbe.client;

    // We will uncomment these later when we build the server and shared models
    // exports com.hotellagbe.server;
    // exports com.hotellagbe.shared.models;
}