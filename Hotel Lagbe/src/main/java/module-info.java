module com.hotellagbe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    opens com.hotel_lagbe.client.controllers to javafx.fxml;

    exports com.hotel_lagbe.client;


}