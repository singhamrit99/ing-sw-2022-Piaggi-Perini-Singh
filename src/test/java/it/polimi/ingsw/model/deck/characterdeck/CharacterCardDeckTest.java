package it.polimi.ingsw.model.deck.characterdeck;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.cards.charactercard.Ability;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.Requirements;
import it.polimi.ingsw.model.cards.charactercard.Type;
import it.polimi.ingsw.model.enumerations.Actions;
import it.polimi.ingsw.model.enumerations.Types;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardDeckTest {
    final int NUM_OF_ASSISTANT_CARDS = 12;
    final CharacterCardDeck CHARACTER_CARD_DECK = new CharacterCardDeck();

    @Test
    void fillDeck() {
        CHARACTER_CARD_DECK.fillDeck();
        assertEquals(NUM_OF_ASSISTANT_CARDS, CHARACTER_CARD_DECK.getAllCards().size());
    }

    @Test
    void getAllCards() {
        for (CharacterCard characterCard : CHARACTER_CARD_DECK.getAllCards()) {
            assertEquals(characterCard.getStatus(), 0);
        }
    }

    @Test
    void testGetDeck() {
        CHARACTER_CARD_DECK.fillDeck();
        assertEquals(NUM_OF_ASSISTANT_CARDS, CHARACTER_CARD_DECK.getDeck().size());
    }

    @Test
    void discardCard() {
        CHARACTER_CARD_DECK.fillDeck();
        CHARACTER_CARD_DECK.discardCard(0);
        assertEquals(NUM_OF_ASSISTANT_CARDS - 1, CHARACTER_CARD_DECK.getAllCards().size());
    }

    @Test
    void charactersCardRequirementsValue() {
        CHARACTER_CARD_DECK.fillDeck();
        CharacterCard card = CHARACTER_CARD_DECK.get(0);
        assertFalse(card.getRequirements().getValue()<=0);
        assertEquals(NUM_OF_ASSISTANT_CARDS, CHARACTER_CARD_DECK.getAllCards().size());
    }

    @Test
    void constructorsSupportToCharacterCards() {
        Requirements test = new Requirements();
        assertNull(test.getRequirements());
        Type testType = new Type(Types.SELECTOR,1);
        assertEquals(Types.SELECTOR.toString(),testType.getName());
        assertEquals(testType.getValue(),1);
        Ability testAbility = new Ability(Actions.NO_ENTRY_TILE,2);
        assertEquals(Actions.NO_ENTRY_TILE,testAbility.getAction());
        assertEquals(testAbility.getValue(),2);
    }

}