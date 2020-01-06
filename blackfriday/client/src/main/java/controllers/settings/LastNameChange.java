package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import util.Operations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import openjfx.Main;
import user.interfaces.User;

import java.io.IOException;

import static validator.Validator.validateString;


public class LastNameChange {

    private User user;

    @FXML
    private Label lastNameLabel;

    @FXML
    private JFXTextField lastNameField;


    @FXML
    void lastNameSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            String newLastName = lastNameField.getText();
            validateString(newLastName);
            boolean isLastNameChanged = Operations.changeUserField("change last name",
                    lastNameField.getText(),
                    ConstantMessages.LAST_NAME_CHANGE_SUCCESSFUL,
                    ConstantMessages.LAST_NAME_CHANGE_UNSUCCESSFUL,
                    Main.store.getOis(),
                    Main.store.getOos());
            if (isLastNameChanged) {
                this.user.setLastName(newLastName);
            }
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        this.lastNameField.getScene().getWindow().hide();
    }

    public void initUser(User user) {
        this.user = user;
    }
}