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
import javafx.fxml.Initializable;
import openjfx.Main;
import passwordHasher.interfaces.Hasher;
import user.Permission;
import user.User;
import util.Operations;
import validator.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogIn implements Initializable {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton logInButton;

    @FXML
    private JFXButton signUpButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @FXML
    void logIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Validator.requireNonBlank(username, ExceptionMessages.NAME_NULL_OR_EMPTY);
            Validator.requireNonBlank(password, ExceptionMessages.NAME_NULL_OR_EMPTY);
            User user;
            //try to login as client;
            try {
                user = login(username, password);

                // logged in as client
                this.usernameField.getScene().getWindow().hide();
                if (user.getPermission().equals(Permission.CLIENT)) {
                    FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/client/clientLoggedIn.fxml", "asd", 650, 800);
                    ClientLoggedIn controller = loader.getController();
                    controller.initUser(user);
                } else {
                    FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffLoggedIn.fxml", "Staff", 600, 600);
                    StaffLoggedIn controller = loader.getController();
                    controller.initUser(user);
                }

            } catch (WrongPasswordException e) {
                ExceptionMessages.showWarningDialog("Invalid password");
            } catch (NotFoundException e) {
                ExceptionMessages.showWarningDialog("Account not found");
            } catch (IOException | ClassNotFoundException e) {
                //TODO throw exceptions
                e.printStackTrace();
            }
        } catch (IllegalArgumentException ex) {
            usernameField.clear();
            passwordField.clear();
            ExceptionMessages.showWarningDialog("You entered blank fields");
        }
    }

    @FXML
    void signUp(ActionEvent event) throws IOException {
        Operations.changeWindows(logInButton, "Sign Up", "/view/signUp.fxml", this.getClass(), 600, 550);

    }


    private User login(String username, String password) throws WrongPasswordException, NotFoundException, IOException, ClassNotFoundException {
        Main.tcpServer.write("login");

        // get user password salt
        Main.tcpServer.write(username);
        String salt = Main.tcpServer.read();
        User user = null;
        if(salt != null){
            // hash password with same salt
            String hashedPassword = Hasher.hash(password, salt);

            // send password
            Main.tcpServer.write(hashedPassword);

            // get user, if null read exception
            user = Main.tcpServer.read();

        }

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
