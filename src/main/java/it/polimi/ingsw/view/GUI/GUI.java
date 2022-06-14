package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.GUI.controllerFX.*;
import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.view.UI;
import javafx.application.Application;
import javafx.application.Platform;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Amrit
 */
public class GUI implements UI {
    public static Client client;
    public LobbyController lobbyController;
    public RoomController roomController;

    public GameViewController gameController;

    public GUI(Client client) {
        GUI.client = client;
        GUI.client.setUi(this);
        GUI.client.view = StringNames.LAUNCHER;

        lobbyController = new LobbyController(this);
        roomController = new RoomController(this);
        gameController = new GameViewController(this);
    }

    public void start() {
        Application.launch(GUILauncher.class);
    }

    public void roomsAvailable(ArrayList<String> rooms) {
        if (GUI.client.view.equals(StringNames.LOBBY)) {
            if (LobbyController.isOpened()) {
                Platform.runLater(() -> lobbyController.update(rooms));
            } else {
                Platform.runLater(() -> {
                    lobbyController.setRoomsList(rooms);
                    Controller.load(ResourcesPath.LOBBY, lobbyController);
                });
            }
        }
    }

    public void roomJoin(ArrayList<String> players) {
        if (GUI.client.view.equals(StringNames.ROOM)) {
            if (RoomController.isOpened()) {
                Platform.runLater(() -> roomController.update(players));
            } else {
                Platform.runLater(() -> {
                    roomController.setPlayersList(players);
                    Controller.load(ResourcesPath.ROOM, roomController);
                });
            }
        }
    }

    @Override
    public void startGame() throws RemoteException {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            Platform.runLater(() -> Controller.load(ResourcesPath.GAME_VIEW, gameController));
        }
    }

    @Override
    public void currentPlayer(String currentPlayer) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) { //TODO
                // Platform.runLater(() -> gameController.setCurrentPlayer(currentPlayer));
            }
        }
    }

    @Override
    public void notifyCloud(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadClouds());
            }
        }
    }

    @Override
    public void diningChange(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadDining());
            }
        }
    }

    @Override
    public void deckChange(String input) {

    }

    @Override
    public void assistantCardPlayed(PropertyChangeEvent e) {

    }

    @Override
    public void islandChange(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadIslands());
            }
        }
    }

    @Override
    public void islandMerged(PropertyChangeEvent e) {

    }

    @Override
    public void islandConquest(PropertyChangeEvent e) {

    }

    @Override
    public void towersEvent(PropertyChangeEvent e) {

    }

    @Override
    public void gameOver(String winner) {

    }

    @Override
    public void coinsChanged(PropertyChangeEvent e) {

    }

    @Override
    public void entranceChanged(PropertyChangeEvent e) {

    }

    @Override
    public void removedProfessors(PropertyChangeEvent e) {

    }
}