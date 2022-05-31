package it.polimi.ingsw.client;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UI {
    void startGame() throws RemoteException;

    void currentPlayer(String s);

    void notifyCloud(PropertyChangeEvent e);

    void deckChange(String input);

    void assistantCardPlayed(PropertyChangeEvent e);

    void islandChange(PropertyChangeEvent e);

    void islandMerged(PropertyChangeEvent e);

    void islandConquest(PropertyChangeEvent e);

    void diningChange(PropertyChangeEvent e);

    void towersEvent(PropertyChangeEvent e);

    void gameOver(String winner);

    void coinsChanged(PropertyChangeEvent e);

    void entranceChanged(PropertyChangeEvent e);

    void removedProfessors(PropertyChangeEvent e);

    /**
     * Notifies that the rooms available have been updated
     */
    void roomsAvailable(ArrayList<String> rooms);
}
