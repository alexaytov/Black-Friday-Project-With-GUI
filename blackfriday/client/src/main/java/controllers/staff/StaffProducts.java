package controllers.staff;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import openjfx.Main;
import product.Product;
import util.Operations;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static validator.Validator.*;

public class StaffProducts implements Initializable {

    private File createProductPictureFile;

    private int maximumQuantity;

    private List<Product> products;

    @FXML
    private VBox vBoxWithProducts;

    @FXML
    private JFXRadioButton allProductsButton;

    @FXML
    private ToggleGroup productGroup;

    @FXML
    private JFXRadioButton blackFridayButton;

    @FXML
    private JFXTextField productSearch;

    @FXML
    private JFXRadioButton productsSortByNameButton;

    @FXML
    private ToggleGroup sortGroup;

    @FXML
    private JFXRadioButton productsSortByPriceAscendingButton;

    @FXML
    private JFXRadioButton productsSortByPriceDescendingButton;

    @FXML
    private Tab quantityControlTab;

    @FXML
    private JFXTextField quantityControlField;

    @FXML
    private JFXButton quantityControlSearchButton;

    @FXML
    private JFXRadioButton quantityCOntrolSortByNameButton;

    @FXML
    private ToggleGroup sortGroup1;

    @FXML
    private JFXRadioButton quantityControlSortByPriceAscendingButton;

    @FXML
    private JFXRadioButton quantityControlSortByPriceDescnedingButton;

    @FXML
    private VBox vBoxWithProductsQualityControl;

    @FXML
    private Tab createProductTab;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField descriptionTextField;

    @FXML
    private JFXTextField priceTextField;

    @FXML
    private JFXTextField quantityTextField;

    @FXML
    private JFXTextField minimumPriceTextField;

    @FXML
    private JFXTextField sizeTextField;

    @FXML
    private JFXTextField discountPercentField;

    @FXML
    private JFXButton createProductButton;

    @FXML
    private ImageView createProductImage;

    @FXML
    private Tab productsTab;

    @FXML
    void selectPictureForNewProduct() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick image");
        FileChooser.ExtensionFilter pngExtensionFilter = new FileChooser.ExtensionFilter("PNG Images", "*.png");
        FileChooser.ExtensionFilter jpgExtensionFilter = new FileChooser.ExtensionFilter("JPG Images", "*.jpg");
        fileChooser.getExtensionFilters().addAll(pngExtensionFilter, jpgExtensionFilter);
        File file = fileChooser.showOpenDialog(this.createProductButton.getScene().getWindow());
        if (file != null) {
            // user picked a file
            createProductImage.setImage(new Image(new FileInputStream(file)));
            createProductPictureFile = file;
        }
    }

    @FXML
    void productsSelected() throws IOException, ClassNotFoundException {
        this.allProductsButton.selectedProperty().setValue(true);
        Main.store.getOos().writeObject("get staff products");
        List<Product> nameImageContentOfProductList = (List<Product>) Main.store.getOis().readObject();
        fillVBoxWithProducts(nameImageContentOfProductList, this.vBoxWithProducts);
    }

    @FXML
    void searchQuantityControl() throws IOException, ClassNotFoundException {

        this.vBoxWithProductsQualityControl.getChildren().clear();
        Main.store.getOos().writeObject("search quantity control");
        Main.store.getOos().writeObject(this.maximumQuantity);
        List<Product> products = (List<Product>) Main.store.getOis().readObject();
        this.products = products;
        if (products.size() == 0) {
            noResultsMessage();
        } else {
            fillVBoxWithProducts(products, this.vBoxWithProductsQualityControl);
        }
    }

    private void fillVBoxWithProducts(List<Product> products, VBox vBoxWithProducts) {
        vBoxWithProducts.getChildren().clear();
        this.products = products;
        if (productsTab.isSelected()) {
            sortProducts(products, productsSortByNameButton, productsSortByPriceAscendingButton, productsSortByPriceDescendingButton);
        } else {
            sortProducts(products, quantityCOntrolSortByNameButton, quantityControlSortByPriceAscendingButton, quantityControlSortByPriceDescnedingButton);
        }
        int index = 0;
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        for (Product product : products) {

            hbox.getChildren().add(createProductVBox(product));
            index++;

            if ((index % 2 == 0 && index != 0) || products.size() == 1) {
                vBoxWithProducts.getChildren().add(hbox);
                hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
            }
        }
        if (products.size() % 2 != 0) {
            hbox.setAlignment(Pos.CENTER_LEFT);
            vBoxWithProducts.getChildren().add(hbox);
        }
    }

    private VBox createProductVBox(Product product) {
        VBox vbox = new VBox();
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        ImageView imageView = new ImageView(new Image(is));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            this.vBoxWithProducts.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/staff/staffChosenProduct.fxml"));
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, 600, 600);
            stage.setTitle("Product");
            StaffChosenProduct controller = loader.getController();
            try {
                controller.initProduct(product);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        double productPrice = product.getPrice();
        productPrice = ((int) (productPrice * 100)) / 100.0;

        Label name = new Label(product.getName() + " " + productPrice + " лв.");

        hbox.getChildren().add(name);
        name.setFont(new Font(14));

        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(imageView, hbox);

        vbox.setPrefWidth(290);
        vbox.spacingProperty().setValue(10);
        vbox.alignmentProperty().set(Pos.CENTER);
        // set product name and price background color to green is they are discounted
        if (product.isDiscounted()) {
            name.setStyle("-fx-background-color:#23cba7;" +
                    "-fx-border-width: 2;");
        }
        return vbox;
    }

    private void sortProducts(List<Product> products, JFXRadioButton sortByNameButton, JFXRadioButton sortByPriceAscendingButton, JFXRadioButton sortByPriceDescendingButton) {
        if (sortByNameButton.isSelected()) {
            products.sort(Comparator.comparing(a -> a.getName().toLowerCase()));
        } else if (sortByPriceAscendingButton.isSelected()) {
            products.sort(Comparator.comparing(Product::getPrice));
        } else if (sortByPriceDescendingButton.isSelected()) {
            products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }
    }

    @FXML
    void loadAllProducts(ActionEvent event) throws IOException, ClassNotFoundException {
        this.vBoxWithProducts.getChildren().clear();
        Main.store.getOos().writeObject("get staff products");
        List<Product> nameImageContentOfProductList = (List<Product>) Main.store.getOis().readObject();
        fillVBoxWithProducts(nameImageContentOfProductList, this.vBoxWithProducts);
        if (nameImageContentOfProductList.size() == 0) {
            noResultsMessage();
        }

    }

    @FXML
    void loadDiscountedProducts(ActionEvent event) throws IOException, ClassNotFoundException {
        this.vBoxWithProducts.getChildren().clear();
        Main.store.getOos().writeObject("get staff discounted products");
        List<Product> nameImageContentOfProductList = (List<Product>) Main.store.getOis().readObject();
        fillVBoxWithProducts(nameImageContentOfProductList, this.vBoxWithProducts);
        if (nameImageContentOfProductList.size() == 0) {
            noResultsMessage();
        }
    }

    @FXML
    void searchProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        this.vBoxWithProducts.getChildren().clear();
        if (allProductsButton.isSelected()) {
            Main.store.getOos().writeObject("search staff all products");
            Main.store.getOos().writeObject(this.productSearch.getText());
            List<Product> products = (List<Product>) Main.store.getOis().readObject();
            fillVBoxWithProducts(products, this.vBoxWithProducts);
        } else {
            Main.store.getOos().writeObject("search staff discounted products");
            Main.store.getOos().writeObject(this.productSearch.getText());
            List<Product> products = (List<Product>) Main.store.getOis().readObject();
            fillVBoxWithProducts(products, this.vBoxWithProducts);
        }
        if (vBoxWithProducts.getChildren().size() == 0) {
            // no results found
            noResultsMessage();
        }
    }

    private void noResultsMessage() {
        Label noResultsLabel = new Label(ConstantMessages.NO_RESULTS);
        HBox hBox = new HBox(noResultsLabel);
        noResultsLabel.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        this.vBoxWithProducts.setAlignment(Pos.CENTER);

        noResultsLabel.setStyle("-fx-alignment: center; -fx-font-size: 14pt; -fx-fill-width: true;");
        this.vBoxWithProducts.getChildren().add(hBox);
    }


    @FXML
    void goToMainMenu(ActionEvent event) throws IOException {
        Operations.changeWindows(nameTextField, "Settings", "/view/staff/staffLoggedIn.fxml", this.getClass(), 600, 600);

    }

    @FXML
    void productSortButtonSelected() {
        sortProducts(this.products, this.productsSortByNameButton, this.productsSortByPriceAscendingButton, this.productsSortByPriceDescendingButton);
        fillVBoxWithProducts(this.products, this.vBoxWithProducts);
    }

    @FXML
    void quantityControlSortButtonSelected() {
        sortProducts(this.products, this.quantityCOntrolSortByNameButton, this.quantityControlSortByPriceAscendingButton, this.quantityControlSortByPriceDescnedingButton);
        fillVBoxWithProducts(this.products, this.vBoxWithProductsQualityControl);
    }

    @FXML
    void createProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        GetProductInformationFromTextFields getProductInformationFromTextFields = new GetProductInformationFromTextFields().invoke();
        String name = getProductInformationFromTextFields.getName();
        String description = getProductInformationFromTextFields.getDescription();
        String size = getProductInformationFromTextFields.getSize();
        int quantity = getProductInformationFromTextFields.getQuantity();
        double price = getProductInformationFromTextFields.getPrice();
        double minimumPrice = getProductInformationFromTextFields.getMinimumPrice();
        double discountPercent = getProductInformationFromTextFields.getDiscountPercent();
        Product product = new Product(name, description, quantity, price, minimumPrice, discountPercent, Files.readAllBytes(this.createProductPictureFile.toPath()), size);

        Main.store.getOos().writeObject("create product");
        Main.store.getOos().writeObject(product);

        if ((boolean) Main.store.getOis().readObject()) {
            ConstantMessages.confirmationPopUp(ConstantMessages.PRODUCT_CREATED);
            resetAllCreateProductField();
        } else {
            ExceptionMessages.showWarningDialog(ExceptionMessages.Product_ALREADY_EXISTS);
        }
    }

    private void resetAllCreateProductField() {
        this.nameTextField.setText("");
        this.descriptionTextField.setText("");
        this.sizeTextField.setText("");
        this.quantityTextField.setText("");
        this.priceTextField.setText("");
        this.minimumPriceTextField.setText("");
        this.discountPercentField.setText("");
        this.createProductPictureFile = null;
        this.createProductImage.setImage(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Timeline verifyQuantityControlField = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            try {
                int quantity = Integer.parseInt(this.quantityControlField.getText());
                validateQuantity(quantity);
                this.quantityControlSearchButton.setDisable(false);
                this.maximumQuantity = quantity;
            } catch (IllegalArgumentException ex) {
                this.quantityControlSearchButton.setDisable(true);
            }
        }));
        verifyQuantityControlField.setCycleCount(Timeline.INDEFINITE);

        this.quantityControlTab.selectedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                verifyQuantityControlField.stop();
            } else {
                verifyQuantityControlField.play();

            }
        });

        Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (areAllFieldsValid()) {
                this.createProductButton.setDisable(false);
            } else {
                this.createProductButton.setDisable(true);
            }
        }));
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        this.createProductTab.selectedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                checkIfAllDataIsValid.stop();
            } else {
                checkIfAllDataIsValid.play();
            }
        });


        Color textFieldDefaultUnfocusedColor = Color.valueOf(ConstantMessages.TEXT_FIELD_UNFOCUSED_COLOR);

        nameTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    requireNonBlank(nameTextField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
                    nameTextField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    nameTextField.setUnFocusColor(Color.RED);
                }
            }
        });

        descriptionTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                textFieldColorChangeValidation(textFieldDefaultUnfocusedColor, descriptionTextField);
            }
        });

        sizeTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                textFieldColorChangeValidation(textFieldDefaultUnfocusedColor, sizeTextField);
            }
        });

        priceTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                priceMinimumPriceDiscountPercentValidation(textFieldDefaultUnfocusedColor);
            }
        });

        quantityTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    validateQuantity(quantity);
                    quantityTextField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    createProductButton.setDisable(true);
                    quantityTextField.setUnFocusColor(Color.RED);
                }
            }
        });

        minimumPriceTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                priceMinimumPriceDiscountPercentValidation(textFieldDefaultUnfocusedColor);
            }
        });

        discountPercentField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                priceMinimumPriceDiscountPercentValidation(textFieldDefaultUnfocusedColor);
            }
        });


    }

    private void priceMinimumPriceDiscountPercentValidation(Color textFieldDefaultUnfocusedColor) {
        try {
            double discountPercent = Double.parseDouble(discountPercentField.getText());
            double price = Double.parseDouble(priceTextField.getText());
            double minimumPrice = Double.parseDouble(minimumPriceTextField.getText());
            validateDiscountPercent(discountPercent, price, minimumPrice);
            discountPercentField.setUnFocusColor(textFieldDefaultUnfocusedColor);
            priceTextField.setUnFocusColor(textFieldDefaultUnfocusedColor);
            minimumPriceTextField.setUnFocusColor(textFieldDefaultUnfocusedColor);
        } catch (IllegalArgumentException ex) {
            createProductButton.setDisable(true);
            discountPercentField.setUnFocusColor(Color.RED);
            priceTextField.setUnFocusColor(Color.RED);
            minimumPriceTextField.setUnFocusColor(Color.RED);
        }
    }

    private void textFieldColorChangeValidation(Color textFieldDefaultUnfocusedColor, JFXTextField textField) {
        try {
            requireNonBlank(textField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
            textField.setUnFocusColor(textFieldDefaultUnfocusedColor);
        } catch (IllegalArgumentException ex) {
            createProductButton.setDisable(true);
            textField.setUnFocusColor(Color.RED);
        }
    }

    private boolean areAllFieldsValid() {
        try {
            if (this.createProductPictureFile == null) {
                throw new IllegalArgumentException();
            }
            String name = this.nameTextField.getText();
            String description = this.descriptionTextField.getText();
            String size = this.sizeTextField.getText();
            double price = Double.parseDouble(this.priceTextField.getText());
            double minimumPrice = Double.parseDouble(this.minimumPriceTextField.getText());
            double discountPercent = Double.parseDouble(this.discountPercentField.getText());
            int quantity = Integer.parseInt(this.quantityTextField.getText());

            requireNonBlank(name, ExceptionMessages.NAME_NULL_OR_EMPTY);
            requireNonBlank(size, ExceptionMessages.SIZE_NULL_OR_EMPTY);
            requireNonBlank(description, ExceptionMessages.DESCRIPTION_NULL_OR_EMPTY);
            validateQuantity(quantity);
            validateDiscountPercent(discountPercent, price, minimumPrice);

            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }


    private class GetProductInformationFromTextFields {
        private String name;
        private String description;
        private String size;
        private int quantity;
        private double price;
        private double minimumPrice;
        private double discountPercent;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getSize() {
            return size;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        double getMinimumPrice() {
            return minimumPrice;
        }

        double getDiscountPercent() {
            return discountPercent;
        }

        GetProductInformationFromTextFields invoke() {
            name = StaffProducts.this.nameTextField.getText();
            description = StaffProducts.this.descriptionTextField.getText();
            size = StaffProducts.this.sizeTextField.getText();
            quantity = Integer.parseInt(StaffProducts.this.quantityTextField.getText());
            price = Double.parseDouble(StaffProducts.this.priceTextField.getText());
            minimumPrice = Double.parseDouble(StaffProducts.this.minimumPriceTextField.getText());
            discountPercent = Double.parseDouble(StaffProducts.this.discountPercentField.getText());
            return this;
        }
    }
}
