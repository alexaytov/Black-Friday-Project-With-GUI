package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import user.User;
import util.Operations;

import static util.Operations.showWarningDialog;
import static validator.Validator.requireNonBlank;


public class LastNameChange {

    private User user;

    @FXML
    private Label lastNameLabel;

    @FXML
    private JFXTextField lastNameField;


    @FXML
    void lastNameSubmit(ActionEvent event) {
        try {
            String newLastName = lastNameField.getText();
            requireNonBlank(newLastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            boolean isLastNameChanged = Operations.changeUserField("change last name",
                    lastNameField.getText(),
                    ConstantMessages.LAST_NAME_CHANGE_SUCCESSFUL,
                    ConstantMessages.LAST_NAME_CHANGE_UNSUCCESSFUL);
            // is last name was changed update user property
            if (isLastNameChanged) {
                this.user.setLastName(newLastName);
            }
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        // hide this window
        this.lastNameField.getScene().getWindow().hide();
    }

    public void initUser(User user) {
        this.user = user;
    }
}
