package controllers.product;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import openjfx.Main;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static validator.Validator.requireNonBlank;

public class ChangeName implements Initializable {

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXButton submitButton;

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("change product name");
        Main.store.getOos().writeObject(this.nameField.getText());

        if ((boolean) Main.store.getOis().readObject()) {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_NAME_CHANGED_SUCCESSFUL);
        } else {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_NAME_CHANGED_UNSUCCESSFUL);
        }

        this.submitButton.getScene().getWindow().hide();

        // load product window
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffChosenProduct.fxml", this.nameField.getText(), 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(this.nameField.getText());
        this.nameField.getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            try {
                requireNonBlank(this.nameField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
                this.submitButton.setDisable(false);
            } catch (IllegalArgumentException ex) {
                this.submitButton.setDisable(true);
            }
        }));
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }
}
