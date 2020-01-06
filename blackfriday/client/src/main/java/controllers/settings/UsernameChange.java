package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import user.interfaces.User;
import util.Operations;

import java.io.IOException;

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
            if (isUsernameChanged) {
                this.user.setUsername(newUsername);
            }
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        this.usernameField.getScene().getWindow().hide();
    }

    public void initUser(User user) {
        this.user = user;
    }
}
