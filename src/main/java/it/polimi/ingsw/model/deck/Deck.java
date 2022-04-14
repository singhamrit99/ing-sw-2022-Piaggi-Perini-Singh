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

    T get(int index);

    ArrayList<T> getAllCards();

    /**
     * Eliminate a specific card from deck
     */
    void discardCard(int index);
}
