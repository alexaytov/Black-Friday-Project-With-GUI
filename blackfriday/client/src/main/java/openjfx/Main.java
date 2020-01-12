package openjfx;

import connection.ServerClientConnection;
import connection.TCPConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import util.Operations;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static util.Operations.showWarningDialog;


/**
 * JavaFX Main
 */
public class Main extends Application {

    public static ServerClientConnection tcpServer;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Operations.loadWindow("/view/login.fxml", 600, 350);
        // use localhost ip for server
        InetAddress ip = InetAddress.getLocalHost();
        // get port number from environment variables
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