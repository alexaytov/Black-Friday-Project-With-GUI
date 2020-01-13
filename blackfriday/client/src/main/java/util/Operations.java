package util;

import application.App;
import commonMessages.ConstantMessages;
import commonMessages.ExceptionMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Operations {

    public static FXMLLoader loadWindow(String fxmlPath, int width, int height) {
        FXMLLoader loader = new FXMLLoader(Operations.class.getResource(fxmlPath));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(String.format(ExceptionMessages.PROBLEM_LOADING_FXML_FILE, fxmlPath));
        }
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
                                              String messageIfUnsuccessful) {
        App.serverConnection.write(commandToServer);
        App.serverConnection.write(newVariable);
        boolean changeSuccessful = App.serverConnection.read();
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
