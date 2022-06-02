package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.StringNames;
import it.polimi.ingsw.exceptions.RoomNotExistsException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
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
    private GridPane roomsList;
    @FXML
    private Button createRoomButton;
    @FXML
    private Button exitButton;

    public LobbyController(GUI gui) {
        super(gui);
        new ArrayList<>();
    }

    @FXML
    public void initialize() {
        opened.set(true);
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

    public void setRoomsList(ArrayList<String> rooms) {
        this.rooms = rooms;
    }

    private void loadRoomsList() {
        for (int i = 0; i < rooms.size(); i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(40);
            roomsList.getRowConstraints().add(row);

            Text roomName = new Text();
            roomName.setText(rooms.get(i));

            Button joinRoom = new Button();
            joinRoom.setText(StringNames.JOIN);

            joinRoom.setOnAction((event) -> {
                try {
                    GUI.client.view = StringNames.ROOM;
                    opened.set(false);
                    GUI.client.requestRoomJoin(roomName.getText());
                } catch (RemoteException e) {
                    Utility.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                } catch (RoomNotExistsException e) {
                    Utility.showErrorDialogBox(StringNames.NO_SUCH_ROOM);
                } catch (UserNotRegisteredException e) {
                    Utility.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                }
            });
            roomsList.addRow(i + 1, roomName, joinRoom);

            GridPane.setHalignment(roomName, HPos.CENTER);
            GridPane.setHalignment(joinRoom, HPos.CENTER);
        }
    }

    public static boolean isOpened() {
        return opened.get();
    }

    public void update(ArrayList<String> rooms) {
        roomsList.getChildren().remove(3, roomsList.getChildren().size());
        setRoomsList(rooms);
        loadRoomsList();
    }
}
