package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import passwordHasher.interfaces.Hasher;
import user.User;
import util.Operations;

import java.io.IOException;

import static validator.Validator.requireNonBlank;

public class PasswordChange {

    private User user;

    @FXML
    private JFXTextField passwordField;

    @FXML
    void passwordSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            String newPassword = passwordField.getText();
            requireNonBlank(newPassword, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
            newPassword = Hasher.hash(newPassword);
            boolean isPasswordChanged = Operations.changeUserField("change password",
                    newPassword,
                    ConstantMessages.PASSWORD_CHANGE_SUCCESSFUL,
                    ConstantMessages.PASSWORD_CHANGE_UNSUCCESSFUL);
            if (isPasswordChanged) {
                this.user.setPasswordHash(newPassword);
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
