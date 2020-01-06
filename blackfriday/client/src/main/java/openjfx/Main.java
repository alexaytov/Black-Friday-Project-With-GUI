package openjfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import store.Store;

import java.io.IOException;
import java.net.InetAddress;

import static commonMessages.ExceptionMessages.showWarningDialog;

/**
 * JavaFX Main
 */
public class Main extends Application {

    public static Store store;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
        primaryStage.setResizable(false);
        try {
            InetAddress ip = InetAddress.getLocalHost();
            int port = 4444;
            Main.store = new Store(ip, port);

        } catch (IOException e) {
            showWarningDialog("There was a problem connecting to the server please try again!");
            Platform.exit();
        }
    }

}