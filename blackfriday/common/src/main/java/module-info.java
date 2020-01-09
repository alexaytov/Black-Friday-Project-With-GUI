module common {
    requires javafx.controls;
    requires javafx.fxml;
    exports commonMessages;
    exports product;
    exports user.interfaces;
    exports user;
    exports exceptions;
    exports validator to openjfx;
    exports connection;
    exports store.earnings;
}