package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import passwordHasher.BCryptHasher;
import user.User;
import util.Operations;

import static util.Operations.showWarningDialog;
import static validator.Validator.requireNonBlank;

public class PasswordChange {

    private User user;

    @FXML
    private JFXTextField passwordField;

    @FXML
    void passwordSubmit(ActionEvent event) {
        try {
            String newPassword = passwordField.getText();
            requireNonBlank(newPassword, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
            newPassword = BCryptHasher.hash(newPassword);
            boolean isPasswordChanged = Operations.changeUserField("change password",
                    newPassword,
                    ConstantMessages.PASSWORD_CHANGE_SUCCESSFUL,
                    ConstantMessages.PASSWORD_CHANGE_UNSUCCESSFUL);
            // if password was changed update user property
            if (isPasswordChanged) {
                this.user.setPasswordHash(newPassword);
            }
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        // hide this window
        this.passwordField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
