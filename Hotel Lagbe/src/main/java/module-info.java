module com.hotel_lagbe.app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hotel_lagbe.app to javafx.fxml;
    exports com.hotel_lagbe.app;
}