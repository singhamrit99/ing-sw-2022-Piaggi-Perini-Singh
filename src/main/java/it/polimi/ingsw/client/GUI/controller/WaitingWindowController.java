package it.polimi.ingsw.client.GUI.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Amrit
 * Class that shows a window with a message when an action is being performed and the player has to waits
 */
public class WaitingWindowController implements Controller {
    private String message = "";

    private Stage stage = new Stage();
    private Scene scene;

    @FXML
    private Text waitingMessage;
    @FXML
    private Button exitButton;

    /**
     * {@inheritDoc}
     */
    @FXML
    public void initialize() {
        loadWaitingMessage();

        exitButton.setOnAction((event) -> {
            //TODO GUILauncher.client.leave or disconnect() something like that
            stage.close();
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Parent root) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);

        this.scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeStage() {
        stage.close();
    }

    /**
     * Sets the message to show
     *
     * @param message message to show
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Shows waiting message
     */
    private void loadWaitingMessage() {
        waitingMessage.setText(message);
    }
}
