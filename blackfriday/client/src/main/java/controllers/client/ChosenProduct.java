package controllers.client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import openjfx.Main;
import product.Product;
import util.Operations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

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
    void buyProduct(ActionEvent event) throws IOException, ClassNotFoundException {
        int wantedQuantity = this.wantedQuantity.getValue();
        Main.store.getOos().writeObject("buy product");
        Main.store.getOos().writeObject(this.product.getName());
        Main.store.getOos().writeObject(wantedQuantity);

        if ((boolean) Main.store.getOis().readObject()) {
            ConstantMessages.confirmationPopUp(String.format("You just purchased %d of %s.", wantedQuantity, this.product.getName()));
            this.product.setQuantity(this.product.getQuantity() - wantedQuantity);
            this.quantityField.setText(String.valueOf(this.product.getQuantity()));
        } else {
            ExceptionMessages.showWarningDialog("Sorry there was a problem with this purchase. Please Try again");
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        this.nameField.getScene().getWindow().hide();
        Operations.loadWindow(this.getClass(), "/openjfx/client/clientLoggedIn.fxml", "Client", 650, 800);

    }

    public void initProduct(Product product) {
        this.product = product;

        this.nameField.setText(this.product.getName());
        this.descriptionField.setText(this.product.getDescription());
        this.sizeField.setText(this.product.getSize());
        this.quantityField.setText(String.valueOf(this.product.getQuantity()));
        this.priceField.setText(String.valueOf(this.product.getPurchasePrice()));
        InputStream is = new ByteArrayInputStream(product.getImageContent());
        this.productImage.setImage(new Image(is));


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> integerSpinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        this.wantedQuantity.setValueFactory(integerSpinnerValueFactory);

        Timeline validateWantedQuantity = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (this.wantedQuantity.getValue() > this.product.getQuantity()) {
                this.wantedQuantity.setStyle("-fx-border-color: red;");
                this.buyProductButton.setDisable(true);
            } else {
                this.wantedQuantity.setStyle("-fx-border-color: #23cba7;");
                this.buyProductButton.setDisable(false);
            }
        }));
        validateWantedQuantity.setCycleCount(Timeline.INDEFINITE);
        validateWantedQuantity.play();
    }
}
