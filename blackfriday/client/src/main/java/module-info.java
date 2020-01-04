module openjfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens openjfx to javafx.fxml;
    exports openjfx;
}