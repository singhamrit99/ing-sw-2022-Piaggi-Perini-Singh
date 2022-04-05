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
    @Test
    public void invalidEnummapException()
    {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        int i = -1;
        for (Colors color : Colors.values()) {
            students.put(color, i);

        }
        SetupCharacterCard tile = new SetupCharacterCard(1,1,"testpower");

        IncorrectArgumentException e = assertThrows(IncorrectArgumentException.class, () -> tile.addStudents(students));


        String expectedMessage= "EnumMap is not correct";
        String actualMessage= e.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}