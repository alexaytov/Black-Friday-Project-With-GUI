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
import product.Product;
import util.Operations;
import util.Windows;

import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;
import static validator.Validator.requireNonBlank;

public class ChangeDescription implements Initializable {

    private Product product;

    @FXML
    private JFXTextField descriptionField;

    @FXML
    private JFXButton submitButton;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        // enables/disable submit button based on entered data in descriptionField
        try {
            requireNonBlank(this.descriptionField.getText(), ExceptionMessages.DESCRIPTION_NULL_OR_EMPTY);
            this.submitButton.setDisable(false);
        } catch (IllegalArgumentException ex) {
            this.submitButton.setDisable(true);
        }
    }));

    @FXML
    void submit(ActionEvent event) {
        // send command to server to change product description
        App.serverConnection.write(CommandNames.CHANGE_PRODUCT_DESCIRPTION);
        // get confirmation from server
        App.serverConnection.write(this.descriptionField.getText());
        // shows confirmation from server to user
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_DESCRIPTION_CHANGED_SUCCESSFUL);
            product.setDescription(this.descriptionField.getText());
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_DESCRIPTION_CHANGED_UNSUCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow(Windows.STAFF_CHOSEN_PRODUCT_PATH, Windows.STAFF_CHOSEN_PRODUCT_WIDTH, Windows.STAFF_CHOSEN_PRODUCT_HEIGHT);
        // initialize product for staff chosen product controller
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        // stop data validation timeline
        this.checkIfAllDataIsValid.stop();
        this.descriptionField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // start data validation timeline
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }

    public void initProduct(Product product) {
        this.product = product;
    }
}
