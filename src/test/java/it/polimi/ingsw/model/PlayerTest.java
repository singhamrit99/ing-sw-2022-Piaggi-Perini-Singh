package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.model.enumerations.Towers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player("Luffy", Towers.BLACK, 2);

    @Test
    void testRemoveCoins() {
        assertEquals(1, player.getCoins());
        assertThrows(NegativeValueException.class, () -> player.removeCoins(-2));

        try {
            player.removeCoins(1);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        } catch (IncorrectArgumentException e) {
            e.printStackTrace();
        }

        assertEquals(0, player.getCoins());
        assertThrows(IncorrectArgumentException.class, () -> player.removeCoins(1));
    }
}