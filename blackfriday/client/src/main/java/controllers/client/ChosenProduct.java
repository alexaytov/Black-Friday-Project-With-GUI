package controllers.client;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import product.Product;
import util.Operations;
import util.Windows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;

public class ChosenProduct implements Initializable {

    private Product product;

    @FXML
    private JFXButton buyProductButton;

    @FXML
    private ImageView productImage;

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
    private Spinner<Integer> wantedQuantity;

    @FXML
    private GridPane productGrid;


    @FXML
    private RowConstraints sizeRow;

    @FXML
    private Label sizeLabel;

    @FXML
    void buyProduct(ActionEvent event) {
        // get entered wanted quantity
        int wantedQuantity = this.wantedQuantity.getValue();
        // send information to server
        App.serverConnection.write("buy product");
        App.serverConnection.write(this.product.getName());
        App.serverConnection.write(wantedQuantity);
        // get server confirmation
        if (App.serverConnection.read()) {
            confirmationPopUp(String.format(ConstantMessages.PRODUCT_BOUGHT_SUCCESSFULLY, wantedQuantity, this.product.getName()));
            this.product.setQuantity(this.product.getQuantity() - wantedQuantity);
            this.quantityField.setText(String.valueOf(this.product.getQuantity()));
        } else {
            Operations.showWarningDialog(ExceptionMessages.PURCHASE_PROBLEM);
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        // go to previous window
        this.nameField.getScene().getWindow().hide();
        Operations.loadWindow(Windows.CLIENT_LOGGED_IN_PATH, Windows.CLIENT_LOGGED_IN_WIDTH, Windows.CLIENT_LOGGED_IN_HEIGHT);
    }

    public void initProduct(Product product) {
        this.product = product;
        // set text fields values
        fillTextFieldsWithInformation(product);
    }

    private void fillTextFieldsWithInformation(Product product) {
        this.nameField.setText(this.product.getName());
        this.descriptionField.setText(this.product.getDescription());
        if (this.product.getSize().trim().isEmpty()) {
            this.productGrid.getChildren().removeAll(this.sizeLabel, this.sizeField);
        } else {
            this.sizeField.setText(this.product.getSize());
        }
        this.quantityField.setText(String.valueOf(this.product.getQuantity()));
        this.priceField.setText(String.valueOf(this.product.getPurchasePrice()));
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        this.productImage.setImage(new Image(is));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize spinner for wanted quantity
        SpinnerValueFactory<Integer> integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        this.wantedQuantity.setValueFactory(integerSpinnerValueFactory);
        // timeline to check if wanted quantity is acceptable
        Timeline validateWantedQuantity = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (this.wantedQuantity.getValue() > this.product.getQuantity()) {
                this.wantedQuantity.setStyle("-fx-border-color: red;");
                this.buyProductButton.setDisable(true);
            } else {
                this.wantedQuantity.setStyle("-fx-border-color: #23cba7;");
                this.buyProductButton.setDisable(false);
            }
        }));
        // start validateWantedQuantity timeline
        validateWantedQuantity.setCycleCount(Timeline.INDEFINITE);
        validateWantedQuantity.play();
    }
}
