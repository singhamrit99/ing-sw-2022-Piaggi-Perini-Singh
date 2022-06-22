package it.polimi.ingsw.model.deck;

import java.util.ArrayList;

/**
 * @author Amrit
 */
public interface Deck<T> {
    /**
     * Fill deck with all the cards of type T, reading from a json file
     */
    void fillDeck();

    /**
     * Return all cards of type T, reading from appropriate data structure.
     * @return arraylist (T)
     */
    ArrayList<T> getAllCards();

}
