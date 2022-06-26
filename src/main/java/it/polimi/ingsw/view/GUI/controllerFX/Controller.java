package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.view.GUI.GUILauncher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Amrit
 */
public interface Controller {
    /**
     * Method to be overridden in different controller types.
     */
    @FXML
    void initialize();

    /**
     * Method that loads a scene and related controller.
     * @param sceneName The name of the scene to load
     * @param controller Its relative controller.
     */
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

    /**
     * Method used to show errors
     * @param content The error message itself
     */
    static void showErrorDialogBox(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.DECORATED);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Method used to load a scene.
     * @param loader The loader that is used to load a scene.
     * @throws IOException Thrown on load failure.
     */
    static void loadScene(FXMLLoader loader) throws IOException {
        Scene scene;
        scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle(StringNames.TITLE);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
