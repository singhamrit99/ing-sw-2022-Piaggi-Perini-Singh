package it.polimi.ingsw.view.GUI.controller;

import it.polimi.ingsw.view.GUI.GUILauncher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    static void showErrorDialogBox(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
