package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import commonMessages.ExceptionMessages;
import controllers.client.ClientLoggedIn;
import controllers.staff.StaffLoggedIn;
import exceptions.NotFoundException;
import exceptions.WrongPasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import application.Main;
import passwordHasher.BCryptHasher;
import user.Permission;
import user.User;
import util.Operations;
import validator.Validator;

import java.io.IOException;

import static util.Operations.showWarningDialog;

public class LogIn {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton logInButton;

    @FXML
    void logIn(ActionEvent event) throws IOException, ClassNotFoundException {
        // get input data from text fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            // validate data
            Validator.requireNonBlank(username, ExceptionMessages.NAME_NULL_OR_EMPTY);
            Validator.requireNonBlank(password, ExceptionMessages.NAME_NULL_OR_EMPTY);
            User user;
            //try to login
            try {
                user = login(username, password);
                this.usernameField.getScene().getWindow().hide();
                if (user.getPermission().equals(Permission.CLIENT)) {
                    // logged in as client
                    FXMLLoader loader = Operations.loadWindow("/view/client/clientLoggedIn.fxml", 650, 800);
                    ClientLoggedIn controller = loader.getController();
                    controller.initUser(user);
                } else {
                    // logged in as admin
                    FXMLLoader loader = Operations.loadWindow("/view/staff/staffLoggedIn.fxml", 600, 600);
                    StaffLoggedIn controller = loader.getController();
                    controller.initUser(user);
                }
            } catch (WrongPasswordException e) {
                showWarningDialog(ExceptionMessages.WRONG_PASSWORD);
            } catch (NotFoundException e) {
                showWarningDialog(ExceptionMessages.ACCOUNT_NOT_FOUNT);
            }
        } catch (IllegalArgumentException ex) {
            usernameField.clear();
            passwordField.clear();
            showWarningDialog(ExceptionMessages.FILL_IN_ALL_FIELDS);
        }
    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        // go to sign up window
        this.logInButton.getScene().getWindow().hide();
        Operations.loadWindow("/view/signUp.fxml", 600, 550);
    }

    private User login(String username, String password) throws WrongPasswordException, NotFoundException, IOException, ClassNotFoundException {
        Main.tcpServer.write("login");
        // get user password salt
        Main.tcpServer.write(username);
        String salt = Main.tcpServer.read();
        User user = null;
        if (salt != null) {
            // hash password with same salt when password originally hashed
            String hashedPassword = BCryptHasher.hash(password, salt);
            // send password
            Main.tcpServer.write(hashedPassword);
            // get user
            user = Main.tcpServer.read();
        }
        // if user null, read exception
        if (user == null) {
            String exceptionType = Main.tcpServer.read().toString();
            if (exceptionType.equals("WrongPasswordException")) {
                throw new WrongPasswordException(ExceptionMessages.WRONG_PASSWORD);
            } else if (exceptionType.equals("NotFoundException")) {
                throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, username));
            }
        }
        return user;
    }
}
