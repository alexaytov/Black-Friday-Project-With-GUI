package controllers.product;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import controllers.staff.StaffChosenProduct;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import util.Operations;
import util.Windows;

import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;
import static validator.Validator.requireNonBlank;

public class ChangeName implements Initializable {

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXButton submitButton;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        // enables/disable submit button based on information entered in nameField
        try {
            requireNonBlank(this.nameField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
            this.submitButton.setDisable(false);
        } catch (IllegalArgumentException ex) {
            this.submitButton.setDisable(true);
        }
    }));

    @FXML
    void submit(ActionEvent event) {
        // send change product name command to server
        App.serverConnection.write(CommandNames.CHANGE_PRODUCT_NAME);
        App.serverConnection.write(this.nameField.getText());
        // shows confirmation of executed command to user
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_NAME_CHANGED_SUCCESSFUL);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_NAME_CHANGED_UNSUCCESSFUL);
        }
        // load product window
        FXMLLoader loader = Operations.loadWindow(Windows.STAFF_CHOSEN_PRODUCT_PATH, Windows.STAFF_CHOSEN_PRODUCT_WIDTH, Windows.STAFF_CHOSEN_PRODUCT_HEIGHT);
        // initialize product in StaffChosenProduct controller
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(this.nameField.getText());
        // hides this window
        this.checkIfAllDataIsValid.stop();
        this.nameField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // start data nameField data validation timeline
        this.checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        this.checkIfAllDataIsValid.play();
    }
}
