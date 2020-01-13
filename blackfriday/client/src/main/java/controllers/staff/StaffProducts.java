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
import application.Main;
import product.Product;
import util.Operations;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static commonMessages.ConstantMessages.STAGE_TITLE;
import static util.Operations.confirmationPopUp;
import static util.Operations.showWarningDialog;
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
    private JFXTextField productSearch;

    @FXML
    private JFXRadioButton productsSortByNameButton;

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
    private JFXRadioButton quantityControlSortByNameButton;

    @FXML
    private JFXRadioButton quantityControlSortByPriceAscendingButton;

    @FXML
    private JFXRadioButton quantityControlSortByPriceDescendingButton;

    @FXML
    private VBox vBoxWithProductsQualityControl;

    @FXML
    private Tab createProductTab;

    @FXML
    private JFXRadioButton blackFridayButton;

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
        // acceptable extension for picture
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
        // products tab is selected
        // load all products in GUI
        this.allProductsButton.selectedProperty().setValue(true);
        Main.tcpServer.write("get staff products");
        List<Product> products = Main.tcpServer.read();
        fillVBoxWithProducts(products, this.vBoxWithProducts);
    }

    @FXML
    void searchQuantityControl() throws IOException, ClassNotFoundException {
        // clear previously loaded products in GUI
        this.vBoxWithProductsQualityControl.getChildren().clear();
        // execute quantity control command
        Main.tcpServer.write("search quantity control");
        Main.tcpServer.write(this.maximumQuantity);
        List<Product> products = Main.tcpServer.read();
        this.products = products;
        if (products.size() == 0) {
            noResultsMessage();
        } else {
            fillVBoxWithProducts(products, this.vBoxWithProductsQualityControl);
        }
    }

    private void fillVBoxWithProducts(List<Product> products, VBox vBoxWithProducts) {
        // clear previously shown products
        vBoxWithProducts.getChildren().clear();
        this.products = products;
        // use productTab/quantityControlTab sort button based on which tab is selected
        if (productsTab.isSelected()) {
            sortProducts(products, productsSortByNameButton, productsSortByPriceAscendingButton, productsSortByPriceDescendingButton);
        } else if (quantityControlTab.isSelected()) {
            sortProducts(products, quantityControlSortByNameButton, quantityControlSortByPriceAscendingButton, quantityControlSortByPriceDescendingButton);
        }
        // fill GUI with products
        fillVBoxWithHBoxWithProducts(products, vBoxWithProducts);
    }

    private void fillVBoxWithHBoxWithProducts(List<Product> products, VBox vBoxWithProducts) {
        // fills VBox with two columns of products
        // which include product picture,name and price
        int index = 0;
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        for (Product product : products) {
            index++;
            hbox.getChildren().add(createProductVBox(product));
            if ((index % 2 == 0 && index != 0) || products.size() == 1) {
                vBoxWithProducts.getChildren().add(hbox);
                hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
            }
        }
        if (products.size() % 2 != 0) {
            vBoxWithProducts.getChildren().add(hbox);
            hbox.setAlignment(Pos.CENTER_LEFT);
        }
    }

    private VBox createProductVBox(Product product) {
        VBox vbox = new VBox();
        // load image in ImageView
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        ImageView image = new ImageView(new Image(is));
        image.setPreserveRatio(true);
        image.setFitWidth(200);
        // adds on mouse clicked event handler
        // to go to staff chosen product window
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
            stage.setTitle(STAGE_TITLE);
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
        // chases purchase price is all products button is selected
        // and discounted price if black friday products button is selected
        double productPrice;
        if (blackFridayButton.isSelected()) {
            productPrice = product.getDiscountedPrice();
        } else {
            productPrice = product.getPurchasePrice();
        }
        // format product price to two decimal places
        productPrice = ((int) (productPrice * 100)) / 100.0;
        // create product label - product name + product price
        Label name = new Label(product.getName() + " " + productPrice + " лв.");
        hbox.getChildren().add(name);
        name.setFont(new Font(14));
        // set HBox and VBox properties
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(image, hbox);
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
        // sort products based on which option is selected
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
        loadProductsFromServerToGUI("get staff products");
    }

    private void loadProductsFromServerToGUI(String serverCommand) throws IOException, ClassNotFoundException {
        // clears previously loaded products in GUI
        this.vBoxWithProducts.getChildren().clear();
        Main.tcpServer.write(serverCommand);
        List<Product> products = Main.tcpServer.read();
        fillVBoxWithProducts(products, this.vBoxWithProducts);
        if (products.size() == 0) {
            noResultsMessage();
        }
    }

    @FXML
    void showAllProducts(ActionEvent event) throws IOException, ClassNotFoundException {
        // loads all products to GUI based on selected
        // radio button - all products, black friday products
        if (allProductsButton.isSelected()) {
            loadProductsFromServerToGUI("get staff products");
        } else {
            loadProductsFromServerToGUI("get staff discounted products");
        }
    }

    @FXML
    void loadDiscountedProducts(ActionEvent event) throws IOException, ClassNotFoundException {
        loadProductsFromServerToGUI("get staff discounted products");
    }

    @FXML
    void searchProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        // clear products from UI
        this.vBoxWithProducts.getChildren().clear();
        // send search command based of selected type of products
        if (allProductsButton.isSelected()) {
            Main.tcpServer.write("search staff all products");
        } else {
            Main.tcpServer.write("search staff discounted products");
        }
        Main.tcpServer.write(this.productSearch.getText());
        // get products from server
        List<Product> products = Main.tcpServer.read();
        // fill UI with products
        fillVBoxWithProducts(products, this.vBoxWithProducts);
        if (vBoxWithProducts.getChildren().size() == 0) {
            // no results found
            noResultsMessage();
        }
    }

    private void noResultsMessage() {
        // create label with no result message
        Label noResultsLabel = new Label(ConstantMessages.NO_RESULTS);
        noResultsLabel.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(noResultsLabel);
        hBox.setAlignment(Pos.CENTER);
        this.vBoxWithProducts.setAlignment(Pos.CENTER);
        noResultsLabel.setStyle("-fx-alignment: center; -fx-font-size: 14pt; -fx-fill-width: true;");
        this.vBoxWithProducts.getChildren().add(hBox);
    }


    @FXML
    void goToMainMenu(ActionEvent event) throws IOException {
        this.nameTextField.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/staffLoggedIn.fxml", 600, 600);
    }

    @FXML
    void productSortButtonSelected() {
        sortProducts(this.products, this.productsSortByNameButton, this.productsSortByPriceAscendingButton, this.productsSortByPriceDescendingButton);
        fillVBoxWithProducts(this.products, this.vBoxWithProducts);
    }

    @FXML
    void quantityControlSortButtonSelected() {
        sortProducts(this.products, this.quantityControlSortByNameButton, this.quantityControlSortByPriceAscendingButton, this.quantityControlSortByPriceDescendingButton);
        fillVBoxWithProducts(this.products, this.vBoxWithProductsQualityControl);
    }

    @FXML
    void createProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        // gets all fields information
        GetProductInformationFromTextFields getProductInformationFromTextFields = new GetProductInformationFromTextFields().invoke();
        String name = getProductInformationFromTextFields.getName();
        String description = getProductInformationFromTextFields.getDescription();
        String size = getProductInformationFromTextFields.getSize();
        int quantity = getProductInformationFromTextFields.getQuantity();
        double price = getProductInformationFromTextFields.getPrice();
        double minimumPrice = getProductInformationFromTextFields.getMinimumPrice();
        double discountPercent = getProductInformationFromTextFields.getDiscountPercent();
        Product product = new Product(name, description, quantity, price, minimumPrice, discountPercent, Files.readAllBytes(this.createProductPictureFile.toPath()), size);
        // send new product ot server
        Main.tcpServer.write("create product");
        Main.tcpServer.write(product);
        // shows uer if product was created successfully
        if (Main.tcpServer.read()) {
            confirmationPopUp(ConstantMessages.PRODUCT_CREATED);
            resetAllCreateProductField();
        } else {
            showWarningDialog(ExceptionMessages.Product_ALREADY_EXISTS);
        }
    }

    private void resetAllCreateProductField() {
        // remove all information from GUI fields
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
        // sets quantityControl button disable/enable based on input in quantityControlField
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
        // start quantityControl field validation based on if the
        // quantity control tab is selected
        this.quantityControlTab.selectedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                verifyQuantityControlField.stop();
            } else {
                verifyQuantityControlField.play();

            }
        });
        // sets create product button disable/enable
        // based on validation of all fields
        Timeline checkIfAllDataIsValid = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (areAllCreateProductFieldsValid()) {
                this.createProductButton.setDisable(false);
            } else {
                this.createProductButton.setDisable(true);
            }
        }));
        checkIfAllDataIsValid.setCycleCount(Timeline.INDEFINITE);
        // start all field validation based on
        // if the create product tab is selected
        this.createProductTab.selectedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                checkIfAllDataIsValid.stop();
            } else {
                checkIfAllDataIsValid.play();
            }
        });
        Color textFieldDefaultUnfocusedColor = Color.valueOf(ConstantMessages.TEXT_FIELD_UNFOCUSED_COLOR);
        // when field is unfocused set unfocused color to
        // red/default unfocused color based if data is invalid/valid
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
        // when field is unfocused set unfocused color to
        // red/default unfocused color based if data is invalid/valid
        descriptionTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                textFieldColorChangeValidation(textFieldDefaultUnfocusedColor, descriptionTextField);
            }
        });
        // when field is unfocused set unfocused color to
        // red/default unfocused color based if data is invalid/valid
        priceTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                priceMinimumPriceDiscountPercentValidation(textFieldDefaultUnfocusedColor);
            }
        });
        // when field is unfocused set unfocused color to
        // red/default unfocused color and sets create product
        // button to disabled/enabled based if data is invalid/valid
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
        // when minimumPriceTextField is unfocused
        // execute validation of:
        // minimum price
        // price
        // discount percent
        minimumPriceTextField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                priceMinimumPriceDiscountPercentValidation(textFieldDefaultUnfocusedColor);
            }
        });
        // when minimumPriceTextField is unfocused
        // execute validation of:
        // minimum price
        // price
        // discount percent
        discountPercentField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                priceMinimumPriceDiscountPercentValidation(textFieldDefaultUnfocusedColor);
            }
        });
    }

    private void priceMinimumPriceDiscountPercentValidation(Color textFieldDefaultUnfocusedColor) {
        // validates:
        // minimum price
        // discount percent
        // price
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
        // sets textField unfocused color to red and
        // disables create product button if input data is invalid
        try {
            requireNonBlank(textField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
            textField.setUnFocusColor(textFieldDefaultUnfocusedColor);
        } catch (IllegalArgumentException ex) {
            createProductButton.setDisable(true);
            textField.setUnFocusColor(Color.RED);
        }
    }

    private boolean areAllCreateProductFieldsValid() {
        try {
            // check if an image was selected
            if (this.createProductPictureFile == null) {
                throw new IllegalArgumentException();
            }
            // get all input data from fields
            String name = this.nameTextField.getText();
            String description = this.descriptionTextField.getText();
            String size = this.sizeTextField.getText();
            double price = Double.parseDouble(this.priceTextField.getText());
            double minimumPrice = Double.parseDouble(this.minimumPriceTextField.getText());
            double discountPercent = Double.parseDouble(this.discountPercentField.getText());
            int quantity = Integer.parseInt(this.quantityTextField.getText());
            // validate all fields
            requireNonBlank(name, ExceptionMessages.NAME_NULL_OR_EMPTY);
//            requireNonBlank(size, ExceptionMessages.SIZE_NULL_OR_EMPTY);
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

        String getDescription() {
            return description;
        }

        public String getSize() {
            return size;
        }

        int getQuantity() {
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
            // gets all input information from text fields
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
