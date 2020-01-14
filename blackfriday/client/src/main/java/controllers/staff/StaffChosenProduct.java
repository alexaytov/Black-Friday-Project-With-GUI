package controllers.staff;

import application.App;
import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ConstantMessages;
import controllers.product.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import product.Product;
import util.Operations;
import util.Windows;

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

    public void initProduct(String productName) {
        // get currently chosen product by name from server
        App.serverConnection.write(CommandNames.GET_PRODUCT_BY_NAME);
        App.serverConnection.write(productName);
        Product product = App.serverConnection.read();
        fillProductFieldsWithInformation(product);
        this.product = product;
    }

    public void initProduct(Product product) {
        // get currently chosen product based on server
        App.serverConnection.write(CommandNames.SET_PRODUCT);
        App.serverConnection.write(product);
        this.product = product;
        fillProductFieldsWithInformation(product);
    }

    private void fillProductFieldsWithInformation(Product product) {
        this.nameField.setText(product.getName());
        this.descriptionField.setText(product.getDescription());
        this.quantityField.setText(String.valueOf(product.getQuantity()));
        this.sizeField.setText(product.getSize());
        this.priceField.setText(String.valueOf(product.getPrice()));
        this.minimumPriceField.setText(String.format("%.2f", product.getMinimumPrice()));
        this.discountedPercentField.setText(String.format("%.2f", product.getDiscountPercent()));
        this.discountedPrice.setText(String.format("%.2f", product.getDiscountedPrice()));
        // set image to GUI
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        this.image.setImage(new Image(is));
    }

    @FXML
    void deleteChosenProduct(ActionEvent event) {
        App.serverConnection.write(CommandNames.DELETE_PRODUCT);
        App.serverConnection.write(this.product.getName());
        // shows user if executed command was successful
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_DELETED_SUCCESSFULLY);
        } else {
            confirmationPopUp(ConstantMessages.PRODUCT_DELETED_UNSUCCESSFULLY);
        }
        // goes to staff products window
        this.goStaffProducts(null);
    }

    @FXML
    void changeDiscountPercent(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PRODUCT_CHANGE_DISCOUNT_PERCENT_PATH, Windows.PRODUCT_CHANGE_DISCOUNT_PERCENT_WIDTH, Windows.PRODUCT_CHANGE_DISCOUNT_PERCENT_HEIGHT);
        // initialize product in ChangeDiscountPercent controller
        ChangeDiscountPercent controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeDescription(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PRODUCT_CHANGE_DESCRIPTION_PATH, Windows.PRODUCT_CHANGE_DESCRIPTION_WIDTH, Windows.PRODUCT_CHANGE_DESCRIPTION_HEIGHT);
        // initialize product in ChangeDescription controller
        ChangeDescription controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeMinimumPrice(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PRODUCT_CHANGE_MINIMUM_PRICE_PATH, Windows.PRODUCT_CHANGE_MINIMUM_PRICE_WIDTH, Windows.PRODUCT_CHANGE_MINIMUM_PRICE_HEIGHT);
        // initialize product in ChangeMinimumPrice controller
        ChangeMinimumPrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeName(ActionEvent event) {
        this.nameField.getScene().getWindow().hide();
        Operations.loadWindow(Windows.PRODUCT_CHANGE_NAME_PATH, Windows.PRODUCT_CHANGE_NAME_WIDTH, Windows.PRODUCT_CHANGE_NAME_HEIGHT);
    }

    @FXML
    void changePicture(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick image");
        FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter("PNG Images", "*.png");
        FileChooser.ExtensionFilter jpgExtensionFilter = new FileChooser.ExtensionFilter("JPG Images", "*.jpg");
        fileChooser.getExtensionFilters().addAll(pngExtensionFilter, jpgExtensionFilter);
        File file = fileChooser.showOpenDialog(this.nameField.getScene().getWindow());
        if (file != null) {
            // user picked a file
            App.serverConnection.write(CommandNames.CHANGE_PRODUCT_IMAGE);
            App.serverConnection.write(Files.readAllBytes(file.toPath()));
            // shows user if image was changed successfully
            if (App.serverConnection.read()) {
                confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_SUCCESSFUL);
                this.image.setImage(new Image(new FileInputStream(file)));
            } else {
                confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_UNSUCCESSFUL);
            }
        }
    }

    @FXML
    void changePrice(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PRODUCT_CHANGE_PRICE_PATH, Windows.PRODUCT_CHANGE_PRICE_WIDTH, Windows.PRODUCT_CHANGE_PRICE_HEIGHT);
        // initialize product in ChangePrice controller
        ChangePrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeQuantity(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PRODUCT_CHANGE_QUANTITY_PATH, Windows.PRODUCT_CHANGE_QUANTITY_WIDTH, Windows.PRODUCT_CHANGE_QUANTITY_HEIGHT);
        // initialize product in ChangeQuantity controller
        ChangeQuantity controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeSize(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PRODUCT_CHANGE_SIZE_PATH, Windows.PRODUCT_CHANGE_SIZE_WIDTH, Windows.PRODUCT_CHANGE_SIZE_HEIGHT);
        // initialize product in ChangeSize controller
        ChangeSize controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void goStaffProducts(ActionEvent event) {
        this.nameField.getScene().getWindow().hide();
        Operations.loadWindow(Windows.STAFF_PRODUCTS_PATH, Windows.STAFF_PRODUCTS_WIDTH, Windows.STAFF_PRODUCTS_HEIGHT);
    }
}
