package controllers.staff;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import application.Main;
import user.Permission;
import user.User;
import util.Operations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static util.Operations.confirmationPopUp;
import static validator.Validator.requireNonBlank;
import static validator.Validator.requireNonNegative;

public class RegisterStaff implements Initializable {

    private String firstName;

    private String lastName;

    private int age;

    private String username;

    private String password;

    @FXML
    private JFXButton registerStaffButton;

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

    private Color textFieldDefaultUnfocusedColor = Color.GREEN;

    private Color invalidInputFieldColor = Color.RED;

    @FXML
    void registerStaff(ActionEvent event) throws IOException, ClassNotFoundException {
        if (checkAllFields()) {
            Main.tcpServer.write("register user");
            User user = new User(this.username, this.password, Permission.ADMIN, this.firstName, this.lastName, this.age);
            Main.tcpServer.write(user);
            // show user if staff was registered successfully
            if (Main.tcpServer.read()) {
                confirmationPopUp(ConstantMessages.STAFF_REGISTERED);
            } else {
                confirmationPopUp(ConstantMessages.STAFF_ALREADY_EXISTS);
            }
        } else {
            confirmationPopUp(ExceptionMessages.FILL_IN_ALL_FIELDS);
        }
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        this.registerStaffButton.getScene().getWindow().hide();
        Operations.loadWindow("/view/staff/staffLoggedIn.fxml", 600, 600);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // when field unfocused validate imputed data
        // if data is invalid change unfocused color to red
        // else set it do default color
        this.firstNameField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    requireNonBlank(this.firstNameField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
                    this.firstNameField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.firstNameField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });
        // when field unfocused validate imputed data
        // if data is invalid change unfocused color to red
        // else set it do default color
        this.lastNameField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    requireNonBlank(this.lastNameField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
                    this.lastNameField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.lastNameField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });
        // when field unfocused validate imputed data
        // if data is invalid change unfocused color to red
        // else set it do default color
        this.usernameField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    requireNonBlank(this.usernameField.getText(), ExceptionMessages.NAME_NULL_OR_EMPTY);
                    this.usernameField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.usernameField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });
        // when field unfocused validate imputed data
        // if data is invalid change unfocused color to red
        // else set it do default color
        this.ageField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    requireNonNegative(Integer.parseInt(this.ageField.getText()), ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
                    this.ageField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.ageField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });
        // when field unfocused validate imputed data
        // if data is invalid change unfocused color to red
        // else set it do default color
        this.passwordField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    requireNonBlank(this.passwordField.getText(), ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
                    this.passwordField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.passwordField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });
    }

    private boolean checkAllFields() {
        // validate all fields
        try {
            String firstName = firstNameField.getText();
            requireNonBlank(firstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            String lastName = lastNameField.getText();
            requireNonBlank(lastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            int age = Integer.parseInt(ageField.getText());
            requireNonNegative(age, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
            String username = usernameField.getText();
            requireNonBlank(username, ExceptionMessages.NAME_NULL_OR_EMPTY);
            String password = passwordField.getText();
            requireNonBlank(password, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.username = username;
            this.password = password;
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
