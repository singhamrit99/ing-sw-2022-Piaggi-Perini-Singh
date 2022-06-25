package it.polimi.ingsw.view.GUI.controllerFX;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    private HBox expertBox;
    @FXML
    private GridPane playersList;
    @FXML
    private Text roomTitle;
    @FXML
    private Button startGameButton, leaveButton;

    private Image blackTowerImage, whiteTowerImage, greyTowerImage;

    private SwitchButton setExpertMode;
    private Label expertMode;

    public RoomController(GUI gui) {
        super(gui);
    }

    public static void setOpened(boolean b) {
        opened.set(false);
    }

    @FXML
    public void initialize() {
        opened.set(true);
        roomTitle.setText(GUI.client.getRoom());
        expertMode = new Label();
        expertMode.setLabelFor(setExpertMode);
        setExpertMode = new SwitchButton();
        setExpertMode.setMaxHeight(20);
        setExpertMode.setMaxWidth(20);
        expertBox.getChildren().add(expertMode);
        expertBox.getChildren().add(setExpertMode);

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
                GUI.client.leaveRoom();
                GUI.client.refreshScreenLobby();
            } catch (RemoteException e) {
                Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
            } catch (UserNotInRoomException e) {
                Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
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
                setExpertMode.setVisible(true);
                setExpertMode.changeState(GUI.client.getExpertMode());
                expertMode.setText(StringNames.EXPERT_MODE);

                startGameButton.setOnAction((event) -> {
                    opened.set(false);
                    try {
                        GUI.client.startGame();
                    } catch (RemoteException e) {
                        Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                    } catch (NotLeaderRoomException e) {
                        Controller.showErrorDialogBox(StringNames.NO_LEADER);
                    } catch (UserNotInRoomException e) {
                        Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
                    } catch (RoomNotExistsException e) {
                        Controller.showErrorDialogBox(StringNames.NO_SUCH_ROOM);
                    } catch (UserNotRegisteredException e) {
                        Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                    } catch (NotEnoughPlayersException e) {
                        Controller.showErrorDialogBox(StringNames.ALONE_IN_ROOM);
                    }
                });

                setExpertMode.setOnMouseClicked((event) -> {
                    try {
                        setExpertMode.changeState(!setExpertMode.getState());
                        GUI.client.setExpertMode(setExpertMode.getState());
                    } catch (RemoteException e) {
                        Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
                    } catch (NotLeaderRoomException e) {
                        Controller.showErrorDialogBox(StringNames.NO_LEADER);
                    } catch (UserNotInRoomException e) {
                        Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
                    } catch (UserNotRegisteredException e) {
                        Controller.showErrorDialogBox(StringNames.USER_NOT_REGISTERED);
                    }
                });
            } else {
                startGameButton.setVisible(false);
                setExpertMode.setVisible(false);
                if (GUI.client.getExpertMode()) {
                    expertMode.setText(StringNames.EXPERT_MODE_ON);
                } else {
                    expertMode.setText(StringNames.EXPERT_MODE_OFF);
                }
            }
        } catch (RemoteException e) {
            Controller.showErrorDialogBox(StringNames.CONNECTION_ERROR);
        } catch (RoomNotExistsException e) {
            Controller.showErrorDialogBox(StringNames.NO_SUCH_ROOM);
        } catch (UserNotInRoomException e) {
            Controller.showErrorDialogBox(StringNames.NOT_IN_ROOM);
        }

        for (int i = 0; i < players.size(); i++) {
            ImageView team = new ImageView();
            if (players.size() == 3) {
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
