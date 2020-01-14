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
import static validator.Validator.requireNonNegative;

public class AgeChange {

    private User user;

    @FXML
    private JFXTextField ageField;

    @FXML
    void ageSubmit(ActionEvent event) {
        try {
            int newAge = Integer.parseInt(ageField.getText());
            requireNonNegative(newAge, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
            // change user field operation
            boolean isAgeChanged = Operations.changeUserField(CommandNames.CHANGE_USER_AGE,
                    ageField.getText(),
                    ConstantMessages.AGE_CHANGE_SUCCESSFUL,
                    ConstantMessages.AGE_CHANGE_UNSUCCESSFUL);
            // if age was changed update user property
            if (isAgeChanged) {
                this.user.setAge(newAge);
            }
        } catch (NumberFormatException ex) {
            showWarningDialog(ExceptionMessages.ENTER_NUMBER);
        } catch (IllegalArgumentException ex) {
            showWarningDialog(ExceptionMessages.INVALID_NUMBER);
        }
        // hide window
        this.ageField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
