package controllers.staff;

import application.Main;
import com.jfoenix.controls.JFXTextField;
import common.ConstantMessages;
import common.Operations;
import controllers.product.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import product.Product;

import java.io.*;
import java.nio.file.Files;

public class StaffChosenProduct {

    private Product product;

    @FXML
    private JFXTextField discountedPrice;


    public void initProduct(String productName) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("get product by name");
        Main.store.getOos().writeObject(productName);
        Product product = (Product) Main.store.getOis().readObject();
        this.nameField.setText(product.getName());
        this.descriptionField.setText(product.getDescription());
        this.quantityField.setText(String.valueOf(product.getQuantity()));
        this.sizeField.setText(product.getSize());
        this.priceField.setText(String.valueOf(product.getPrice()));
        this.minimumPriceField.setText(String.valueOf(product.getMinimumPrice()));
        this.discountedPriceField.setText(String.valueOf(product.getDiscountPercent()));
        byte[] imageContent = product.getImageContent();
        for (byte b : imageContent) {
            System.out.println(b);
        }
        this.product = product;

    }

    public void initProduct(Product product) throws IOException {
        Main.store.getOos().writeObject("set product");
        Main.store.getOos().writeObject(product);
        this.product = product;
        this.discountedPrice.setText(String.valueOf(product.getDiscountedPrice()));
        this.nameField.setText(product.getName());
        this.descriptionField.setText(product.getDescription());
        this.quantityField.setText(String.valueOf(product.getQuantity()));
        this.sizeField.setText(product.getSize());
        this.priceField.setText(String.valueOf(product.getPrice()));
        this.minimumPriceField.setText(String.valueOf(product.getMinimumPrice()));
        this.discountedPriceField.setText(String.valueOf(product.getDiscountPercent()));
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        this.image.setImage(new Image(is));

    }


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
    private JFXTextField discountedPriceField;

    @FXML
    private JFXTextField pictureField;


    @FXML
    void deleteChosenProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("delete product");
        Main.store.getOos().writeObject(this.product.getName());
        if ((boolean) Main.store.getOis().readObject()) {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DELETED_SUCCESSFULLY);
        } else {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_DELETED_UNSUCCESSFULLY);
        }

        this.goStaffProducts(null);
    }

    @FXML
    void changeDiscountPercent(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/product/changeDiscountPercent.fxml", "Change product discount percent", 600, 200);
        ChangeDiscountPercent controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeDescription(ActionEvent event) throws IOException {

        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/product/changeDescription.fxml", "Change product description", 600, 200);
        ChangeDescription controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeMinimumPrice(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/product/changeMinimumPrice.fxml", "Change product minimum price", 600, 200);
        ChangeMinimumPrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeName(ActionEvent event) throws IOException {
        Operations.changeWindows(this.nameField, "Change product name", "/FXML/product/changeName.fxml", this.getClass(), 600, 200);

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
            Main.store.getOos().writeObject("change product image");
            Main.store.getOos().writeObject(Files.readAllBytes(file.toPath()));
            if ((boolean) Main.store.getOis().readObject()) {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_SUCCESSFUL);
                this.image.setImage(new Image(new FileInputStream(file)));
            } else {
                ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_IMAGE_CHANGE_UNSUCCESSFUL);
            }
        }

    }

    @FXML
    void changePrice(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/product/changePrice.fxml", "Change product price", 600, 200);
        ChangePrice controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeQuantity(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/product/changeQuantity.fxml", "Change product quantity", 600, 200);
        ChangeQuantity controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void changeSize(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/product/changeSize.fxml", "Change product size", 600, 200);
        ChangeSize controller = loader.getController();
        controller.initProduct(product);
        this.nameField.getScene().getWindow().hide();
    }

    @FXML
    void goStaffProducts(ActionEvent event) throws IOException {
        Operations.changeWindows(this.nameField, "product", "/FXML/staff/staffProducts.fxml", this.getClass(), 600, 730);
    }


}
