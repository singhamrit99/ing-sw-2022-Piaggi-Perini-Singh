package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardDeckTest {
    final int NUM_OF_ASSISTANT_CARDS = 10;
    final AssistantCardDeck ASSISTANT_CARD_DECK = new AssistantCardDeck();

    @Test
    void fillDeck() {
        // Testing a deck, loaded from a JSON file, with all leader cards
        ASSISTANT_CARD_DECK.fillDeck();
        assertEquals(NUM_OF_ASSISTANT_CARDS, ASSISTANT_CARD_DECK.getAllCards().size());
    }

    @Test
    void getLeaderCards() {
        // Testing each card in deck
        for (AssistantCard assistantCard : ASSISTANT_CARD_DECK.getAllCards()) {
            assertTrue(assistantCard.getValue() > 0);
            assertTrue(assistantCard.getMove() > 0);
        }
    }
}