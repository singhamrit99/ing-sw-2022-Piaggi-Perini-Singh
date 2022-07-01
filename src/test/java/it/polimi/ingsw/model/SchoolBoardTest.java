package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDiningException;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class SchoolBoardTest {
    SchoolBoard schoolBoard2 = new SchoolBoard(2);
    SchoolBoard schoolBoard3 = new SchoolBoard(3);
    EnumMap<Colors, Integer> enumMap = StudentManager.createEmptyStudentsEnum();

    void setupEnum() {
        enumMap.put(Colors.BLUE, 2);
        enumMap.put(Colors.PINK, 3);
        enumMap.put(Colors.YELLOW, 1);
    }

    @Test
    void testAddStudentsException() {
        enumMap.put(Colors.PINK, -1);
        assertThrows(NegativeValueException.class, () -> schoolBoard2.addStudents(enumMap));
    }

    @Test
    void testAddStudents() {
        setupEnum();

        //if students are not present in the board
        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        assertEquals(2, schoolBoard2.getEntrance().get(Colors.BLUE));
        assertEquals(3, schoolBoard2.getEntrance().get(Colors.PINK));
        assertEquals(1, schoolBoard2.getEntrance().get(Colors.YELLOW));

        //if students are present in the board
        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        assertEquals(4, schoolBoard2.getEntrance().get(Colors.BLUE));
        assertEquals(6, schoolBoard2.getEntrance().get(Colors.PINK));
        assertEquals(2, schoolBoard2.getEntrance().get(Colors.YELLOW));
    }

    @Test
    void testRemoveStudentsException() {
        enumMap.put(Colors.BLUE, 2);
        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        enumMap.clear();
        enumMap.put(Colors.BLUE, -1);
        //if value is negative
        assertThrows(NegativeValueException.class, () -> schoolBoard2.removeStudents(enumMap));
    }

    @Test
    void testRemoveStudents() {
        setupEnum();

        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        enumMap.clear();
        enumMap.put(Colors.BLUE, 1);
        enumMap.put(Colors.PINK, 2);

        //remove
        try {
            schoolBoard2.removeStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMoveStudentsException() {
        enumMap.put(Colors.PINK, -1);
        assertThrows(NegativeValueException.class, () -> schoolBoard2.moveStudents(enumMap));
    }

    @Test
    void testMoveStudents() {
        setupEnum();
        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        //if students are not present in the board
        try {
            schoolBoard2.moveStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
        //if students are present in the board
        try {
            schoolBoard2.moveStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddProfessor() {
        schoolBoard2.addProfessor(Colors.BLUE);
        assertEquals(1, schoolBoard2.getProfessorsTable().size());
    }

    @Test
    void testRemoveProfessorException() {
        assertThrows(ProfessorNotFoundException.class, () -> schoolBoard2.removeProfessor(Colors.BLUE));
    }

    @Test
    void testRemoveProfessor() {
        schoolBoard2.addProfessor(Colors.BLUE);
        assertEquals(1, schoolBoard2.getProfessorsTable().size());

        try {
            schoolBoard2.removeProfessor(Colors.BLUE);
        } catch (ProfessorNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(0, schoolBoard2.getProfessorsTable().size());
    }

    @Test
    void testHasEnoughStudentsException() {
        enumMap.put(Colors.BLUE, 2);
        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        enumMap.clear();
        enumMap.put(Colors.BLUE, -1);
        //if value is negative
        assertThrows(NegativeValueException.class, () -> schoolBoard2.hasEnoughStudents(enumMap));
    }

    @Test
    void testHasEnoughStudents() {
        setupEnum();
        try {
            schoolBoard2.addStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        enumMap.clear();
        enumMap.put(Colors.YELLOW, 1);
        //return true
        try {
            assertTrue(schoolBoard2.hasEnoughStudents(enumMap));
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        enumMap.put(Colors.YELLOW, 2);
        //return false
        try {
            assertFalse(schoolBoard2.hasEnoughStudents(enumMap));
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMoveTowers() {
        schoolBoard2.moveTowers(-3);
        assertEquals(5, schoolBoard2.getTowers());

        schoolBoard3.moveTowers(-3);
        assertEquals(3, schoolBoard3.getTowers());
    }

    @Test
    void testGetStudentsByColorException() {
        setupEnum();
        try {
            schoolBoard2.addStudents(enumMap);
            schoolBoard2.moveStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        assertThrows(IncorrectArgumentException.class, () -> schoolBoard2.getStudentsByColor(Colors.getStudent(6)));
    }

    @Test
    void getStudentsByColor() {
        setupEnum();
        try {
            schoolBoard2.addStudents(enumMap);
            schoolBoard2.moveStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }

        assertEquals(2, schoolBoard2.getStudentsByColor(Colors.BLUE));
    }

    @Test
    void testRemoveDiningStudentsException() {
        enumMap.put(Colors.BLUE, 2);
        try {
            schoolBoard2.addStudentsDining(enumMap);
        } catch (NegativeValueException | FullDiningException e) {
            e.printStackTrace();
        }

        enumMap.clear();
        enumMap.put(Colors.BLUE, -1);
        //if value is negative
        assertThrows(NegativeValueException.class, () -> schoolBoard2.removeDiningStudents(enumMap));
    }

    @Test
    void testRemoveDiningStudents() {
        setupEnum();

        try {
            schoolBoard2.addStudentsDining(enumMap);
        } catch (NegativeValueException | FullDiningException e) {
            e.printStackTrace();
        }
        enumMap.clear();
        enumMap.put(Colors.BLUE, 1);
        enumMap.put(Colors.PINK, 2);

        //remove
        try {
            schoolBoard2.removeDiningStudents(enumMap);
        } catch (NegativeValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddStudentsDiningException() {
        enumMap.put(Colors.PINK, -1);
        assertThrows(NegativeValueException.class, () -> schoolBoard2.addStudentsDining(enumMap));
    }

    @Test
    void testAddStudentsDining() {
        setupEnum();

        //if students are not present in the board
        try {
            schoolBoard2.addStudentsDining(enumMap);
        } catch (NegativeValueException | FullDiningException e) {
            e.printStackTrace();
        }
        assertEquals(2, schoolBoard2.getDining().get(Colors.BLUE));
        assertEquals(3, schoolBoard2.getDining().get(Colors.PINK));
        assertEquals(1, schoolBoard2.getDining().get(Colors.YELLOW));

        //if students are present in the board
        try {
            schoolBoard2.addStudentsDining(enumMap);
        } catch (NegativeValueException | FullDiningException e) {
            e.printStackTrace();
        }
        assertEquals(4, schoolBoard2.getDining().get(Colors.BLUE));
        assertEquals(6, schoolBoard2.getDining().get(Colors.PINK));
        assertEquals(2, schoolBoard2.getDining().get(Colors.YELLOW));
    }
}