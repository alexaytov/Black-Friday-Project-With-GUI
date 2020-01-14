package controllers.settings;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
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
import util.Windows;

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
    void ChangeAge(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.AGE_CHANGE_PATH, Windows.AGE_CHANGE_WIDTH, Windows.AGE_CHANGE_HEIGHT);
        AgeChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeFirstName(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.FIRST_NAME_CHANGE_PATH, Windows.FIRST_NAME_CHANGE_WIDTH, Windows.FIRST_NAME_CHANGE_HEIGHT);
        FirstNameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeLastName(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.LAST_NAME_CHANGE_PATH, Windows.LAST_NAME_CHANGE_WIDTH, Windows.LAST_NAME_CHANGE_HEIGHT);
        LastNameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changePassword(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.PASSWORD_CHANGE_PATH, Windows.PASSWORD_CHANGE_WIDTH, Windows.PASSWORD_CHANGE_HEIGHT);
        PasswordChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeUsername(ActionEvent event) {
        FXMLLoader loader = Operations.loadWindow(Windows.USERNAME_CHANGE_PATH, Windows.USERNAME_CHANGE_WIDTH, Windows.USERNAME_CHANGE_HEIGHT);
        UsernameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void goBack(ActionEvent event) {
        this.usernameField.getScene().getWindow().hide();
        FXMLLoader loader;
        // load admin/client window based on user permission
        if ((this.user.getPermission().equals(Permission.CLIENT))) {
            loader = Operations.loadWindow(Windows.CLIENT_LOGGED_IN_PATH, Windows.CLIENT_LOGGED_IN_WIDTH, Windows.CLIENT_LOGGED_IN_HEIGHT);
            ClientLoggedIn controller = loader.getController();
            controller.initUser(this.user);
        } else if (this.user.getPermission().equals(Permission.ADMIN)) {
            loader = Operations.loadWindow(Windows.STAFF_LOGGED_IN_PATH, Windows.STAFF_LOGGED_IN_WIDTH, Windows.STAFF_LOGGED_IN_HEIGHT);
            StaffLoggedIn controller = loader.getController();
            controller.initUser(this.user);
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        App.serverConnection.write("delete user");
        // show user if command was executed successfully
        if (App.serverConnection.read()) {
            confirmationPopUp(ConstantMessages.USER_DELETED);
            this.backButton.getScene().getWindow().hide();
            Operations.loadWindow(Windows.LOGIN_PATH, Windows.LOGIN_WIDTH, Windows.LOGIN_HEIGHT);
        } else {
            confirmationPopUp(ExceptionMessages.PROBLEM_DELETING_USER);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get currently logged user information from server
        App.serverConnection.write(CommandNames.GET_LOGGED_IN_USER);
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
