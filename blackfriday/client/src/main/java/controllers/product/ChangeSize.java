package controllers.product;

import application.App;
import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ConstantMessages;
import controllers.staff.StaffChosenProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import product.Product;
import util.Operations;
import util.Windows;

import static util.Operations.confirmationPopUp;

public class ChangeSize {

    private Product product;
    @FXML
    private JFXTextField sizeField;

    public void initProduct(Product product) {
        this.product = product;
    }

    @FXML
    void submit(ActionEvent event) {
        // send change product size command to server
        App.serverConnection.write(CommandNames.CHANGE_PRODUCT_SIZE);
        App.serverConnection.write(this.sizeField.getText());
        // shows if executed command was successful
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_SIZE_CHANGED_SUCCESSFUL);
            this.product.setSize(this.sizeField.getText());
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_SIZE_CHANGED_SUCCESSFUL);
        }
        // load staff chosen product window
        FXMLLoader loader = Operations.loadWindow(Windows.STAFF_CHOSEN_PRODUCT_PATH, Windows.STAFF_CHOSEN_PRODUCT_WIDTH, Windows.STAFF_CHOSEN_PRODUCT_HEIGHT);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(this.product);
        this.sizeField.getScene().getWindow().hide();
    }
}
