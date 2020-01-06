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

public class UsernameChange {

    private User user;

    @FXML
    private JFXTextField usernameField;

    @FXML
    void usernameSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        String newUsername = usernameField.getText();
        try{
            validateString(newUsername);
            boolean isUsernameChanged = Operations.changeUserField("change username",
                    usernameField.getText(),
                    ConstantMessages.USERNAME_CHANGE_SUCCESSFUL,
                    ConstantMessages.USERNAME_CHANGE_UNSUCCESSFUL,
                    Main.store.getOis(),
                    Main.store.getOos());
            if(isUsernameChanged){
                this.user.setUsername(newUsername);
            }
        }catch (IllegalArgumentException ex){
            ExceptionMessages.showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        this.usernameField.getScene().getWindow().hide();
    }

    public void initUser(User user){
        this.user = user;
    }
}
