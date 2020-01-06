module openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires common;


    opens controllers;
    opens controllers.product;
    opens controllers.staff;
    opens controllers.client;
    opens controllers.settings;

    opens openjfx to javafx.fxml;
    exports openjfx;
}