package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
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

import java.net.URL;
import java.util.ResourceBundle;

public class GUILauncher extends Application implements Initializable {

    public static Client client = GUI.client;

    public static final int MAIN_MENU_WIDTH = 740;
    public static final int MAIN_MENU_HEIGHT = 600;

    @FXML
    private StackPane mainMenuStage;
    @FXML
    private TextField nicknameField;
    @FXML
    private Button startBtn;

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
        initializeConfirmButton();
    }

    /**
     * Sets the actions to do when confirm button is clicked
     */
    private void initializeConfirmButton() {
        startBtn.setOnAction((event) -> {

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

