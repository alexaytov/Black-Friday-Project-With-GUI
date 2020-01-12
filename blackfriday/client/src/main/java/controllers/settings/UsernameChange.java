package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import user.User;
import util.Operations;

import java.io.IOException;

import static util.Operations.showWarningDialog;
import static validator.Validator.requireNonBlank;

public class UsernameChange {

    private User user;

    @FXML
    private JFXTextField usernameField;

    @FXML
    void usernameSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        String newUsername = usernameField.getText();
        try {
            requireNonBlank(newUsername, ExceptionMessages.NAME_NULL_OR_EMPTY);
            boolean isUsernameChanged = Operations.changeUserField("change username",
                    usernameField.getText(),
                    ConstantMessages.USERNAME_CHANGE_SUCCESSFUL,
                    ConstantMessages.USERNAME_CHANGE_UNSUCCESSFUL);
            // if username was changed update user property
            if (isUsernameChanged) {
                this.user.setUsername(newUsername);
            }
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        this.usernameField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
