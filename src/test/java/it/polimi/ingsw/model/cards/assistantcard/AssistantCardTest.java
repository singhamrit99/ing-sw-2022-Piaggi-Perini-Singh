package it.polimi.ingsw.model.cards.assistantcard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

    @Test
    void testCardConstructor() {
        AssistantCard assistantCard = new AssistantCard("Carta1", 1);
        assertFalse(assistantCard.getHasPlayed());
    }

}