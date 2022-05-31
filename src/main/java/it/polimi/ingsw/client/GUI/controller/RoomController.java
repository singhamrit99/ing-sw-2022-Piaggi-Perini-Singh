package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.RoomNotExistsException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Amrit
 * Class that shows all the game rooms availables to join
 */

public class RoomController extends InitialStage implements Controller {
    private Stage stage = new Stage();
    private Scene scene;

    private ArrayList<String> players = new ArrayList<>();

    @FXML
    private GridPane playersList;

    @FXML
    private Text roomTitle;

    @FXML
    private Button startGameButton;

    @FXML
    private Button leaveButton;

    public RoomController(GUI gui) {
        super(gui);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void initialize() {
        loadPlayersList();
       /* startGameButton.setOnAction((event) -> {
            CreateNewGameController createNewGameController = new CreateNewGameController(gui);
            createNewGameController.setRooms(rooms);
            Controller.startStage(ResourcesPath.CREATE_NEW_GAME_ACTION, createNewGameController);
            gui.stopAction();
            closeStage();
        });*/

        leaveButton.setOnAction((event) -> {
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
        stage.setResizable(true);
        this.scene = new Scene(root);
        roomTitle.setText(GUI.client.getRoom());
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

    public void setPlayersList(ArrayList<String> players) {
        this.players = players;
    }

    private void loadPlayersList() {
        try {
            players = GUI.client.getNicknamesInRoom(GUI.client.getRoom());
        } catch (RemoteException | RoomNotExistsException e) {
            e.printStackTrace();
        }
    }
}
