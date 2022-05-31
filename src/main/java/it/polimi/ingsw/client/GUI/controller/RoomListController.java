package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Amrit
 * Class that shows all the game rooms availables to join
 */

public class RoomListController extends InitialStage implements Controller {
    private Stage stage = new Stage();
    private Scene scene;

    private ArrayList<String> rooms = new ArrayList<>();

    @FXML
    private GridPane roomsList;

    @FXML
    private Button newGameButton;

    @FXML
    private Button exitButton;

    public RoomListController(GUI gui) {
        super(gui);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void initialize() {
        loadRoomsList();
       /* newGameButton.setOnAction((event) -> {
            CreateNewGameController createNewGameController = new CreateNewGameController(gui);
            createNewGameController.setRooms(rooms);
            Controller.startStage(ResourcesPath.CREATE_NEW_GAME_ACTION, createNewGameController);
            gui.stopAction();
            closeStage();
        });*/

        exitButton.setOnAction((event) -> {
            //GUILauncher.observer.quit();
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
        gui.startAction();
        stage.setTitle("Eryantis");
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
     * Sets the list of rooms available
     *
     * @param rooms available
     */
    public void setRoomsList(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    /**
     * Loads the list of rooms available
     */
    private void loadRoomsList() {
        System.out.println("loading rooms");
    }
}
