package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.GUI.controllerFX.Controller;
import it.polimi.ingsw.view.GUI.controllerFX.ResourcesPath;
import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.UserAlreadyExistsException;
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

import java.io.FileInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * @author Amrit
 */
public class GUILauncher extends Application implements Initializable {
    public static Client client = GUI.client;
    public static Stage mainWindow;

    public static final int MAIN_MENU_WIDTH = 1280;
    public static final int MAIN_MENU_HEIGHT = 720;

    @FXML
    private StackPane mainMenuStage;
    @FXML
    private TextField nicknameField;
    @FXML
    private Button startButton;

    @FXML
    private ImageView title;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(StringNames.TITLE);
        stage.setResizable(true);
        stage.setMinHeight(1080);
        stage.setMinWidth(1920);
        stage.setWidth(MAIN_MENU_WIDTH);
        stage.setHeight(MAIN_MENU_HEIGHT);
        mainWindow = stage;

        javafx.scene.image.Image icon = new Image(new FileInputStream("src/main/resources/img/professors/teacher_blue.png"));
        mainWindow.getIcons().add(icon);

        stage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });

        String path = ResourcesPath.FXML_FILE_PATH + ResourcesPath.LAUNCHER + ResourcesPath.FILE_EXTENSION;
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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
        floatingTitle.setByY(25);
        floatingTitle.setAutoReverse(true);
        floatingTitle.setInterpolator(Interpolator.EASE_BOTH);
        floatingTitle.play();
    }

    private void startButtonEvent() {
        startButton.setOnAction((event) -> {
            if (controlNickname()) {
                final String nickname = nicknameField.getText();

                try {
                    GUI.client.view = StringNames.LOBBY;
                    client.registerClient(nickname);
                } catch (UserAlreadyExistsException e) {
                    Controller.showErrorDialogBox(StringNames.NICKNAME_ALREADY_EXISTS);
                } catch (RemoteException e) {
                    Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                }
            }
        });
    }

    private boolean controlNickname() {
        final String nickname = nicknameField.getText();

        if (nickname.length() == 0) {
            Controller.showErrorDialogBox(StringNames.NICKNAME_FIELD_NULL);
            return false;
        }
        return true;
    }
}