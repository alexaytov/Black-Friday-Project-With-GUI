package controllers.product;

import application.App;
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
import product.Product;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;
import static validator.Validator.requireNonNegative;


public class ChangeQuantity implements Initializable {

    private int quantity;

    private Product product;
    @FXML
    private JFXTextField quantityField;
    @FXML
    private JFXButton submitButton;
    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        try {
            this.quantity = Integer.parseInt(this.quantityField.getText());
            requireNonNegative(quantity, ExceptionMessages.QUANTITY_NEGATIVE);
            this.submitButton.setDisable(false);
        } catch (IllegalArgumentException ex) {
            this.submitButton.setDisable(true);
        }
    }));

    public void initProduct(Product product) {
        this.product = product;
    }

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        App.serverConnection.write("change product quantity");
        App.serverConnection.write(this.quantity);

        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_QUANTITY_CHANGED_SUCCESSFUL);
            this.product.setQuantity(this.quantity);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_QUANTITY_CHANGED_UNSUCCESSFUL);
        }

        FXMLLoader loader = Operations.loadWindow("/view/staff/staffChosenProduct.fxml", 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        this.checkIfAllDataIsValid.stop();
        this.quantityField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }
}
