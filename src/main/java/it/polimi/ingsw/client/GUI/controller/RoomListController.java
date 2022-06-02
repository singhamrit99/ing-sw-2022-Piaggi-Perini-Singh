package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
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
 * Class that shows all the game rooms availables to join
 */
public class RoomListController extends InitialStage implements Controller {
    protected static AtomicBoolean opened = new AtomicBoolean(false);

    private ArrayList<String> rooms;

    @FXML
    private GridPane roomsList;

    @FXML
    private Button createRoomButton;

    @FXML
    private Button exitButton;

    public RoomListController(GUI gui) {
        super(gui);
        new ArrayList<>();
    }

    @FXML
    public void initialize() {
        opened.set(true);
        gui.startAction();
        loadRoomsList();
        createRoomButton.setOnAction((event) -> {
            GUI.view = "new_room";
            opened.set(false);
            gui.stopAction();
            CreateNewGameController createNewGameController = new CreateNewGameController(gui);
            createNewGameController.setRooms(rooms);
            Controller.load(ResourcesPath.CREATE_NEW_GAME, createNewGameController);

            /*String filePath = ResourcesPath.FXML_FILE_PATH + ResourcesPath.CREATE_NEW_GAME + ResourcesPath.FILE_EXTENSION;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            loader.setController(createNewGameController);

            Stage mainWindow = GUILauncher.mainWindow;
            try {
                Scene sceneRooms = new Scene(loader.load());
                mainWindow.setScene(sceneRooms);
                mainWindow.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        });

        exitButton.setOnAction((event) -> {
            opened.set(false);
            GUI.view = "";
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
            joinRoom.setText("Join");

            joinRoom.setOnAction((event) -> {
                try {
                    GUI.view = "room";
                    opened.set(false);
                    gui.stopAction();
                    GUI.client.requestRoomJoin(roomName.getText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (RoomNotExistsException e) {
                    e.printStackTrace();
                } catch (UserNotRegisteredException e) {
                    e.printStackTrace();
                }
            });

            roomsList.addRow(i + 1, roomName, joinRoom);

            roomsList.setHalignment(roomName, HPos.CENTER);
            roomsList.setHalignment(joinRoom, HPos.CENTER);
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
