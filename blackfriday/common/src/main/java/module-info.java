module common {
    requires javafx.controls;
    requires javafx.fxml;
    exports commonMessages;
    requires jbcrypt;
    exports product;
    exports user.interfaces;
    exports user;
    exports exceptions;
    exports validator;
    exports connection;
    exports store.earnings;
    exports passwordHasher.interfaces;
}