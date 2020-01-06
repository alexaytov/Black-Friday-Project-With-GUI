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

import static validator.Validator.requireNonBlank;

public class ChangeDescription implements Initializable {

    private Product product;
    @FXML
    private JFXTextField descriptionField;
    @FXML
    private JFXButton submitButton;
    private Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(10), event -> {
        try {
            requireNonBlank(this.descriptionField.getText(), ExceptionMessages.DESCRIPTION_NULL_OR_EMPTY);
            this.submitButton.setDisable(false);
        } catch (IllegalArgumentException ex) {
            this.submitButton.setDisable(true);
        }
    }));

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        System.out.println(" addddd");
        Main.tcpServer.write("change product description");
        Main.tcpServer.write(this.descriptionField.getText());

        if (Main.tcpServer.read()) {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DESCRIPTION_CHANGED_SUCCESSFUL);
            product.setDescription(this.descriptionField.getText());
        } else {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DESCRIPTION_CHANGED_UNSUCCESSFUL);
        }

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffChosenProduct.fxml", "Product", 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(product);
        this.checkIfAllDataIsValid.stop();
        this.descriptionField.getScene().getWindow().hide();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        checkIfAllDataIsValid.play();
    }

    public void initProduct(Product product) {
        this.product = product;
    }
}
