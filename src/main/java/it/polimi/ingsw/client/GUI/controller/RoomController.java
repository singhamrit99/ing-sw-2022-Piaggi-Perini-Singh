package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
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
 * Class that shows all the game rooms availables to join
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
        gui.startAction();
        opened.set(true);
        roomTitle.setText(GUI.client.getRoom());

        //Importing towers image
        try {
            blackTowerImage = new Image(new FileInputStream("src/main/resources/img/towers/black_tower.png"));
            whiteTowerImage = new Image(new FileInputStream("src/main/resources/img/towers/white_tower.png"));
            greyTowerImage = new Image(new FileInputStream("src/main/resources/img/towers/grey_tower.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        setExpertMode = new ToggleButton();
        setExpertMode.setOnAction((event) -> {
            try {
                GUI.client.setExpertMode(setExpertMode.selectedProperty().get());
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotLeaderRoomException e) {
                e.printStackTrace();
            } catch (UserNotInRoomException e) {
                e.printStackTrace();
            } catch (UserNotRegisteredException e) {
                e.printStackTrace();
            }
        });

        loadPlayersList();
        startGameButton.setOnAction((event) -> {
            opened.set(false);
            GUI.view = "board";
            gui.stopAction();
            try {
                GUI.client.startGame();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotLeaderRoomException e) {
                e.printStackTrace();
            } catch (UserNotInRoomException e) {
                e.printStackTrace();
            } catch (RoomNotExistsException e) {
                e.printStackTrace();
            } catch (UserNotRegisteredException e) {
                e.printStackTrace();
            }
        });

        leaveButton.setOnAction((event) -> {
            try {
                opened.set(false);
                GUI.view = "lobby";
                gui.stopAction();
                GUI.client.leaveRoom();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (UserNotInRoomException e) {
                e.printStackTrace();
            } catch (UserNotRegisteredException e) {
                e.printStackTrace();
            }
        });
    }

    public void setPlayersList(ArrayList<String> players) {
        this.players = players;
    }

    private void loadPlayersList() {
        ImageView blackTeam = new ImageView(blackTowerImage);
        ImageView whiteTeam = new ImageView(whiteTowerImage);
        ImageView greyTeam = new ImageView(greyTowerImage);

        for (int i = 0; i < players.size(); i++) {
            ImageView team = blackTeam;
            if (players.size() == 3) {
                if (i == 2) team = greyTeam;
            } else {
                if (i % 2 == 1) team = blackTeam;
                else team = whiteTeam;
            }

            team.setFitHeight(40);
            team.setFitWidth(40);

            RowConstraints row = new RowConstraints();
            row.setPrefHeight(40);

            playersList.getRowConstraints().add(row);
            Text playerName = new Text();
            playerName.setText(players.get(i));

            playersList.addRow(i + 1, playerName, team);

            playersList.setHalignment(playerName, HPos.CENTER);
            playersList.setHalignment(team, HPos.CENTER);
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
