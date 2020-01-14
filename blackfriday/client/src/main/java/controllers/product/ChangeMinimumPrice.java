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

import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;
import static validator.Validator.requireNonNegative;
import static validator.Validator.validatePrice;

public class ChangeMinimumPrice implements Initializable {

    private Product product;

    private double minimumPrice;

    @FXML
    private JFXTextField minimumPriceField;

    @FXML
    private JFXButton submitButton;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        // enables/disable submit button based on entered data in the minimumPriceField
        try {
            this.minimumPrice = Integer.parseInt(this.minimumPriceField.getText());
            requireNonNegative(minimumPrice, ExceptionMessages.MINIMUM_PRICE_MUST_BE_POSITIVE);
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
        validatePrice(product.getPrice(), this.minimumPrice);
        // send change product minimum price command to server
        App.serverConnection.write("change product minimum price");
        App.serverConnection.write(this.minimumPrice);
        // shows if executed server command was successful
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_MINIMUM_PRICE_CHANGED_SUCCESSFUL);
            this.product.setMinimumPrice(this.minimumPrice);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_MINIMUM_PRICE_CHANGED_UNSUCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow(Windows.STAFF_CHOSEN_PRODUCT_PATH, Windows.STAFF_CHOSEN_PRODUCT_WIDTH, Windows.STAFF_CHOSEN_PRODUCT_HEIGHT);
        // initializes product StaffChosenProduct controller
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        // stops minimumPrice field data validation timeline
        this.checkIfAllDataIsValid.stop();
        this.minimumPriceField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // start minimumPrice data validation timeline
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }
}
