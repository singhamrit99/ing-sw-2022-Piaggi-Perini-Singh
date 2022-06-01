package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.RoomAlreadyExistsException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class CreateNewGameController extends InitialStage implements Controller {
    private Stage stage = new Stage();
    private Scene scene;

    private ArrayList<String> rooms = new ArrayList<>();

    @FXML
    private VBox container;
    @FXML
    private TextField roomName;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createNewGameButton;

    public CreateNewGameController(GUI gui) {
        super(gui);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void initialize() {
        initializeConfirmButton();
        initializeCancelButton();

        //ErrorManager.initializeElements(numOfPlayersField);
    }

    /**
     * Sets the actions to do when the action is canceled
     */
    private void initializeCancelButton() {
        cancelButton.setOnAction((event) -> {
            RoomListController roomListController = new RoomListController(gui);
            roomListController.setRoomsList(rooms);
            Controller.startStage(ResourcesPath.ROOM_LIST, roomListController);
            gui.stopAction();
            closeStage();
        });
    }

    /**
     * Sets the actions to do when the action is finished
     */
    private void initializeConfirmButton() {
        createNewGameButton.setOnAction((event) -> {
            if (!roomName.getText().equals("")){
                try {
                    GUI.client.createRoom(roomName.getText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (UserNotRegisteredException e) {
                    e.printStackTrace();
                } catch (RoomAlreadyExistsException e) {
                    e.printStackTrace();
                }
                stage.close();
            }
        });

        /*createNewGameButton.setOnAction((event) -> {
            int numOfPlayers = 0;

            if (numOfPlayersField.getText() != "") {
                try {
                    numOfPlayers = Integer.parseInt(numOfPlayersField.getText());
                } catch (NumberFormatException e) {
                    ErrorManager.setError(numOfPlayersField);
                }

                if (numOfPlayers >= 2 && numOfPlayers < 5) {
                    GUILauncher.observer.createNewRoom(numOfPlayers);
                    gui.stopAction();

                    WaitingWindowController waitingWindowController = new WaitingWindowController();
                    waitingWindowController.setMessage(WaitingMessages.WAITING_PLAYERS);
                    Controller.startStage(ResourcesPath.WAITING_WINDOW, waitingWindowController);
                    GUI.controller = waitingWindowController;
                    stage.close();
                } else {
                    ErrorManager.setError(numOfPlayersField);
                    manageError("Input incorrect! Number of player has to be between 2 and 4");
                }

            } else {
                ErrorManager.setError(numOfPlayersField);
                ErrorManager.initializeElements(numOfPlayersField);
            }
        });*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Parent root) throws IOException {
        gui.startAction();

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
     * Saves the rooms list in case the create new game action is cancelled
     *
     * @param rooms list of rooms available
     */
    public void setRooms(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    /**
     * {@inheritDoc}
     */
    /*@Override
    public void manageError(String message) {
        showErrorMessage(container, message);
    }*/

    /**
     * {@inheritDoc}
     */
    /*@Override
    public void close() {
        closeStage();
    }*/
}
