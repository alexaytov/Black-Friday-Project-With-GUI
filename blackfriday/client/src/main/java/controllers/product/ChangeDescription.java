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
import application.Main;
import product.Product;
import util.Operations;

import java.io.IOException;
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
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        // send command to server to change product description
        Main.tcpServer.write("change product description");
        // get confirmation from server
        Main.tcpServer.write(this.descriptionField.getText());
        // shows confirmation from server to userw
        if (Main.tcpServer.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_DESCRIPTION_CHANGED_SUCCESSFUL);
            product.setDescription(this.descriptionField.getText());
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_DESCRIPTION_CHANGED_UNSUCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow("/view/staff/staffChosenProduct.fxml", 600, 600);
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
