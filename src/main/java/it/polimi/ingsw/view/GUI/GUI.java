package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.GUI.controllerFX.*;
import it.polimi.ingsw.view.UI;
import javafx.application.Application;
import javafx.application.Platform;

import javax.naming.ldap.Control;
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
    public GameOverController gameOverController;

    /**
     * GUI constructor. Binds this instance to a Client instance.
     *
     * @param client the Client that is using this GUI.
     */
    public GUI(Client client) {
        GUI.client = client;
        GUI.client.setUi(this);
        GUI.client.view = StringNames.LAUNCHER;

        lobbyController = new LobbyController(this);
        roomController = new RoomController(this);
        gameController = new GameViewController(this);
        gameOverController = new GameOverController(this);
    }

    /**
     * Starts the application.
     */
    public void start() {
        Application.launch(GUILauncher.class);
    }

    /**
     * Method that outputs the available rooms in graphical format
     *
     * @param rooms the rooms to display on the server.
     */
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

    /**
     * Method used to join a room.
     *
     * @param players players in the room
     */
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

    /**
     * Method used to notify a change in professors
     */
    @Override
    public void professorChanged() {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadProfs());
            }
        }
    }

    @Override
    public void characterChanged(PropertyChangeEvent evt) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadCharacters());
            }
        }
    }

    /**
     * Method used to start the game
     *
     * @throws RemoteException Thrown in case of a network error
     */
    @Override
    public void startGame() throws RemoteException {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            Platform.runLater(() -> Controller.load(ResourcesPath.GAME_VIEW, gameController));
        }
    }

    /**
     * Displays the current player
     *
     * @param currentPlayer the current player's nickname
     */
    @Override
    public void currentPlayer(String currentPlayer) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.setCurrentPlayer(currentPlayer));
            }
        }
    }

    /**
     * Notifies cloud change through an event.
     *
     * @param e the event that changed the clouds.
     */
    @Override
    public void notifyCloud(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadClouds());
            }
        }
    }

    /**
     * Notifies dining change through an event.
     *
     * @param e dining change event.
     */
    @Override
    public void diningChange(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadDining());
            }
        }
    }

    /**
     * Notifies assistant deck change
     *
     * @param input the assistant card to play.
     */
    @Override
    public void deckChange(String input) {

    }

    /**
     * Notifies island changed through an event.
     *
     * @param e island change event
     */
    @Override
    public void islandChange(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadIslands());
            }
        }
    }

    /**
     * Notifies island merge through an event.
     *
     * @param e island merge event
     */
    @Override
    public void islandMerged(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadIslands());
            }
        }
    }

    /**
     * Notifies an island conquest through an event.
     *
     * @param e island conquest event
     */
    @Override
    public void islandConquest(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadIslands());
            }
        }
    }

    /**
     * Notifies tower change through an event.
     *
     * @param e tower change event.
     */
    @Override
    public void towersEvent(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadTowers());
            }
        }
    }

    /**
     * Notifies game over through an event.
     *
     * @param leavingPlayer The leaving player in case of quit-out and remaking of game
     * @param winner        Winner team if the game reached a valid end.
     */
    @Override
    public void gameOver(String leavingPlayer, String winner) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> {
                    gameOverController.setLeavingPlayer(leavingPlayer);
                    gameOverController.setWinner(winner);
                    Controller.load(ResourcesPath.GAME_OVER, gameOverController);
                });
            }
        }
    }

    /**
     * Notifies coins changed through an event.
     *
     * @param e coins changed event.
     */
    @Override
    public void coinsChanged(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadCoins());
            }
        }
    }

    /**
     * Notifies entrance changed through an event.
     *
     * @param e entrance changed event.
     */
    @Override
    public void entranceChanged(PropertyChangeEvent e) {
        if (GUI.client.view.equals(StringNames.INGAME)) {
            if (gameController.isOpened()) {
                Platform.runLater(() -> gameController.reloadEntrance());
            }
        }
    }
}