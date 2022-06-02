package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.UserNotInRoomException;
import it.polimi.ingsw.exceptions.UserNotRegisteredException;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Amrit
 * Class that shows all the game rooms availables to join
 */
public class RoomController extends InitialStage implements Controller {
    private ArrayList<String> players = new ArrayList<>();

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

    public RoomController(GUI gui) {
        super(gui);
    }

    @FXML
    public void initialize() {
        gui.startAction();
        roomTitle.setText(GUI.client.getRoom());

        //Importing towers image
        try {
            blackTowerImage = new Image(new FileInputStream("src/main/resources/img/towers/black_tower.png"));
            whiteTowerImage = new Image(new FileInputStream("src/main/resources/img/towers/white_tower.png"));
            greyTowerImage = new Image(new FileInputStream("src/main/resources/img/towers/grey_tower.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadPlayersList();
       /* startGameButton.setOnAction((event) -> {
            CreateNewGameController createNewGameController = new CreateNewGameController(gui);
            createNewGameController.setRooms(rooms);
            Controller.startStage(ResourcesPath.CREATE_NEW_GAME_ACTION, createNewGameController);
            gui.stopAction();
            closeStage();
        });*/

        leaveButton.setOnAction((event) -> {
            try {
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
}
