package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import util.Operations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import openjfx.Main;
import user.interfaces.User;

import java.io.IOException;

import static validator.Validator.validateString;

public class PasswordChange {

    private User user;

    @FXML
    private JFXTextField passwordField;

    @FXML
    void passwordSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            String newPassword = passwordField.getText();
            validateString(newPassword);
            boolean isPasswordChanged = Operations.changeUserField("change password",
                    passwordField.getText(),
                    ConstantMessages.PASSWORD_CHANGE_SUCCESSFUL,
                    ConstantMessages.PASSWORD_CHANGE_UNSUCCESSFUL, Main.store.getOis(), Main.store.getOos());
            if (isPasswordChanged) {
                this.user.setPassword(newPassword);
            }
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        this.passwordField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
