package controllers.product;

import application.App;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import controllers.staff.StaffChosenProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import product.Product;
import util.Operations;

import java.io.IOException;

import static util.Operations.confirmationPopUp;

public class ChangeSize {

    private Product product;
    @FXML
    private JFXTextField sizeField;

    public void initProduct(Product product) {
        this.product = product;
    }

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        // send change product size command to server
        App.tcpServer.write("change product size");
        App.tcpServer.write(this.sizeField.getText());
        // shows if executed command was successful
        if (App.tcpServer.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_SIZE_CHANGED_SUCCESSFUL);
            this.product.setSize(this.sizeField.getText());
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_SIZE_CHANGED_SUCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow("/view/staff/staffChosenProduct.fxml", 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(this.product);
        this.sizeField.getScene().getWindow().hide();
    }
}
