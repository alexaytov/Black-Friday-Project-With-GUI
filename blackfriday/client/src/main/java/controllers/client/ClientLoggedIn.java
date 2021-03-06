package controllers.client;

import application.App;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ConstantMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import product.Product;
import user.User;
import util.Operations;
import util.Windows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static commonMessages.ConstantMessages.STAGE_TITLE;

public class ClientLoggedIn implements Initializable {

    private List<Product> products;

    @FXML
    private Label welcomeMessage;

    @FXML
    private JFXTextField productSearch;

    @FXML
    private JFXRadioButton sortByNameButton;

    @FXML
    private JFXRadioButton sortByPriceAscendingButton;

    @FXML
    private JFXRadioButton sortByPriceDescendingButton;

    @FXML
    private HBox productsFilterChoice;

    @FXML
    private JFXRadioButton allProductsButton;

    @FXML
    private VBox vBoxWithProducts;

    @FXML
    void goBack(ActionEvent event) {
        // go to previous window
        App.serverConnection.write(CommandNames.LOGOUT);
        this.vBoxWithProducts.getScene().getWindow().hide();
        Operations.loadWindow(Windows.LOGIN_PATH, Windows.LOGIN_WIDTH, Windows.LOGIN_HEIGHT);
    }

    @FXML
    void settings(ActionEvent event) {
        // go to settings menu
        this.vBoxWithProducts.getScene().getWindow().hide();
        Operations.loadWindow(Windows.STAFF_SETTINGS_PATH, Windows.STAFF_SETTINGS_WIDTH, Windows.STAFF_SETTINGS_HEIGHT);
    }

    @FXML
    void productsSortingButtonSelected() {
        // sort products
        sortProducts(this.products);
        // fill UI with products returned from server
        fillVBoxWithProducts(this.products, this.vBoxWithProducts);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // check if black friday is started
        App.serverConnection.write(CommandNames.IS_BLACK_FRIDAY);
        boolean storeHasPromotions = App.serverConnection.read();
        // make black friday products button visible is black friday is active
        if (storeHasPromotions) {
            productsFilterChoice.setVisible(true);
        } else {
            productsFilterChoice.setVisible(false);
        }
        loadAllProductsFromServerToUI(CommandNames.GET_CLIENT_PRODUCTS);
    }

    private void fillVBoxWithProducts(List<Product> products, VBox vBoxWithProducts) {
        // clear UI from previously loaded products
        vBoxWithProducts.getChildren().clear();
        this.products = products;
        // sort products based on selected button
        sortProducts(products);
        // fills VBox
        addHBoxWithProductsToVBox(products, vBoxWithProducts);
    }

    private void addHBoxWithProductsToVBox(List<Product> products, VBox vBoxWithProducts) {
        int index = 0;
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        for (Product product : products) {
            hbox.getChildren().add(createProductVBox(product));
            index++;
            // add final product HBow to VBox and reinitialize HBox
            if ((index % 2 == 0 && index != 0) || products.size() == 1) {
                vBoxWithProducts.getChildren().add(hbox);
                hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
            }
        }
        if (products.size() % 2 != 0) {
            // add remaining product if product size is odd
            hbox.setAlignment(Pos.CENTER_LEFT);
            vBoxWithProducts.getChildren().add(hbox);
        }
    }

    @FXML
    void showAllProducts(ActionEvent event) {
        // show all products based on which button is selected - allProducts and blackFridayProducts
        if (allProductsButton.isSelected()) {
            loadAllProductsFromServerToUI(CommandNames.GET_CLIENT_PRODUCTS);
        } else {
            loadAllProductsFromServerToUI(CommandNames.GET_CLIENT_DISCOUNTED_PRODUCTS);
        }
    }

    @FXML
    void loadAllProducts(ActionEvent event) {
        // loads all products to UI
        loadAllProductsFromServerToUI(CommandNames.GET_CLIENT_PRODUCTS);
    }

    private void loadAllProductsFromServerToUI(String serverCommand) {
        // send command for products to server
        App.serverConnection.write(serverCommand);
        // get products list from server
        List<Product> products = App.serverConnection.read();
        // fill UI with products
        fillVBoxWithProducts(products, this.vBoxWithProducts);
        // if there are not products set no results message to UI
        if (products.size() == 0) {
            noResultsMessage();
        }
    }

    @FXML
    void loadDiscountedProducts(ActionEvent event) {
        // loads all discounted products to UI
        loadAllProductsFromServerToUI(CommandNames.GET_CLIENT_DISCOUNTED_PRODUCTS);
    }

    @FXML
    void searchProduct(ActionEvent event) {
        // gets searched product name
        String searchedProductName = this.productSearch.getText();
        if (allProductsButton.isSelected()) {
            App.serverConnection.write(CommandNames.SEARCH_CLIENT_ALL_PRODUCTS);
            App.serverConnection.write(searchedProductName);
        } else {
            App.serverConnection.write(CommandNames.SEARCH_CLIENT_DISCOUNTED_PRODUCTS);
            App.serverConnection.write(searchedProductName);
        }
        // get products from server
        List<Product> products = App.serverConnection.read();
        // fill UI with result from search
        fillVBoxWithProducts(products, this.vBoxWithProducts);
        if (this.vBoxWithProducts.getChildren().size() == 0) {
            // no results found
            noResultsMessage();
        }
    }

    /**
     * Sorts products list based on which sort button is selected
     *
     * @param products the list of products
     */
    private void sortProducts(List<Product> products) {
        // sorts products based on selected option from sort menu
        if (this.sortByNameButton.isSelected()) {
            products.sort(Comparator.comparing(a -> a.getName().toLowerCase()));
        } else if (this.sortByPriceAscendingButton.isSelected()) {
            products.sort(Comparator.comparing(Product::getPrice));
        } else if (this.sortByPriceDescendingButton.isSelected()) {
            products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }
    }

    private void noResultsMessage() {
        // sets no result message to VBox
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
        // create imageView for product image
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        ImageView imageView = new ImageView(new Image(is));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(200);
        // add event handler to product VBox to enter chosen product window on mouse clicked
        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            this.welcomeMessage.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Windows.CLIENT_CHOSEN_PRODUCT));
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // set new scene
            Scene scene = new Scene(root, 600, 500);
            stage.setTitle(STAGE_TITLE);
            ChosenProduct controller = loader.getController();
            controller.initProduct(product);
            // set new stage
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });

        // get product purchase price
        double productPurchasePrice = product.getPurchasePrice();
        productPurchasePrice = ((int) (productPurchasePrice * 100)) / 100.0;
        // create product label
        Label name = new Label(product.getName() + " " + productPurchasePrice + " лв.");
        name.setFont(new Font(14));
        // set HBox properties
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().add(name);
        hbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(imageView, hbox);
        // set VBox properties
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
        // sets welcome message
        this.welcomeMessage.setText(String.format(ConstantMessages.WELCOME_MESSAGE, user.getFirstName(), user.getLastName()));
    }
}
