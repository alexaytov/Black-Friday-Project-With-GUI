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
                System.out.println("wrong password");
                ExceptionMessages.showWarningDialog("Invalid password");
            } catch (NotFoundException e) {
                System.out.println("not found exception");
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
        StringBuilder sb = new StringBuilder();
        Main.tcpServer.write("login");
        sb.append(username).append(" ").append(password);
        Main.tcpServer.write(sb.toString());
        User user = Main.tcpServer.read();
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
