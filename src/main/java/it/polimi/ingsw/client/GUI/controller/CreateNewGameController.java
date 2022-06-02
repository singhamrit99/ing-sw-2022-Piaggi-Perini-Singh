package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.RoomAlreadyExistsException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CreateNewGameController extends InitialStage implements Controller {
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

    @FXML
    public void initialize() {
        gui.startAction();
        initializeConfirmButton();
        initializeCancelButton();

        //ErrorManager.initializeElements(numOfPlayersField);
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((event) -> {
            GUI.view = "lobby";
            RoomListController roomListController = new RoomListController(gui);
            roomListController.setRoomsList(rooms);
            Controller.load(ResourcesPath.ROOM_LIST, roomListController);

            /*String filePath = ResourcesPath.FXML_FILE_PATH + ResourcesPath.ROOM_LIST + ResourcesPath.FILE_EXTENSION;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            loader.setController(roomListController);

            Stage mainWindow = GUILauncher.mainWindow;
            try {
                Scene sceneRooms = new Scene(loader.load());
                mainWindow.setScene(sceneRooms);
                mainWindow.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        });
    }

    private void initializeConfirmButton() {
        createNewGameButton.setOnAction((event) -> {
            if (!roomName.getText().equals("")) {
                try {
                    GUI.view = "room";
                    GUI.client.createRoom(roomName.getText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (UserNotRegisteredException e) {
                    e.printStackTrace();
                } catch (RoomAlreadyExistsException e) {
                    e.printStackTrace();
                }
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

    public void setRooms(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    /*@Override
    public void manageError(String message) {
        showErrorMessage(container, message);
    }*/

    /*@Override
    public void close() {
        closeStage();
    }*/
}
