package application;

import commonMessages.ExceptionMessages;
import connection.Connection;
import connection.TCPConnection;
import exceptions.ConnectionException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import util.Operations;
import util.Windows;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import static util.Operations.showWarningDialog;


/**
 * JavaFX App
 */
public class App extends Application {

    public static Connection serverConnection;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Operations.loadWindow(Windows.LOGIN_PATH, Windows.LOGIN_WIDTH, Windows.LOGIN_HEIGHT);
        // use localhost ip for server
        InetAddress ip = InetAddress.getLocalHost();
        // get port number from environment variables
        try {
            final int port = Integer.parseInt(System.getenv("PORT"));
            Socket socket = new Socket(ip, port);
            serverConnection = new TCPConnection(socket);
        } catch (ConnectionException | ConnectException ex) {
            showWarningDialog(ExceptionMessages.PROBLEM_CONNECTION_TO_SERVER);
            Platform.exit();
        } catch (IllegalStateException ex) {
            showWarningDialog(ex.getMessage());
            Platform.exit();
        } catch (NumberFormatException ex) {
            showWarningDialog(ExceptionMessages.ENVIRONMENT_VARIABLES_PORT_NOT_SET);
            Platform.exit();
        }


    }

}