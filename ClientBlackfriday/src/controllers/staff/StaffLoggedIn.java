package controllers.staff;

import application.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import common.Operations;
import controllers.settings.StaffSettings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import user.interfaces.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffLoggedIn implements Initializable {

    private User user;

    @FXML
    private JFXToggleButton blackFriday;

    @FXML
    private JFXButton productsButton;

    @FXML
    private JFXButton clientsButton;

    @FXML
    private JFXButton earningsButton;

    @FXML
    private JFXButton settingsButton;

    @FXML
    private JFXButton registerStaffButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    void blackFridaySwitch(ActionEvent event) throws IOException {
        if (blackFriday.isSelected()) {
            Main.store.getOos().writeObject("start blackFriday");
        } else {
            Main.store.getOos().writeObject("stop blackFriday");
        }
    }

    @FXML
    void clients(ActionEvent event) throws IOException {
        this.welcomeLabel.getScene().getWindow().hide();
        Operations.loadWindow(this.getClass(), "/FXML/staff/clients.fxml", "ClientPurchases", 600, 800);
    }

    @FXML
    void earnings(ActionEvent event) throws IOException {
        Operations.changeWindows(this.earningsButton, "earnings", "/FXML/staff/earnings.fxml", this.getClass(), 600, 400);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Main.store.getOos().writeObject("logout");
        Operations.changeWindows(this.logoutButton, "staffLoggedIn", "/FXML/login.fxml", this.getClass(), 600, 350);
    }

    @FXML
    void products(ActionEvent event) throws IOException {
        Operations.changeWindows(this.logoutButton, "Products", "/FXML/staff/staffProducts.fxml", this.getClass(), 600, 730);
    }

    @FXML
    void registerStaff(ActionEvent event) throws IOException {
        Operations.loadWindow(this.getClass(), "/FXML/staff/registerStaff.fxml", "Register Staff", 600, 630);
        this.welcomeLabel.getScene().getWindow().hide();
    }

    @FXML
    void settings(ActionEvent event) throws IOException {
        this.welcomeLabel.getScene().getWindow().hide();
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/FXML/staff/staffSettings.fxml", "Settings", 600, 400);
        StaffSettings controller = loader.getController();
        controller.initUser(this.user);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Main.store.getOos().writeObject("has promotions");
            if ((boolean) Main.store.getOis().readObject()) {
                blackFriday.selectedProperty().setValue(true);
            } else {
                blackFriday.selectedProperty().setValue(false);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
