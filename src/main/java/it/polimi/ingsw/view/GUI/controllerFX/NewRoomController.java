package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.exceptions.NameFieldException;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.StringNames;
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
    private Button cancelButton, createNewGameButton;

    /**
     * Method used to bind this scene to a GUI
     *
     * @param gui the GUI to bind to
     */
    public NewRoomController(GUI gui) {
        super(gui);
    }

    /**
     * Initializes the New Room controller scene.
     */
    @FXML
    public void initialize() {
        roomName.textProperty().addListener((event) -> {
            int maxLength = 15;
            if (roomName.getText().length() > maxLength) {
                String set = roomName.getText().substring(0, maxLength);
                roomName.setText(set);
            }
        });
        initializeConfirmButton();
        initializeCancelButton();
    }

    /**
     * Initializes the "cancel" button
     */
    private void initializeCancelButton() {
        cancelButton.setOnAction((event) -> {
            GUI.client.view = StringNames.LOBBY;
            try {
                GUI.client.roomListShow();
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            }
        });
    }

    /**
     * Initializes the "confirm" button
     */
    private void initializeConfirmButton() {
        createNewGameButton.setOnAction((event) -> {
            try {
                GUI.client.view = StringNames.ROOM;
                GUI.client.createRoom(roomName.getText());
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.REMOTE);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            } catch (RoomAlreadyExistsException e) {
                Controller.showErrorDialogBox(StringNames.ROOM_ALREADY_EXISTS);
            } catch (NameFieldException e) {
                Controller.showErrorDialogBox(StringNames.NAME_FIELD_NULL);
            }
        });
    }
}
