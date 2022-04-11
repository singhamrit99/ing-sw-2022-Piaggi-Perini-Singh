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
     * @return how many cards the deck contains
     */
    int getDeckSize();

    T get(int index);

    ArrayList<T> getAllCards();

    /**
     * Adds a specific card to deck
     *
     * @param card to be added to deck
     */
    void addCard(T card);

    /**
     * Adds a specific group of cards to deck
     *
     * @param cards to be added to deck
     */
    void addCards(ArrayList<T> cards);

    /**
     * Eliminate a specific card from deck
     *
     * @param card card to be eliminated
     */
    void discardCard(T card);

    /**
     * Eliminate a specific group of cards from deck
     *
     * @param cards cards to be eliminated
     */
    void discardCards(ArrayList<T> cards);
}
