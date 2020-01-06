package controllers.staff;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import util.Operations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import openjfx.Main;
import user.Staff;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static validator.Validator.validateAge;
import static validator.Validator.validateString;

public class RegisterStaff implements Initializable {

    private String firstName;
    private String lastName;
    private int age;
    private String username;
    private String password;


    @FXML
    private JFXButton registerStaffButton;

    @FXML
    private ImageView progress;

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
            Main.store.getOos().writeObject("register staff");
            Staff staff = new Staff(this.username, this.password, this.firstName, this.lastName, this.age);
            Main.store.getOos().writeObject(staff);
            if ((boolean) Main.store.getOis().readObject()) {
                ConstantMessages.confirmationPopUp(ConstantMessages.STAFF_REGISTERED);
            } else {
                ConstantMessages.confirmationPopUp(ConstantMessages.STAFF_ALREADY_EXISTS);
            }
        } else {
            ConstantMessages.confirmationPopUp("Please enter all field!");
        }
    }

    @FXML
    void goToStaffLoggedIn(ActionEvent event) throws IOException {
        this.registerStaffButton.getScene().getWindow().hide();
        Operations.loadWindow(this.getClass(), "/openjfx/staff/staffLoggedIn.fxml", "BlackFriday", 600, 730);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.firstNameField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    validateString(this.firstNameField.getText());
                    this.firstNameField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.firstNameField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });

        this.lastNameField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    validateString(this.lastNameField.getText());
                    this.lastNameField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.lastNameField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });

        this.usernameField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    validateString(this.usernameField.getText());
                    this.usernameField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.usernameField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });

        this.ageField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    validateAge(Integer.parseInt(this.ageField.getText()));
                    this.ageField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.ageField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });

        this.passwordField.focusedProperty().addListener((arg0, unfocused, focused) -> {
            if (unfocused) {
                try {
                    validateString(this.passwordField.getText());
                    this.passwordField.setUnFocusColor(textFieldDefaultUnfocusedColor);
                } catch (IllegalArgumentException ex) {
                    this.passwordField.setUnFocusColor(invalidInputFieldColor);
                }
            }
        });
    }

    private boolean checkAllFields() {
        try {
            String firstName = firstNameField.getText();
            validateString(firstName);
            String lastName = lastNameField.getText();
            validateString(lastName);
            int age = Integer.parseInt(ageField.getText());
            validateAge(age);
            String username = usernameField.getText();
            validateString(username);
            String password = passwordField.getText();
            validateString(password);
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
