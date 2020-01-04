package controllers.product;

import application.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import common.ConstantMessages;
import common.Operations;
import controllers.staff.StaffChosenProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import product.Product;

import java.io.IOException;

public class ChangeSize {

    private Product product;

    public void initProduct(Product product){
        this.product = product;
    }

    @FXML
    private JFXTextField sizeField;

    @FXML
    private JFXButton submitButton;

    @FXML
    void submit(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("change product size");
        Main.store.getOos().writeObject(this.sizeField.getText());

        if((boolean) Main.store.getOis().readObject()){
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_SIZE_CHANGED_SUCCESSFUL);
            this.product.setSize(this.sizeField.getText());
        }else{
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_SIZE_CHANGED_SUCCESSFUL);
        }

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/staff/staffChosenProduct.fxml", "Product", 600, 600);
        StaffChosenProduct controller = loader.getController();
        controller.initProduct(this.product);
        this.sizeField.getScene().getWindow().hide();
    }
}
