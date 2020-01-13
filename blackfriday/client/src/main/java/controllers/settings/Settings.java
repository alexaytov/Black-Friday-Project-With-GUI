package controllers.settings;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.client.ClientLoggedIn;
import controllers.staff.StaffLoggedIn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import user.Permission;
import user.User;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;

public class Settings implements Initializable {

    private User user;

    @FXML
    private JFXTextField firstNameField;

    @FXML
    private JFXTextField lastNameField;

    @FXML
    private JFXTextField ageField;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXButton backButton;


    @FXML
    void ChangeAge(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/settings/ageChange.fxml", 600, 200);
        AgeChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeFirstName(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/settings/firstNameChange.fxml", 600, 200);
        FirstNameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeLastName(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/settings/lastNameChange.fxml", 600, 200);
        LastNameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changePassword(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/settings/passwordChange.fxml", 600, 200);
        PasswordChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeUsername(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow("/view/settings/usernameChange.fxml", 600, 200);
        UsernameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        this.usernameField.getScene().getWindow().hide();
        FXMLLoader loader;
        // load admin/client window based on user permission
        if ((this.user.getPermission().equals(Permission.CLIENT))) {
            loader = Operations.loadWindow("/view/client/clientLoggedIn.fxml", 650, 800);
            ClientLoggedIn controller = loader.getController();
            controller.initUser(this.user);
        } else if (this.user.getPermission().equals(Permission.ADMIN)) {
            loader = Operations.loadWindow("/view/staff/staffLoggedIn.fxml", 600, 600);
            StaffLoggedIn controller = loader.getController();
            controller.initUser(this.user);
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) throws IOException, ClassNotFoundException {
        App.serverConnection.write("delete user");
        // show user if command was executed successfully
        if (App.serverConnection.read()) {
            confirmationPopUp("User successfully deleted!");
            this.backButton.getScene().getWindow().hide();
            Operations.loadWindow("/view/login.fxml", 600, 350);
        } else {
            confirmationPopUp("There was a problem deleting user!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get currently logged user information from server
        App.serverConnection.write("get logged in user");
        this.user = App.serverConnection.read();
        // refresh all fields text properties
        Timeline refreshTextFieldsTimeline = new Timeline(new KeyFrame(Duration.millis(100), event -> refreshTextFields()));
        refreshTextFieldsTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTextFieldsTimeline.play();
    }

    private void refreshTextFields() {
        // update all user information fields
        firstNameField.setText(this.user.getFirstName());
        lastNameField.setText(this.user.getLastName());
        ageField.setText(String.valueOf(this.user.getAge()));
        usernameField.setText(this.user.getUsername());
    }
}
