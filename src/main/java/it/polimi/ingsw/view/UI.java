package it.polimi.ingsw.view;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * All of these methods are reimplemented and overridden in CLI and GUI.
 */
public interface UI {
    /**
     * Starts the game.
     *
     * @throws RemoteException Thrown in case of a network error.
     */
    void startGame() throws RemoteException;

    /**
     * Returns the currentPlayer-
     *
     * @param s current player name.
     */
    void currentPlayer(String s);

    /**
     * Returns the cloud that changed
     *
     * @param e the event that changed the clouds.
     */
    void notifyCloud(PropertyChangeEvent e);

    /**
     * Assistant card played (outgoing)
     *
     * @param input the assistant card to play.
     */
    void deckChange(String input);

    /**
     * Island change display event
     *
     * @param e island change event
     */
    void islandChange(PropertyChangeEvent e);

    /**
     * Island merge display event
     *
     * @param e island merge event
     */
    void islandMerged(PropertyChangeEvent e);

    /**
     * Island conquest display event
     *
     * @param e island conquest event
     */
    void islandConquest(PropertyChangeEvent e);

    /**
     * Dining change display event
     *
     * @param e dining change event.
     */
    void diningChange(PropertyChangeEvent e);

    /**
     * Towers change display event.
     *
     * @param e tower change event.
     */
    void towersEvent(PropertyChangeEvent e);

    /**
     * Game over display event.
     *
     * @param leavingPlayer The leaving player in case of quit-out and remaking of game
     * @param winner        Winner team if the game reached a valid end.
     */
    void gameOver(String leavingPlayer, String winner);

    /**
     * Coins change display event.
     *
     * @param e coins changed event.
     */
    void coinsChanged(PropertyChangeEvent e);

    /**
     * Entrance change display event
     *
     * @param e entrance changed event.
     */
    void entranceChanged(PropertyChangeEvent e);

    /**
     * Rooms display event
     *
     * @param rooms the rooms to display on the server.
     */
    void roomsAvailable(ArrayList<String> rooms);

    /**
     * Method used to join rooms
     *
     * @param players players in the room
     */
    void roomJoin(ArrayList<String> players);

    /**
     * Event that displays if anyone professors changed.
     */
    void professorChanged();

    void characterChanged(PropertyChangeEvent evt);
}
