package openjfx;

import connection.Connection;
import connection.TCPConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static commonMessages.ExceptionMessages.showWarningDialog;

/**
 * JavaFX Main
 */
public class Main extends Application {

    public static Connection tcpServer;


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

        InetAddress ip = InetAddress.getLocalHost();
        final int port = Integer.parseInt(System.getenv("PORT"));
        try {
            Socket socket = new Socket(ip, port);
            tcpServer = new TCPConnection(socket);
        } catch (IOException ex) {
            showWarningDialog("There was a problem connecting to the server please try again!");
            Platform.exit();
        }


    }

}