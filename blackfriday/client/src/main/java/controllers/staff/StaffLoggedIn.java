package controllers.staff;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import user.User;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffLoggedIn implements Initializable {

    private User user;

    @FXML
    private JFXToggleButton blackFriday;

    @FXML
    private JFXButton earningsButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void blackFridaySwitch(ActionEvent event) throws IOException {
        if (blackFriday.isSelected()) {
            App.serverConnection.write("start blackFriday");
        } else {
            App.serverConnection.write("stop blackFriday");
        }
    }

    @FXML
    void clients(ActionEvent event) throws IOException {
        this.welcomeLabel.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/clients.fxml", 600, 800);
    }

    @FXML
    void earnings(ActionEvent event) throws IOException {
        this.earningsButton.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/earnings.fxml", 600, 500);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        App.serverConnection.write("logout");
        this.earningsButton.getScene().getWindow().hide();
        Operations.loadWindow("/view/login.fxml", 600, 350);
    }

    @FXML
    void products(ActionEvent event) throws IOException {
        this.logoutButton.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/staffProducts.fxml", 600, 730);
    }

    @FXML
    void registerStaff(ActionEvent event) throws IOException {
        Operations.loadWindow("/view/staff/registerStaff.fxml", 600, 630);
        this.welcomeLabel.getScene().getWindow().hide();
    }

    @FXML
    void settings(ActionEvent event) throws IOException {
        this.welcomeLabel.getScene().getWindow().hide();
        FXMLLoader loader = Operations.loadWindow("/view/staff/staffSettings.fxml", 600, 400);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set on/off black friday toggle
        App.serverConnection.write("is blackFriday");
        if (App.serverConnection.read()) {
            blackFriday.selectedProperty().setValue(true);
        } else {
            blackFriday.selectedProperty().setValue(false);
        }
    }

    public void initUser(User user) {
        this.user = user;
        setWelcomeMessage();
    }

    private void setWelcomeMessage() {
        this.welcomeLabel.setText("Welcome " + this.user.getFirstName() + " " + this.user.getLastName() + "!");
    }
}
