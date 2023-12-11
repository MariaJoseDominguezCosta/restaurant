module com.example{
    
    requires javafx.fxml;

    requires javafx.controls;

    exports com.example;
    exports com.example.controllers;

    opens com.example to javafx.fxml;
    opens com.example.controllers to javafx.fxml;
}