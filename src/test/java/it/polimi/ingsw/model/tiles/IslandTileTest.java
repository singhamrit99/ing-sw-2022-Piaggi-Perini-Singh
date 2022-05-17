package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.exceptions.NegativeValueException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class IslandTileTest {
    @Test
    void testGetName() {
        String test = "Test";
        Island island = new Island("Test");
        assertEquals(test, island.getName());
    }

    @Test
    void testGetTowersColor() {
        Island island = new Island("Test");

        for (Towers color : Towers.values()) {
            island.setTowersColor(color);
            assertEquals(color, island.getTowersColor());
        }
    }

    @Test
    void testSetTowersColor() {
        Island island = new Island("Test");

        for (Towers color : Towers.values()) {
            island.setTowersColor(color);
            assertEquals(island.getTowersColor(), color);
        }
    }

    @Test
    void testGetStudents() {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        int i = 0;
        for (Colors color : Colors.values()) {
            students.put(color, i);
            i++;
        }
        Island island = new Island("Test");
        try {
            island.addStudents(students);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        assertEquals(students, island.getStudents());
    }

    @Test
    void testGetNumOfTowers() throws NegativeValueException {
        int towers = 3;
        Island island = new Island("Test");
        island.sumTowers(towers);
        assertEquals(towers, island.getNumOfTowers());
        island.sumTowers(towers);
        towers += towers;
        assertEquals(towers, island.getNumOfTowers());
    }

    @Test
    void testAddStudents() {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        int i = 0;
        for (Colors color : Colors.values()) {
            students.put(color, i);
            i++;
        }
        Island island = new Island("Test");
        try {
            island.addStudents(students);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        assertEquals(students, island.getStudents());
    }

    @Test
    void testAddStudentsException() {
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        int i = -1;
        for (Colors color : Colors.values()) {
            students.put(color, i);

        }
        Island tile = new Island("Test");
        assertThrows(NegativeValueException.class, () -> tile.addStudents(students));
    }

    @Test
    void testHasMotherNature() {
        Island island = new Island("Test");
        assertFalse(island.hasMotherNature());
        island.moveMotherNature();
        assertTrue(island.hasMotherNature());
    }

    @Test
    void testMoveMotherNature() {
        Island island = new Island("Test");
        assertFalse(island.hasMotherNature());
        island.moveMotherNature();
        assertTrue(island.hasMotherNature());
    }

    @Test
    void testRemoveMotherNature() {
        Island island = new Island("Test");
        island.moveMotherNature();
        assertTrue(island.hasMotherNature());
        island.removeMotherNature();
        assertFalse(island.hasMotherNature());
    }
}