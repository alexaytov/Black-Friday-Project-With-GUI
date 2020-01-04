package application;

import com.jfoenix.controls.JFXClippedPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import store.Store;
import user.interfaces.User;

import java.io.IOException;
import java.net.InetAddress;

public class Main extends Application {

    public static Store store;
    public static User user;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/login.fxml"));
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
        primaryStage.setResizable(false);

        try {
            InetAddress ip = InetAddress.getLocalHost();
            int port = 4444;
            store = new Store(ip, port);

        } catch (IOException e) {
            System.out.println("There was a problem connection to the server");
        }

    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Main.user = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
