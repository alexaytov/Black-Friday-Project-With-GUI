package util;

import commonMessages.ConstantMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Operations {

    //TODO replace with loadWindow
    public static void changeWindows(Node node, String stageName, String fxmlFileName, Class currentClass, int width, int height) throws IOException {
        node.getScene().getWindow().hide();

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(currentClass.getResource(fxmlFileName));
        Scene scene = new Scene(root, width, height);
        stage.setTitle(stageName);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    //TODO replace with loadWindow
    public static void changeWindows(Parent root, String stageTitle) {
        Stage stage = new Stage();
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.show();
    }

    public static FXMLLoader loadWindow(Class clazz, String fxmlPath, String stageTitle, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(clazz.getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root, width, height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.show();

        return loader;

    }

    public static <T> boolean changeUserField(String commandToServer,
                                              T newVariable,
                                              String messageIfSuccessful,
                                              String messageIfUnsuccessful,
                                              ObjectInputStream ois,
                                              ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        oos.writeObject(commandToServer);
        oos.writeObject(newVariable);
        boolean changeSuccessful = (Boolean) ois.readObject();
        if (changeSuccessful) {
            ConstantMessages.confirmationPopUp(messageIfSuccessful);
            return true;
        } else {
            ConstantMessages.confirmationPopUp(messageIfUnsuccessful);
        }
        return false;
    }


}
