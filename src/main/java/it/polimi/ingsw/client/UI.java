package it.polimi.ingsw.client;

import java.util.ArrayList;

public interface UI {

    /**
     * Notifies that the rooms available have been updated
     * @param rooms are the rooms available
     */
    void roomsAvailable(ArrayList<String> rooms);
}
