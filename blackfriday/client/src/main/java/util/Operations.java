package util;

import commonMessages.ConstantMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import application.App;

import java.io.IOException;

public class Operations {

    public static FXMLLoader loadWindow(String fxmlPath, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(Operations.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(ConstantMessages.STAGE_TITLE);
        stage.show();
        return loader;
    }

    public static <T> boolean changeUserField(String commandToServer,
                                              T newVariable,
                                              String messageIfSuccessful,
                                              String messageIfUnsuccessful) throws IOException, ClassNotFoundException {
        App.tcpServer.write(commandToServer);
        App.tcpServer.write(newVariable);
        boolean changeSuccessful = App.tcpServer.read();
        if (changeSuccessful) {
            confirmationPopUp(messageIfSuccessful);
            return true;
        } else {
            confirmationPopUp(messageIfUnsuccessful);
        }
        return false;
    }

    public static void confirmationPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showWarningDialog(String warningMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(warningMessage);
        alert.showAndWait();
    }


}
