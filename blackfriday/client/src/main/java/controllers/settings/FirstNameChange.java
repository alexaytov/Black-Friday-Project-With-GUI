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
                    ConstantMessages.FIRST_NAME_CHANGE_UNSUCCESSFUL,
                    Main.store.getOis(),
                    Main.store.getOos());
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
