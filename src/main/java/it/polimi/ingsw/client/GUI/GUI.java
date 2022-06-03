package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.controller.*;
import it.polimi.ingsw.client.StringNames;
import it.polimi.ingsw.client.UI;
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
    public static Controller controller;
    public LobbyController lobbyController;
    public RoomController roomController;

    //private final AtomicBoolean isDoing;

    public GUI(Client client) {
        //this.isDoing = new AtomicBoolean(false);
        GUI.client = client;
        GUI.client.setUi(this);
        GUI.client.view = StringNames.LAUNCHER;
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
                    lobbyController = new LobbyController(this);
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
                    roomController = new RoomController(this);
                    roomController.setPlayersList(players);
                    Controller.load(ResourcesPath.ROOM, roomController);
                });
            }
        }
    }

    @Override
    public void startGame() throws RemoteException{
        if (GUI.client.view.equals(StringNames.BOARD)) {
            Platform.runLater(() -> {
                GameViewController gameController = new GameViewController(this);
                Controller.load(ResourcesPath.GAME_VIEW, gameController);
/*
                try {
                    gameController.setPlayersViewMenu(GUI.client.getNicknamesInRoom());
                } catch (RemoteException | RoomNotExistsException | UserNotInRoomException e) {
                    throw new RuntimeException(e);
                }
    */
            });
        }
    }

    /*public void startAction() {
        if (!isDoing.get()) {
            isDoing.set(true);
        }
    }

    public void stopAction() {
        if (isDoing.get()) {
            isDoing.set(false);
        }
    }

    public boolean isDoing() {
        return this.isDoing.get();
    }*/

    @Override
    public void currentPlayer(String s) {

    }

    @Override
    public void notifyCloud(PropertyChangeEvent e) {

    }

    @Override
    public void deckChange(String input) {

    }

    @Override
    public void assistantCardPlayed(PropertyChangeEvent e) {

    }

    @Override
    public void islandChange(PropertyChangeEvent e) {

    }

    @Override
    public void islandMerged(PropertyChangeEvent e) {

    }

    @Override
    public void islandConquest(PropertyChangeEvent e) {

    }

    @Override
    public void diningChange(PropertyChangeEvent e) {

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