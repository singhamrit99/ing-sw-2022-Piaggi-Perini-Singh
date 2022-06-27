package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Amrit
 */
public class LobbyController extends InitialStage implements Controller {
    protected static AtomicBoolean opened = new AtomicBoolean(false);
    private ArrayList<String> rooms;

    @FXML
    StackPane titlePane;
    @FXML
    private GridPane roomsList;
    @FXML
    private Button createRoomButton, exitButton;

    /**
     * Constructor for Lobby controller scene.
     * @param gui The GUI this controller is bound to.
     */
    public LobbyController(GUI gui) {
        super(gui);
        new ArrayList<>();
        opened.set(false);
    }

    /**
     * Setter method to tell whether the view is open or not
     * @param b boolean value
     */
    public static void setOpened(boolean b) {
        opened.set(b);
    }


    @FXML
    ScrollPane scrollLobby;

    /**
     * Initializer for lobby controller scene
     */
    @FXML
    public void initialize() {
        opened.set(true);

        roomsList.setOnScroll((event) -> {
            final double y = event.getDeltaY();
            if(y != 0){ //prevent from panning before values are initialized
                roomsList.setTranslateY(y);
            }
        });

        loadRoomsList();

        createRoomButton.setOnAction((event) -> {
            GUI.client.view = StringNames.CREATE_NEW_ROOM;
            opened.set(false);
            NewRoomController newRoomController = new NewRoomController(gui);
            Controller.load(ResourcesPath.NEW_ROOM, newRoomController);
        });

        exitButton.setOnAction((event) -> {
            opened.set(false);
            GUI.client.view = "";
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Setter for rooms parameter
      * @param rooms the rooms available on the server.
     */
    public void setRoomsList(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    /**
     * Loads the rooms list with graphical output.
     */
    private void loadRoomsList() {
        for (int i = 0; i < rooms.size(); i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(50);
            row.setMinHeight(50);
            roomsList.getRowConstraints().add(row);

            Text roomName = new Text();
            roomName.setText(rooms.get(i));

            Button joinRoom = new Button();
            joinRoom.setText(StringNames.JOIN);
            joinRoom.setStyle("-fx-border-color: #2c43ff;\n" +
                    "    -fx-background-color: #848abd; \n -fx-border-width: 1;\n" +
                    "    -fx-border-radius: 30;\n" +
                    "    -fx-background-radius: 30;");

            joinRoom.setOnAction((event) -> {
                try {
                    GUI.client.view = StringNames.ROOM;
                    opened.set(false);
                    GUI.client.requestRoomJoin(roomName.getText());
                } catch (RemoteException e) {
                    Controller.showErrorDialogBox(StringNames.REMOTE);
                } catch (RoomNotExistsException e) {
                    Controller.showErrorDialogBox(StringNames.ROOM_NOT_EXISTS);
                } catch (UserNotRegisteredException e) {
                    Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                } catch (RoomFullException | RoomInGameException e) {
                    Controller.showErrorDialogBox(e.getMessage());
                } catch (UserInRoomException e) {
                    Controller.showErrorDialogBox(StringNames.USER_IN_ROOM);
                }
            });

            roomsList.addRow(i + 1, roomName, joinRoom);
            GridPane.setHalignment(roomName, HPos.CENTER);
            GridPane.setHalignment(joinRoom, HPos.CENTER);
        }
    }

    /**
     * Getter method for the opened status.
     * @return boolean value depending on the state.
     */
    public static boolean isOpened() {
        return opened.get();
    }

    /**
     * Updates the rooms list because of room deletion or creation.
     * @param rooms New rooms name array.
     */
    public void update(ArrayList<String> rooms) {
        roomsList.getChildren().remove(3, roomsList.getChildren().size());
        setRoomsList(rooms);
        loadRoomsList();
    }
}
