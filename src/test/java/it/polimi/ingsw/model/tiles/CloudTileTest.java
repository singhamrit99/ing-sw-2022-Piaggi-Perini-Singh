package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class CloudTileTest {
    @Test
    void testRemoveStudents() {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        int i = 0;
        for (Colors color : Colors.values()) {
            students.put(color, i);
            i++;
        }
        Cloud tile = new Cloud("TestCloud");
        try {
            tile.addStudents(students);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        tile.drawStudents();
        for (Colors color : Colors.values()) {
            assertEquals(0, tile.getStudents().get(color));
        }
    }

    @Test
    void testAddStudents() {
        Cloud tile = new Cloud("TestCloud");
        EnumMap<Colors, Integer> newStudents = new EnumMap<>(Colors.class);
        int i = 0;
        for (Colors color : Colors.values()) {
            newStudents.put(color, i);
            i++;
        }
        try {
            tile.addStudents(newStudents);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        for (Colors color : Colors.values()) {
            assertEquals(newStudents.get(color), tile.getStudents().get(color));
        }
    }

    @Test
    void testAddStudentsException() {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        int i = -1;
        for (Colors color : Colors.values()) {
            students.put(color, i);
        }
        Cloud tile = new Cloud("TestCloud");
        assertThrows(NegativeValueException.class, () -> tile.addStudents(students));
    }

    @Test
    public void testGetName() {
        Cloud testTile = new Cloud("NotTheTestName");
        assertEquals("NotTheTestName", testTile.getName());
    }
}

