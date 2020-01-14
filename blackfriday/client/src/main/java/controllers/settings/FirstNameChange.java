package controllers.settings;


import com.jfoenix.controls.JFXTextField;
import commonMessages.CommandNames;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import user.User;
import util.Operations;

import static util.Operations.showWarningDialog;
import static validator.Validator.requireNonBlank;

public class FirstNameChange {

    private User user;

    @FXML
    private JFXTextField firstNameField;

    @FXML
    void firstNameSubmit(ActionEvent event) {
        try {
            String newFirstName = firstNameField.getText();
            requireNonBlank(newFirstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
            boolean change_first_name = Operations.changeUserField(CommandNames.CHANGE_USER_FIRST_NAME,
                    firstNameField.getText(),
                    ConstantMessages.FIRST_NAME_CHANGE_SUCCESSFUL,
                    ConstantMessages.FIRST_NAME_CHANGE_UNSUCCESSFUL);
            // is first name was changed update user property
            if (change_first_name) {
                user.setFirstName(newFirstName);
            }
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }
        // hide window
        this.firstNameField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
