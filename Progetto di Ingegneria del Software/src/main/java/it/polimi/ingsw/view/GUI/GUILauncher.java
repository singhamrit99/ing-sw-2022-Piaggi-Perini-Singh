package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.NameFieldException;
import it.polimi.ingsw.exceptions.UserAlreadyExistsException;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.GUI.controllerFX.Controller;
import it.polimi.ingsw.view.GUI.controllerFX.ResourcesPath;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * @author Amrit
 */
public class GUILauncher extends Application implements Initializable {
    public static Client client = GUI.client;
    public static Stage mainWindow;

    @FXML
    private StackPane mainMenuStage;
    @FXML
    private TextField nicknameField;
    @FXML
    private Button startButton;
    @FXML
    private ImageView title;

    /**
     * Starts the GUI on the starting stage.
     *
     * @param stage The stage to set to
     * @throws Exception used for Stage exceptions.
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(StringNames.TITLE);
        stage.setResizable(true);
        stage.setMaximized(true);

        mainWindow = stage;

        Image icon = new Image("/img/professors/teacher_blue.png");
        mainWindow.getIcons().add(icon);

        String path = ResourcesPath.FXML_FILE_PATH + ResourcesPath.LAUNCHER + ResourcesPath.FILE_EXTENSION;
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Initializes launcher in preparation for start button event.
     *
     * @param url            The url of the resource
     * @param resourceBundle the graphical resources of the game
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nicknameField.textProperty().addListener((event) -> {
            int maxLength = 15;
            if (nicknameField.getText().length() > maxLength) {
                String set = nicknameField.getText().substring(0, maxLength);
                nicknameField.setText(set);
            }
        });
        startButtonEvent();

        TranslateTransition floatingTitle = new TranslateTransition();
        floatingTitle.setNode(title);
        floatingTitle.setDuration(Duration.millis(9000));
        floatingTitle.setCycleCount(TranslateTransition.INDEFINITE);
        floatingTitle.setByY(10);
        floatingTitle.setAutoReverse(true);
        floatingTitle.setInterpolator(Interpolator.EASE_BOTH);
        floatingTitle.play();
    }

    /**
     * Method that handles the Start Button being pressed.
     */
    private void startButtonEvent() {
        startButton.setOnAction((event) -> {
            final String nickname = nicknameField.getText();
            GUI.client.view = StringNames.LOBBY;
            try {
                client.registerClient(nickname);
            } catch (NotBoundException | RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (UserAlreadyExistsException e) {
                Controller.showErrorDialogBox(StringNames.USER_ALREADY_EXISTS);
            } catch (NameFieldException e) {
                Controller.showErrorDialogBox(StringNames.NAME_FIELD_NULL);
            }
        });
    }


}