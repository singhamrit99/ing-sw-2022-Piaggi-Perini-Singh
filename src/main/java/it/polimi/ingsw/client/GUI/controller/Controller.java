package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUILauncher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Amrit
 */
public interface Controller {

    @FXML
    void initialize();

    static void load(String sceneName, Controller controller) {
        String filePath = ResourcesPath.FXML_FILE_PATH + sceneName + ResourcesPath.FILE_EXTENSION;
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
        loader.setController(controller);

        Stage mainWindow = GUILauncher.mainWindow;
        try {
            Scene sceneRooms = new Scene(loader.load());
            mainWindow.setScene(sceneRooms);
            mainWindow.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
