package controllers.settings;

import com.jfoenix.controls.JFXTextField;
import common.ConstantMessages;
import common.ExceptionMessages;
import common.Operations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import user.interfaces.User;

import java.io.IOException;

import static validator.Validator.validateString;

public class FirstNameChange {

    User user;

    @FXML
    private Label firstNameLabel;

    @FXML
    private JFXTextField firstNameField;

    @FXML
    void firstNameSubmit(ActionEvent event) throws IOException, ClassNotFoundException {
        try{
            String newFirstName = firstNameField.getText();
            validateString(newFirstName);
            boolean change_first_name = Operations.changeUserField("change first name",
                    firstNameField.getText(),
                    ConstantMessages.FIRST_NAME_CHANGE_SUCCESSFUL,
                    ConstantMessages.FIRST_NAME_CHANGE_UNSUCCESSFUL);
            if(change_first_name){
                user.setFirstName(newFirstName);
            }
        }catch (IllegalArgumentException ex){
            ExceptionMessages.showWarningDialog(ExceptionMessages.STRING_NULL_OR_EMPTY);
        }

        this.firstNameField.getScene().getWindow().hide();
    }

    void initUser(User user) {
        this.user = user;
    }
}
