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

import static validator.Validator.validateDiscountPercent;


public class ChangeDiscountPercent implements Initializable {

    private Product product;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        try {
            this.discountPercent = Double.parseDouble(this.discountPercentField.getText());
            this.submitButton.setDisable(false);
        } catch (IllegalArgumentException ex) {
            this.submitButton.setDisable(true);
        }
    }));

    private double discountPercent;

    public void initProduct(Product product) {
        this.product = product;
    }

    @FXML
    private JFXTextField discountPercentField;

    @FXML
    private JFXButton submitButton;

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            validateDiscountPercent(this.discountPercent, product.getPrice(), product.getMinimumPrice());
            Main.tcpServer.write("change product discount percent");
            Main.tcpServer.write(this.discountPercent);

            if (Main.tcpServer.read()) {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DISCOUNT_PERCENT_CHANGED_SUCCESSFUL);
                this.product.setDiscountPercent(this.discountPercent);

            } else {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DISCOUNT_PERCENT_CHANGED_UNSUCCESSFUL);
            }
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ex.getMessage());
        }

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffChosenProduct.fxml", "Product", 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        this.checkIfAllDataIsValid.stop();
        this.discountPercentField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        this.checkIfAllDataIsValid.play();

    }
}
