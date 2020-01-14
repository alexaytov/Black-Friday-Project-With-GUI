package controllers.staff;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import commonMessages.CommandNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import user.User;
import util.Operations;
import util.Windows;

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
    void blackFridaySwitch(ActionEvent event) {
        if (blackFriday.isSelected()) {
            App.serverConnection.write(CommandNames.START_BLACK_FRIDAY);
        } else {
            App.serverConnection.write(CommandNames.STOP_BLACK_FRIDAY);
        }
    }

    @FXML
    void clients(ActionEvent event) {
        this.welcomeLabel.getScene().getWindow().hide();
        Operations.loadWindow(Windows.STAFF_CLIENTS_PATH, Windows.STAFF_CLIENTS_WIDTH, Windows.STAFF_CLIENTS_HEIGHT);
    }

    @FXML
    void earnings(ActionEvent event) {
        this.earningsButton.getScene().getWindow().hide();
        Operations.loadWindow(Windows.EARNINGS_PATH, Windows.EARNINGS_WIDTH, Windows.EARNINGS_HEIGHT);
    }

    @FXML
    void logout(ActionEvent event) {
        App.serverConnection.write(CommandNames.LOGOUT);
        this.earningsButton.getScene().getWindow().hide();
        Operations.loadWindow(Windows.LOGIN_PATH, Windows.LOGIN_WIDTH, Windows.LOGIN_HEIGHT);
    }

    @FXML
    void products(ActionEvent event) {
        this.logoutButton.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/staffProducts.fxml", 600, 730);
    }

    @FXML
    void registerStaff(ActionEvent event) {
        Operations.loadWindow(Windows.REGISTER_STAFF_PATH, Windows.REGISTER_STAFF_WIDTH, Windows.REGISTER_STAFF_HEIGHT);
        this.welcomeLabel.getScene().getWindow().hide();
    }

    @FXML
    void settings(ActionEvent event) {
        this.welcomeLabel.getScene().getWindow().hide();
        FXMLLoader loader = Operations.loadWindow(Windows.STAFF_SETTINGS_PATH, Windows.STAFF_SETTINGS_WIDTH, Windows.STAFF_SETTINGS_HEIGHT);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set on/off black friday toggle
        App.serverConnection.write(CommandNames.IS_BLACK_FRIDAY);
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
