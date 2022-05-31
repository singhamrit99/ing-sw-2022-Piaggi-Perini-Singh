package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.RoomNotExistsException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Amrit
 * Class that shows all the game rooms availables to join
 */

public class RoomListController extends InitialStage implements Controller {
    private Stage stage = new Stage();
    private Scene scene;

    private ArrayList<String> rooms;

    @FXML
    private GridPane roomsList;

    @FXML
    private Button newGameButton;

    @FXML
    private Button exitButton;

    public RoomListController(GUI gui) {
        super(gui);
        new ArrayList<>();
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

        for (int i = 0, j = 1; i < rooms.size(); i++, j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(40);
            roomsList.getRowConstraints().add(row);

            Text roomName = new Text();
            roomName.setText(rooms.get(i));

            Button joinRoom = new Button();
            joinRoom.setText("Join");

            joinRoom.setOnAction((event) -> {
                try {
                    GUI.client.requestRoomJoin(roomName.getText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (RoomNotExistsException e) {
                    e.printStackTrace();
                } catch (UserNotRegisteredException e) {
                    e.printStackTrace();
                }

                gui.stopAction();

                WaitingWindowController waitingWindowController = new WaitingWindowController();
                waitingWindowController.setMessage(WaitingMessages.CONNECTING);
                Controller.startStage(ResourcesPath.WAITING_WINDOW, waitingWindowController);
                GUI.controller = waitingWindowController;
                stage.close();
            });

            roomsList.addRow(j, roomName, joinRoom);

            roomsList.setHalignment(roomName, HPos.CENTER);
            roomsList.setHalignment(joinRoom, HPos.CENTER);

        }
    }
}
