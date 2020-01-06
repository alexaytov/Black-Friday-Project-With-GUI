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
import user.interfaces.User;
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
        // show progress animation
//        progress.setVisible(true);

        // get username and password
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            Validator.validateString(username);
            Validator.validateString(password);
            User user;
            //try to login as client;
            try {
                user = login("client", username, password);

                // logged in as client
                this.usernameField.getScene().getWindow().hide();
                FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/client/clientLoggedIn.fxml", "Welcome", 650, 800);
                ClientLoggedIn controller = loader.getController();
                controller.initUser(user);

            } catch (WrongPasswordException e) {
                System.out.println("wrong password");
                ExceptionMessages.showWarningDialog("Invalid password");
            } catch (NotFoundException e) {
                try {
                    user = login("controllers/staff", username, password);
                    // logged in as staff
//                    util.Operations.changeWindows(logInButton, "StaffLoggedIn", "FXML/staffLoggedIn.fxml", this.getClass(), 600, 600);
                    this.usernameField.getScene().getWindow().hide();
                    FXMLLoader loader = Operations.loadWindow(this.getClass(), "/view/staff/staffLoggedIn.fxml", "Staff", 600, 600);
                    StaffLoggedIn controller = loader.getController();
                    controller.initUser(user);

                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (NotFoundException ex) {
                    System.out.println("not found exception");
                    ExceptionMessages.showWarningDialog("Account not found");
                } catch (WrongPasswordException ex) {
                    System.out.println("wrong password");
                    ExceptionMessages.showWarningDialog("Wrong password");
                }
            } catch (IOException | ClassNotFoundException e) {
                //TODO throw exceptions
                e.printStackTrace();
            }
        } catch (IllegalArgumentException ex) {
            usernameField.clear();
            passwordField.clear();
            ExceptionMessages.showWarningDialog("You entered blank fields");
        }

//        progress.setVisible(false);

    }


    @FXML
    void signUp(ActionEvent event) throws IOException {
        Operations.changeWindows(logInButton, "Sign Up", "/view/signUp.fxml", this.getClass(), 600, 550);

    }


    private User login(String type, String username, String password) throws WrongPasswordException, NotFoundException, IOException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        Main.store.getOos().writeObject("login");
        sb.append(type.toLowerCase()).append(" ").append(username).append(" ").append(password);
        Main.store.getOos().writeObject(sb.toString());
        User user = (User) Main.store.getOis().readObject();
        if (user == null) {
            String exceptionType = Main.store.getOis().readObject().toString();
            if (exceptionType.equals("WrongPasswordException")) {
                throw new WrongPasswordException(ExceptionMessages.WRONG_PASSWORD);
            } else if (exceptionType.equals("NotFoundException")) {
                throw new NotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, username));
            }
        }
        return user;
    }

}
