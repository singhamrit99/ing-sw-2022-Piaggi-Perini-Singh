package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.StringNames;
import it.polimi.ingsw.exceptions.RoomAlreadyExistsException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;

/**
 * @author Amrit
 */
public class NewRoomController extends InitialStage implements Controller {
    @FXML
    private VBox container;
    @FXML
    private TextField roomName;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createNewGameButton;

    public NewRoomController(GUI gui) {
        super(gui);
    }

    @FXML
    public void initialize() {
        initializeConfirmButton();
        initializeCancelButton();
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((event) -> {
            GUI.client.view = StringNames.LOBBY;
            GUI.client.roomListShow();
        });
    }

    private void initializeConfirmButton() {
        createNewGameButton.setOnAction((event) -> {
            if (!roomName.getText().equals("")) {
                try {
                    GUI.client.view = StringNames.ROOM;
                    GUI.client.createRoom(roomName.getText());
                } catch (RemoteException e) {
                    Utility.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                } catch (UserNotRegisteredException e) {
                    Utility.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                } catch (RoomAlreadyExistsException e) {
                    Utility.showErrorDialogBox(StringNames.ROOM_EXISTS);
                }
            }
        });
    }
}
