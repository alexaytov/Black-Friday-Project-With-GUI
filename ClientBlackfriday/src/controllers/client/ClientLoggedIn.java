package controllers.client;

import application.Main;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import common.ConstantMessages;
import common.Operations;
import controllers.settings.StaffSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import product.Product;
import user.interfaces.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ClientLoggedIn implements Initializable {

    private User user;

    private List<Product> products;

    @FXML
    private Label welcomeMessage;

    @FXML
    private JFXTextField productSearch;

    @FXML
    private JFXRadioButton sortByNameButton;

    @FXML
    private ToggleGroup sortGroup;

    @FXML
    private JFXRadioButton sortByPriceAscendingButton;

    @FXML
    private JFXRadioButton sortByPriceDescnedingButton;

    @FXML
    private HBox productsFilterChoice;

    @FXML
    private JFXRadioButton allProductsButton;

    @FXML
    private ToggleGroup productGroup;

    @FXML
    private JFXRadioButton blackFridayButton;

    @FXML
    private VBox vBoxWithProducts;

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Main.store.getOos().writeObject("logout");
        this.vBoxWithProducts.getScene().getWindow().hide();
        Operations.loadWindow(this.getClass(), "/FXML/login.fxml", "Log In", 600, 350);
    }

    @FXML
    void settings(ActionEvent event) throws IOException {
        this.vBoxWithProducts.getScene().getWindow().hide();
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/staff/staffSettings.fxml", "Settings", 600, 600);
        StaffSettings staffSettings = loader.getController();
        staffSettings.initUser(this.user);
    }


    @FXML
    void productsSortingButtonSelected() {

        sortProducts(this.products);
        fillVBoxWithProducts(this.products, this.vBoxWithProducts);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Main.store.getOos().writeObject("has promotions");
            boolean storeHasPromotions = (boolean) Main.store.getOis().readObject();
            if (storeHasPromotions) {
                productsFilterChoice.setVisible(true);
            } else {
                productsFilterChoice.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            this.loadAllProducts(null);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fillVBoxWithProducts(List<Product> products, VBox vBoxWithProducts) {
        vBoxWithProducts.getChildren().clear();
        this.products = products;
        sortProducts(products);

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

    @FXML
    void loadAllProducts(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("get client products");
        List<Product> products = (List<Product>) Main.store.getOis().readObject();
        fillVBoxWithProducts(products, this.vBoxWithProducts);
        if (products.size() == 0) {
            noResultsMessage();
        }

    }

    @FXML
    void loadDiscountedProducts(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("get client discounted products");
        List<Product> products = (List<Product>) Main.store.getOis().readObject();
        fillVBoxWithProducts(products, this.vBoxWithProducts);

        if (products.size() == 0) {
            noResultsMessage();
        }
    }


    @FXML
    void searchProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        String searchedProductName = this.productSearch.getText();
        if (allProductsButton.isSelected()) {
            Main.store.getOos().writeObject("search client all products");
            Main.store.getOos().writeObject(searchedProductName);
            List<Product> products = (List<Product>) Main.store.getOis().readObject();

            fillVBoxWithProducts(products, this.vBoxWithProducts);
        } else {
            Main.store.getOos().writeObject("search client discounted products");
            Main.store.getOos().writeObject(searchedProductName);
            List<Product> products = (List<Product>) Main.store.getOis().readObject();

            fillVBoxWithProducts(products, this.vBoxWithProducts);
        }
        if (this.vBoxWithProducts.getChildren().size() == 0) {
            // no results found
            noResultsMessage();
        }
    }

    private void sortProducts(List<Product> products) {
        if (this.sortByNameButton.isSelected()) {
            products.sort(Comparator.comparing(a -> a.getName().toLowerCase()));
        } else if (this.sortByPriceAscendingButton.isSelected()) {
            products.sort(Comparator.comparing(Product::getPrice));
        } else if (this.sortByPriceDescnedingButton.isSelected()) {
            products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }
    }


    private void noResultsMessage() {
        Label noResultsLabel = new Label(ConstantMessages.NO_RESULTS);
        HBox hBox = new HBox(noResultsLabel);
        noResultsLabel.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);

        noResultsLabel.setStyle("-fx-alignment: center; -fx-font-size: 14pt; -fx-fill-width: true;");
        this.vBoxWithProducts.getChildren().add(hBox);

        this.vBoxWithProducts.setAlignment(Pos.CENTER);
    }

    private VBox createProductVBox(Product product) {
        VBox vbox = new VBox();
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        ImageView imageView = new ImageView(new Image(is));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);


        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            this.welcomeMessage.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/client/clientChosenProduct.fxml"));
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root, 600, 500);
            stage.setTitle("Product");
            ChosenProduct controller = loader.getController();
            controller.initProduct(product);

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

        vbox.setPrefWidth(300);
        vbox.spacingProperty().setValue(10);
        vbox.alignmentProperty().set(Pos.CENTER);
        // set product name and price background color to green is they are discounted
        if (product.isDiscounted()) {
            name.setStyle("-fx-background-color:#23cba7;" +
                    "-fx-border-width: 2;");
        }
        return vbox;
    }

    public void initUser(User user) {
        this.user = user;
        String welcomeText = "Welcome " + this.user.getFirstName() + " " + this.user.getLastName() + "!";
        this.welcomeMessage.setText(welcomeText);
    }
}
