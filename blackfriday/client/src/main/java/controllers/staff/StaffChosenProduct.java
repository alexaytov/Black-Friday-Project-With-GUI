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
    @FXML
    private JFXTextField pictureField;

    public void initProduct(String productName) throws IOException, ClassNotFoundException {
        Main.tcpServer.write("get product by name");
        Main.tcpServer.write(productName);
        Product product = Main.tcpServer.read();
        fillProductFieldsWithInformation(product);
        this.product = product;
    }

    public void initProduct(Product product) throws IOException {
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
        this.discountedPercentField.setText(String.format("%.2f",product.getDiscountPercent()));
        this.discountedPrice.setText(String.format("%.2f", product.getDiscountedPrice()));

        InputStream is = new ByteArrayInputStream(product.getImageContent());
        this.image.setImage(new Image(is));
    }

    @FXML
    void deleteChosenProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.tcpServer.write("delete product");
        Main.tcpServer.write(this.product.getName());
        if (Main.tcpServer.read()) {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DELETED_SUCCESSFULLY);
        } else {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DELETED_UNSUCCESSFULLY);
        }

        this.goStaffProducts(null);
    }

    @FXML
    void changeDiscountPercent(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/product/changeDiscountPercent.fxml", "Change product discount percent", 600, 200);
        ChangeDiscountPercent controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeDescription(ActionEvent event) throws IOException {

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/product/changeDescription.fxml", "Change product description", 600, 200);
        ChangeDescription controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeMinimumPrice(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/product/changeMinimumPrice.fxml", "Change product minimum price", 600, 200);
        ChangeMinimumPrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeName(ActionEvent event) throws IOException {
        Operations.changeWindows(this.nameField, "Change product name", "/view/product/changeName.fxml", this.getClass(), 600, 200);

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
            if (Main.tcpServer.read()) {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_SUCCESSFUL);
                this.image.setImage(new Image(new FileInputStream(file)));
            } else {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_UNSUCCESSFUL);
            }
        }

    }

    @FXML
    void changePrice(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/product/changePrice.fxml", "Change product price", 600, 200);
        ChangePrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeQuantity(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/product/changeQuantity.fxml", "Change product quantity", 600, 200);
        ChangeQuantity controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeSize(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/product/changeSize.fxml", "Change product size", 600, 200);
        ChangeSize controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void goStaffProducts(ActionEvent event) throws IOException {
        Operations.changeWindows(this.nameField, "controllers/product", "/view/staff/staffProducts.fxml", this.getClass(), 600, 730);
    }


}
