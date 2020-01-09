package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import user.User;
import util.Operations;

import java.io.IOException;

import static validator.Validator.requireNonNegative;

public class AgeChange {

    private User user;

    @FXML
    private JFXTextField ageField;

    @FXML
    void ageSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            int newAge = Integer.parseInt(ageField.getText());
            requireNonNegative(newAge, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
            boolean isAgeChanged = Operations.changeUserField("change age",
                    ageField.getText(),
                    ConstantMessages.AGE_CHANGE_SUCCESSFUL,
                    ConstantMessages.AGE_CHANGE_UNSUCCESSFUL);
            if (isAgeChanged) {
                this.user.setAge(newAge);
            }
        } catch (NumberFormatException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.ENTER_NUMBER);
        } catch (IllegalArgumentException ex) {
            ExceptionMessages.showWarningDialog(ExceptionMessages.INVALID_NUMBER);
        }
        this.ageField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
