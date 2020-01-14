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
import util.Windows;
import validator.Validator;

import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;
import static validator.Validator.validatePrice;

public class ChangePrice implements Initializable {

    private Product product;

    private double price;

    @FXML
    private JFXTextField priceField;

    @FXML
    private JFXButton submitButton;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        // enables/disable submit button based on entered price in priceField
        try {
            this.price = Double.parseDouble(this.priceField.getText());
            Validator.requireNonNegative(this.price, ExceptionMessages.MINIMUM_PRICE_MUST_BE_POSITIVE);
            this.submitButton.setDisable(false);
        } catch (IllegalArgumentException ex) {
            this.submitButton.setDisable(true);
        }
    }));

    public void initProduct(Product product) {
        this.product = product;
    }

    @FXML
    void submit(ActionEvent event) {
        // send change product price command to server
        validatePrice(this.price, product.getMinimumPrice());
        App.serverConnection.write("change product price");
        App.serverConnection.write(this.price);
        // show if executed command was successful to user
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_PRICE_CHANGED_SUCCESSFUL);
            this.product.setPrice(this.price);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_PRICE_CHANGED_UNCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow(Windows.STAFF_CHOSEN_PRODUCT_PATH, Windows.STAFF_CHOSEN_PRODUCT_WIDTH, Windows.STAFF_CHOSEN_PRODUCT_HEIGHT);
        // initialize product StaffChosenProduct controller
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        this.checkIfAllDataIsValid.stop();
        this.priceField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }
}
