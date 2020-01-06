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
import validator.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static validator.Validator.validatePrice;

public class ChangePrice implements Initializable {

    private Product product;
    private double price;
    @FXML
    private JFXTextField priceField;
    @FXML
    private JFXButton submitButton;
    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
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
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            validatePrice(this.price, product.getMinimumPrice());
            Main.tcpServer.write("change product price");
            Main.tcpServer.write(this.price);

            if (Main.tcpServer.read()) {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_PRICE_CHANGED_SUCCESSFUL);
                this.product.setPrice(this.price);
            } else {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_PRICE_CHANGED_UNCCESSFUL);
            }
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ex.getMessage());
        }

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffChosenProduct.fxml", "Product", 600, 600);
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
