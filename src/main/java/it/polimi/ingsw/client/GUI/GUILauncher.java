package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUILauncher extends Application implements Initializable {

    public static Client client = GUI.client;
    public static WaitingWindowController waitingWindowController = null;

    public static final int MAIN_MENU_WIDTH = 740;
    public static final int MAIN_MENU_HEIGHT = 600;

    @FXML
    private StackPane mainMenuStage;
    @FXML
    private TextField nicknameField;
    @FXML
    private Button startButton;

    @Override
    public void start(Stage menuStage) throws Exception {
        menuStage.setTitle("Eriantys");
        menuStage.setResizable(false);
        menuStage.setWidth(MAIN_MENU_WIDTH);
        menuStage.setHeight(MAIN_MENU_HEIGHT);

        menuStage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });

        String path = ResourcesPath.FXML_FILE_PATH + ResourcesPath.MAIN_MENU + ResourcesPath.FILE_EXTENSION;
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);

        menuStage.setScene(scene);
        menuStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ErrorManager.initializeElements(nicknameField);
        startButtonEvent();
    }

    /**
     * Sets the actions to do when start button is clicked
     */
    private void startButtonEvent() {
        startButton.setOnAction((event) -> {

            if (controlNickname()) {
                final String nickname = nicknameField.getText();

                try {
                    client.registerClient(nickname);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (GUILauncher.waitingWindowController == null) {
                    waitingWindowController = new WaitingWindowController();
                    waitingWindowController.setMessage(WaitingMessages.CONNECTING);
                    GUI.controller = waitingWindowController;

                    String filePath = ResourcesPath.FXML_FILE_PATH + ResourcesPath.WAITING_WINDOW + ResourcesPath.FILE_EXTENSION;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
                    loader.setController(waitingWindowController);

                    try {
                        waitingWindowController.start(loader.load());
                        Stage stage = (Stage) startButton.getScene().getWindow();
                        stage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Controls choices before sent them to server
     *
     * @return true if choices are correct
     */
    private boolean controlNickname() {
        final String nickname = nicknameField.getText();

        if (nickname.length() == 0) {
            //ErrorManager.setError(nicknameField);
            return false;
        }

        return true;
    }
}