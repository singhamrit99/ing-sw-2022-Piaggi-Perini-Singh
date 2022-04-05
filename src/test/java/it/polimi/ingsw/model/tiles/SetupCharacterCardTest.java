package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class SetupCharacterCardTest {

    @Test
    void testaddStudents() {

        SetupCharacterCard tile = new SetupCharacterCard(1, 1, "test");
        EnumMap<Colors, Integer> newstudents = new EnumMap<>(Colors.class);
        int i = 0;
        for (Colors color : Colors.values()) {
            newstudents.put(color, i);
            i++;
        }

        try {
            tile.addStudents(newstudents);
        } catch (IncorrectArgumentException e) {
            e.printStackTrace();
        }
        for (Colors color: Colors.values()) {
            assertEquals(newstudents.get(color), tile.getStudents().get(color));
        }

    }
}