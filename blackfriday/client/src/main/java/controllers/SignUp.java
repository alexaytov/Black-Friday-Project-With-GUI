package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import openjfx.Main;
import user.Client;
import user.interfaces.User;

import java.io.IOException;

import static validator.Validator.validateAge;
import static validator.Validator.validateString;

public class SignUp {

    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXTextField firstNameField;

    @FXML
    private JFXTextField lastNameField;

    @FXML
    private JFXTextField ageField;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXTextField passwordField;

    @FXML
    void goToLoginScreen(ActionEvent event) throws IOException {
        signUpButton.getScene().getWindow().hide();

        Stage logIn = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        logIn.setTitle("Log in");
        logIn.setScene(new Scene(root, 600, 350));
        logIn.show();
        logIn.setResizable(false);
    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        try {
            // validation of data
            int age = Integer.parseInt(ageField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            validateString(firstName);
            validateString(lastName);
            validateString(username);
            validateString(password);
            validateAge(age);

            Client client = new Client(username, password, firstName, lastName, age);

            if (registerUser(client)) {
                // client registered successfully
                signUpButton.getScene().getWindow().hide();
                Stage logIn = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                logIn.setTitle("Log in");
                logIn.setScene(new Scene(root, 600, 400));
                logIn.show();
                logIn.setResizable(false);
            } else {
                ExceptionMessages.showWarningDialog(ConstantMessages.CLIENT_ALREADY_EXISTS);
            }


        } catch (NumberFormatException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.FILL_IN_ALL_FIELDS);
        }
    }

    private boolean registerUser(User toBeRegisteredUser) {
        try {
            Main.store.getOos().writeObject("register client");
            Main.store.getOos().writeObject(toBeRegisteredUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return Main.store.getOis().readObject().equals("true");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
