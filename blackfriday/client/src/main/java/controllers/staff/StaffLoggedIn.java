package controllers.staff;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import openjfx.Main;
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
            Main.tcpServer.write("start blackFriday");
        } else {
            Main.tcpServer.write("stop blackFriday");
        }
    }

    @FXML
    void clients(ActionEvent event) throws IOException {
        this.welcomeLabel.getScene().getWindow().hide();
        Operations.loadWindow(this.getClass(), "/view/staff/clients.fxml", "ClientPurchases", 600, 800);
    }

    @FXML
    void earnings(ActionEvent event) throws IOException {
        Operations.changeWindows(this.earningsButton, "store/earnings", "/view/staff/earnings.fxml", this.getClass(), 600, 500);
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Main.tcpServer.write("logout");
        Operations.changeWindows(this.logoutButton, "staffLoggedIn", "/view/login.fxml", this.getClass(), 600, 350);
    }

    @FXML
    void products(ActionEvent event) throws IOException {
        Operations.changeWindows(this.logoutButton, "Products", "/view/staff/staffProducts.fxml", this.getClass(), 600, 730);
    }

    @FXML
    void registerStaff(ActionEvent event) throws IOException {
        Operations.loadWindow(this.getClass(), "/view/staff/registerStaff.fxml", "Register Staff", 600, 630);
        this.welcomeLabel.getScene().getWindow().hide();
    }

    @FXML
    void settings(ActionEvent event) throws IOException {
        this.welcomeLabel.getScene().getWindow().hide();
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffSettings.fxml", "Settings", 600, 400);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Main.tcpServer.write("is blackFriday");
            if (Main.tcpServer.read()) {
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
