package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    Bag bag = Bag.getInstance();
    EnumMap<Colors, Integer> enumMap = new EnumMap<>(Colors.class);

    void setupEnum() {
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 3);
        enumMap.put(Colors.YELLOW, 1);
    }

    void clearBag() {
        for (Colors color : Colors.values()) {
            enumMap.put(color, 0);
        }
        bag.setStudents(enumMap);
    }

    @Test
    void testAddStudentsException() {
        enumMap.put(Colors.PINK, -1);
        assertThrows(NegativeValueException.class, () -> bag.addStudents(enumMap));
    }

    @Test
    void testAddStudents() {
        setupEnum();

        //if students are not present in the board
        try {
            bag.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        assertEquals(2, bag.getStudents().get(Colors.BLUE));
        assertEquals(3, bag.getStudents().get(Colors.PINK));
        assertEquals(1, bag.getStudents().get(Colors.YELLOW));

        //if students are present in the board
        try {
            bag.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        assertEquals(4, bag.getStudents().get(Colors.BLUE));
        assertEquals(6, bag.getStudents().get(Colors.PINK));
        assertEquals(2, bag.getStudents().get(Colors.YELLOW));

        clearBag();
    }

    @Test
    void testRemoveStudentsException() {
        enumMap.put(Colors.BLUE, 2);
        try {
            bag.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        enumMap.clear();
        enumMap.put(Colors.BLUE, -1);

        //if value is negative
        assertThrows(NegativeValueException.class, () -> bag.removeStudents(enumMap));

        clearBag();
    }

    @Test
    void testRemoveStudents() {
        setupEnum();

        try {
            bag.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        enumMap.clear();
        enumMap.put(Colors.BLUE, 1);
        enumMap.put(Colors.PINK, 2);

        //remove
        try {
            bag.removeStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        clearBag();
    }

    @Test
    void drawStudents() {
        setupEnum();
        enumMap.put(Colors.GREEN, 2);
        enumMap.put(Colors.RED, 3);
        enumMap.put(Colors.YELLOW, 1);
        try {
            bag.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        try {
            bag.drawStudents(3);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        } catch (IncorrectArgumentException e) {
            e.printStackTrace();
        }

        clearBag();
    }

    @Test
    void hasEnoughStudents() {
        setupEnum();
        try {
            bag.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        assertTrue(bag.hasEnoughStudents(5));
        assertFalse(bag.hasEnoughStudents(7));
        clearBag();
    }
}