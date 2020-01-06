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

import static validator.Validator.validateAge;

public class AgeChange {

    private User user;

    @FXML
    private JFXTextField ageField;

    @FXML
    void ageSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        try {
            int newAge = Integer.parseInt(ageField.getText());
            validateAge(newAge);
            boolean isAgeChanged = Operations.changeUserField("change age",
                    ageField.getText(),
                    ConstantMessages.AGE_CHANGE_SUCCESSFUL,
                    ConstantMessages.AGE_CHANGE_UNSUCCESSFUL,
                    Main.store.getOis(),
                    Main.store.getOos()
            );
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