package controllers;

import application.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import user.Permission;
import user.User;
import util.Operations;
import util.Windows;

import static util.Operations.showWarningDialog;
import static validator.Validator.requireNonBlank;
import static validator.Validator.requireNonNegative;

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
    void goToLoginScreen(ActionEvent event) {
        signUpButton.getScene().getWindow().hide();
        Operations.loadWindow(Windows.LOGIN_PATH, Windows.LOGIN_WIDTH, Windows.LOGIN_HEIGHT);
    }

    @FXML
    void signUp(ActionEvent event) {
        try {
            // get all input data from GUI
            int age = Integer.parseInt(ageField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            // data validation
            requireNonBlank(firstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            requireNonBlank(lastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            requireNonBlank(username, ExceptionMessages.NAME_NULL_OR_EMPTY);
            requireNonBlank(password, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
            requireNonNegative(age, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
            User user = new User(username, password, Permission.CLIENT, firstName, lastName, age);
            if (registerUser(user)) {
                // client registered successfully
                signUpButton.getScene().getWindow().hide();
                Operations.loadWindow(Windows.LOGIN_PATH, Windows.LOGIN_WIDTH, Windows.LOGIN_HEIGHT);
            } else {
                showWarningDialog(ConstantMessages.CLIENT_ALREADY_EXISTS);
            }
        } catch (NumberFormatException ex) {
            showWarningDialog(ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.FILL_IN_ALL_FIELDS);
        }
    }

    private boolean registerUser(User toBeRegisteredUser) {
        App.serverConnection.write("register user");
        App.serverConnection.write(toBeRegisteredUser);
        return App.serverConnection.read();
    }
}
