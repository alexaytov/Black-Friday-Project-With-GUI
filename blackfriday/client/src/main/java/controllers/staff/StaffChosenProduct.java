package controllers.staff;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import controllers.product.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import openjfx.Main;
import product.Product;
import util.Operations;

import java.io.*;
import java.nio.file.Files;

import static util.Operations.confirmationPopUp;

public class StaffChosenProduct {

    private Product product;

    @FXML
    private JFXTextField discountedPrice;
    @FXML
    private ImageView image;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField descriptionField;
    @FXML
    private JFXTextField quantityField;
    @FXML
    private JFXTextField sizeField;
    @FXML
    private JFXTextField priceField;
    @FXML
    private JFXTextField minimumPriceField;
    @FXML
    private JFXTextField discountedPercentField;

    public void initProduct(String productName) throws IOException, ClassNotFoundException {
        // get currently chosen product by name from server
        Main.tcpServer.write("get product by name");
        Main.tcpServer.write(productName);
        Product product = Main.tcpServer.read();
        fillProductFieldsWithInformation(product);
        this.product = product;
    }

    public void initProduct(Product product) throws IOException {
        // get currently chosen product based on server
        Main.tcpServer.write("set product");
        Main.tcpServer.write(product);
        this.product = product;
        fillProductFieldsWithInformation(product);
    }

    private void fillProductFieldsWithInformation(Product product) {
        this.nameField.setText(product.getName());
        this.descriptionField.setText(product.getDescription());
        this.quantityField.setText(String.valueOf(product.getQuantity()));
        this.sizeField.setText(product.getSize());
        this.priceField.setText(String.valueOf(product.getPrice()));
        this.minimumPriceField.setText(String.valueOf(product.getMinimumPrice()));
        this.discountedPercentField.setText(String.format("%.2f", product.getDiscountPercent()));
        this.discountedPrice.setText(String.format("%.2f", product.getDiscountedPrice()));
        // set image to GUI
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        this.image.setImage(new Image(is));
    }

    @FXML
    void deleteChosenProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.tcpServer.write("delete product");
        Main.tcpServer.write(this.product.getName());
        // shows user if executed command was successful
        if (Main.tcpServer.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_DELETED_SUCCESSFULLY);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_DELETED_UNSUCCESSFULLY);
        }
        // goes to staff products window
        this.goStaffProducts(null);
    }

    @FXML
    void changeDiscountPercent(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/product/changeDiscountPercent.fxml", 600, 200);
        // initialize product in ChangeDiscountPercent controller
        ChangeDiscountPercent controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeDescription(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/product/changeDescription.fxml", 600, 200);
        // initialize product in ChangeDescription controller
        ChangeDescription controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeMinimumPrice(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/product/changeMinimumPrice.fxml", 600, 200);
        // initialize product in ChangeMinimumPrice controller
        ChangeMinimumPrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeName(ActionEvent event) throws IOException {
        this.nameField.getScene().getWindow().hide();
        Operations.loadWindow("/view/product/changeName.fxml", 600, 200);
    }

    @FXML
    void changePicture(ActionEvent event) throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick image");
        FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter("PNG Images", "*.png");
        FileChooser.ExtensionFilter jpgExtensionFilter = new FileChooser.ExtensionFilter("JPG Images", "*.jpg");
        fileChooser.getExtensionFilters().addAll(pngExtensionFilter, jpgExtensionFilter);
        File file = fileChooser.showOpenDialog(this.nameField.getScene().getWindow());
        if (file != null) {
            // user picked a file
            Main.tcpServer.write("change product image");
            Main.tcpServer.write(Files.readAllBytes(file.toPath()));
            // shows user if image was changed successfully
            if (Main.tcpServer.read()) {
                confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_SUCCESSFUL);
                this.image.setImage(new Image(new FileInputStream(file)));
            } else {
                confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_UNSUCCESSFUL);
            }
        }
    }

    @FXML
    void changePrice(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/product/changePrice.fxml", 600, 200);
        // initialize product in ChangePrice controller
        ChangePrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeQuantity(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/product/changeQuantity.fxml", 600, 200);
        // initialize product in ChangeQuantity controller
        ChangeQuantity controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeSize(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/product/changeSize.fxml", 600, 200);
        // initialize product in ChangeSize controller
        ChangeSize controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void goStaffProducts(ActionEvent event) throws IOException {
        this.nameField.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/staffProducts.fxml", 600, 730);
    }
}
