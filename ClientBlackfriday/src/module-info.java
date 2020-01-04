module javaFXClient {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires javafx.media;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;

    opens application;
    opens controllers;
    opens FXML;

    opens controllers.settings;
    opens controllers.product;
    opens controllers.client;
    opens controllers.staff;
    opens FXML.settings;
    opens FXML.product;

}