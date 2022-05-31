package it.polimi.ingsw.client.GUI.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * @author Amrit
 * Interface that defines methods to control JavaFX window interactions
 */
public interface Controller {
    /**
     * Open a new window on the same thread
     * @param root content of FXML file loaded using FXMLoader
     * @throws IOException
     */
    void start(Parent root) throws IOException;

    /**
     * Load and set up the window's elements
     */
    @FXML
    void initialize();

    /**
     * Close the current window
     */
    void closeStage();

    /**
     * Load the content of FXML file and set up its controller
     * @param fxmlName name of FXML file
     * @param controller object that controls the window interactions
     * @return the FXML file content
     * @throws IOException
     */
    static Parent loadStage(String fxmlName, Controller controller) throws IOException {
        String filePath = ResourcesPath.FXML_FILE_PATH + fxmlName + ResourcesPath.FILE_EXTENSION;
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource(filePath));
        loader.setController(controller);

        return loader.load();
    }

    /**
     * Load the content of FXML file, set up its controller and open a new window
     * @param fxmlName name of FXML file
     * @param controller object that controls the window interactions
     */
    static void startStage(String fxmlName, Controller controller) {
        try {
            Parent root = loadStage(fxmlName, controller);

            controller.start(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
