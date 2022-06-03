package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.StringNames;
import it.polimi.ingsw.exceptions.NotLeaderRoomException;
import it.polimi.ingsw.exceptions.RoomNotExistsException;
import it.polimi.ingsw.exceptions.UserNotInRoomException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Amrit
 */
public class RoomController extends InitialStage implements Controller {
    private ArrayList<String> players = new ArrayList<>();
    protected static AtomicBoolean opened = new AtomicBoolean(false);

    @FXML
    private GridPane playersList;
    @FXML
    private Text roomTitle;
    @FXML
    private Image blackTowerImage;
    @FXML
    private Image whiteTowerImage;
    @FXML
    private Image greyTowerImage;
    @FXML
    private Button startGameButton;
    @FXML
    private Button leaveButton;
    @FXML
    private ToggleButton setExpertMode;

    public RoomController(GUI gui) {
        super(gui);
    }

    @FXML
    public void initialize() {
        opened.set(true);
        roomTitle.setText(GUI.client.getRoom());
        setExpertMode = new ToggleButton();

        try {
            blackTowerImage = new Image(new FileInputStream(ResourcesPath.BLACK_TOWER));
            whiteTowerImage = new Image(new FileInputStream(ResourcesPath.WHITE_TOWER));
            greyTowerImage = new Image(new FileInputStream(ResourcesPath.GREY_TOWER));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadPlayersList();

        leaveButton.setOnAction((event) -> {
            try {
                opened.set(false);
                GUI.client.view = StringNames.LOBBY;
                GUI.client.leaveRoom();
            } catch (RemoteException e) {
                Utility.showErrorDialogBox(StringNames.CONNECTION_ERROR);
            } catch (UserNotInRoomException e) {
                Utility.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Utility.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
            }
        });
    }

    public void setPlayersList(ArrayList<String> players) {
        this.players = players;
    }

    private void loadPlayersList() {
        try {
            if (GUI.client.isLeader()) {
                startGameButton.setVisible(true);

                setExpertMode.setOnAction((event) -> {
                    try {
                        GUI.client.setExpertMode(setExpertMode.selectedProperty().get());
                    } catch (RemoteException e) {
                        Utility.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                    } catch (NotLeaderRoomException e) {
                        Utility.showErrorDialogBox(StringNames.NO_LEADER);
                    } catch (UserNotInRoomException e) {
                        Utility.showErrorDialogBox(StringNames.NOT_IN_ROOM);
                    } catch (UserNotRegisteredException e) {
                        Utility.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                    }
                });

                startGameButton.setOnAction((event) -> {
                    opened.set(false);
                    GUI.client.view = StringNames.BOARD;
                    try {
                        GUI.client.startGame();
                    } catch (RemoteException e) {
                        Utility.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                    } catch (NotLeaderRoomException e) {
                        Utility.showErrorDialogBox(StringNames.NO_LEADER);
                    } catch (UserNotInRoomException e) {
                        Utility.showErrorDialogBox(StringNames.NOT_IN_ROOM);
                    } catch (RoomNotExistsException e) {
                        Utility.showErrorDialogBox(StringNames.NO_SUCH_ROOM);
                    } catch (UserNotRegisteredException e) {
                        Utility.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                    }
                });
            } else {
                startGameButton.setVisible(false);
            }
        } catch (RemoteException e) {
            Utility.showErrorDialogBox(StringNames.CONNECTION_ERROR);
        } catch (RoomNotExistsException e) {
            Utility.showErrorDialogBox(StringNames.NO_SUCH_ROOM);
        } catch (UserNotInRoomException e) {
            Utility.showErrorDialogBox(StringNames.NOT_IN_ROOM);
        }

        for (int i = 0; i < players.size(); i++) {
            ImageView team = new ImageView();
            if (players.size() == 3) {
                //TODO do randomly selected
                if (i == 0) team = new ImageView(blackTowerImage);
                if (i == 1) team = new ImageView(whiteTowerImage);
                if (i == 2) team = new ImageView(greyTowerImage);
            } else {
                if (i % 2 == 1) team = new ImageView(blackTowerImage);
                else team = new ImageView(whiteTowerImage);
            }
            team.setFitHeight(40);
            team.setFitWidth(40);

            RowConstraints row = new RowConstraints();
            row.setPrefHeight(40);

            playersList.getRowConstraints().add(row);
            Text playerName = new Text();
            playerName.setText(players.get(i));

            playersList.addRow(i + 1, playerName, team);

            GridPane.setHalignment(playerName, HPos.CENTER);
            GridPane.setHalignment(team, HPos.CENTER);
        }
    }

    public static boolean isOpened() {
        return opened.get();
    }

    public void update(ArrayList<String> players) {
        playersList.getChildren().remove(3, playersList.getChildren().size());
        setPlayersList(players);
        loadPlayersList();
    }
}
