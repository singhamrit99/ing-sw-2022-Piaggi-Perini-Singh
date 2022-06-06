package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.exceptions.AssistantCardNotFoundException;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardDeckTest {
    final int NUM_OF_ASSISTANT_CARDS = 10;
    final AssistantCardDeck ASSISTANT_CARD_DECK = new AssistantCardDeck("random Nickname");

    @Test
    void fillDeck() {
        // Testing a deck, loaded from a JSON file, with all leader cards
        ASSISTANT_CARD_DECK.fillDeck();
        assertEquals(NUM_OF_ASSISTANT_CARDS, ASSISTANT_CARD_DECK.getAllCards().size());
    }

    @Test
    void testGetAllCards() {
        // Testing each card in deck
        for (AssistantCard assistantCard : ASSISTANT_CARD_DECK.getAllCards()) {
            assertTrue(assistantCard.getMove() > 0);
        }
    }

    @Test
    void testGetDeck(){
        ASSISTANT_CARD_DECK.fillDeck();
        assertEquals(NUM_OF_ASSISTANT_CARDS, ASSISTANT_CARD_DECK.getDeck().size());
    }

    @Test
    void testDiscardCard() throws AssistantCardNotFoundException {
        ASSISTANT_CARD_DECK.fillDeck();
        ASSISTANT_CARD_DECK.get("1");
        assertEquals(NUM_OF_ASSISTANT_CARDS-1 , ASSISTANT_CARD_DECK.getAllCards().size());
    }
}