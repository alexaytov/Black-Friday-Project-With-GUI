package controllers.product;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
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

import static util.Operations.confirmationPopUp;
import static validator.Validator.validateDiscountPercent;


public class ChangeDiscountPercent implements Initializable {

    private Product product;

    private double discountPercent;

    @FXML
    private JFXTextField discountPercentField;

    @FXML
    private JFXButton submitButton;

    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        // enables/disable submit button based on entered data in the discountPercentField
        try {
            this.discountPercent = Double.parseDouble(this.discountPercentField.getText());
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
        // validate new discount percent before sending to server
        validateDiscountPercent(this.discountPercent, product.getPrice(), product.getMinimumPrice());
        // send change discount percent command to server
        Main.tcpServer.write("change product discount percent");
        Main.tcpServer.write(this.discountPercent);
        // shows to user if the change was successful
        if (Main.tcpServer.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_DISCOUNT_PERCENT_CHANGED_SUCCESSFUL);
            this.product.setDiscountPercent(this.discountPercent);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_DISCOUNT_PERCENT_CHANGED_UNSUCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow("/view/staff/staffChosenProduct.fxml", 600, 600);
        // initialize product to StaffChosenProduct controller
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        // stop data validation timeline
        this.checkIfAllDataIsValid.stop();
        this.discountPercentField.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // start data validation timeline
        this.checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        this.checkIfAllDataIsValid.play();
    }
}
