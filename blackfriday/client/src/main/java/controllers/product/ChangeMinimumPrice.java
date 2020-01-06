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
import product.Product;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static validator.Validator.requireNonNegative;
import static validator.Validator.validatePrice;

public class ChangeMinimumPrice implements Initializable {


    private Product product;

    private double minimumPrice;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
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
    private JFXTextField minimumPriceField;

    @FXML
    private JFXButton submitButton;

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            validatePrice(product.getPrice(), this.minimumPrice);
            Main.store.getOos().writeObject("change product minimum price");
            Main.store.getOos().writeObject(this.minimumPrice);

            if ((boolean) Main.store.getOis().readObject()) {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_MINIMUM_PRICE_CHANGED_SUCCESSFUL);
                this.product.setMinimumPrice(this.minimumPrice);
            } else {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_MINIMUM_PRICE_CHANGED_UNSUCCESSFUL);
            }
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.PRICE_BELOW_MINIMUM_PRICE);
        }

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffChosenProduct.fxml", "Product", 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        this.checkIfAllDataIsValid.stop();
        this.minimumPriceField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }
}
