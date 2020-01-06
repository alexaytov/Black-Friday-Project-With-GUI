package controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import controllers.client.ClientLoggedIn;
import controllers.staff.StaffLoggedIn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import openjfx.Main;
import user.Client;
import user.Staff;
import user.interfaces.User;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffSettings implements Initializable {

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
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton backButton;


    @FXML
    void ChangeAge(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/settings/ageChange.fxml", "Age Change", 600, 200);
        AgeChange controller = loader.getController();
        controller.initUser(this.user);

    }

    @FXML
    void changeFirstName(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/settings/firstNameChange.fxml", "First Name Change", 600, 200);
        FirstNameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changeLastName(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/settings/lastNameChange.fxml", "Last Name Change", 600, 200);
        LastNameChange controller = loader.getController();
        controller.initUser(this.user);
    }

    @FXML
    void changePassword(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/settings/passwordChange.fxml", "Password Change", 600, 200);
        PasswordChange controller = loader.getController();
        controller.initUser(this.user);

    }

    @FXML
    void changeUsername(ActionEvent event) throws IOException {
        FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/settings/usernameChange.fxml", "Username Change", 600, 200);
        UsernameChange controller = loader.getController();
        controller.initUser(this.user);

    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        this.usernameField.getScene().getWindow().hide();
        FXMLLoader loader = null;

        if (this.user instanceof Client) {
            loader = Operations.loadWindow(this.getClass(), "/view/client/clientLoggedIn.fxml", "Logged In", 600, 730);
            ClientLoggedIn controller = loader.getController();
            controller.initUser(this.user);
        } else if (this.user instanceof Staff) {
            loader = Operations.loadWindow(this.getClass(), "/view/staff/staffLoggedIn.fxml", "Logged In", 600, 600);
            StaffLoggedIn controller = loader.getController();
            controller.initUser(this.user);
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) throws IOException, ClassNotFoundException {
        Main.store.getOos().writeObject("delete staff");
        if ((boolean) Main.store.getOis().readObject()) {
            ConstantMessages.confirmationPopUp("User successfully deleted!");
            this.backButton.getScene().getWindow().hide();
            Operations.loadWindow(this.getClass(), "/view/login.fxml", "Log In", 600, 600);
        } else {
            ConstantMessages.confirmationPopUp("There was a problem deleting user!");
        }
    }


    private void changePopUpWindow(String variableName, String fxmlFilePath) throws IOException {
        Parent root = FXMLLoader.load(Operations.class.getResource(fxmlFilePath));
        Scene scene = new Scene(root, 400, 150);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(variableName);
        stage.show();
        stage.setResizable(false);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Timeline refreshTextFieldsTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshTextFields()));
        refreshTextFieldsTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTextFieldsTimeline.play();


    }

    private void refreshTextFields() {
        firstNameField.setText(this.user.getFirstName());
        lastNameField.setText(this.user.getLastName());
        ageField.setText(String.valueOf(this.user.getAge()));
        usernameField.setText(this.user.getUsername());
        passwordField.setText(this.user.getPassword());
    }

    public void initUser(User user) {
        this.user = user;
        refreshTextFields();
    }
}
