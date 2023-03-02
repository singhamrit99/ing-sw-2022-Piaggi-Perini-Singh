package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import java.io.Serializable;

/**
 * @author Amrit
 */
public enum Colors implements Serializable {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);

    private final int index;

    Colors(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    /**
     * A method used to return a student of a certain color.
     * @param index The color of the student to return.
     * @return The student to return, implemented as a Colors instance.
     * @throws IllegalArgumentException if the index is not associated with any of the Enum values this exception is thrown.
     */
    public static Colors getStudent(int index) throws IncorrectArgumentException {
        for (Colors student : values()) {
            if (student.getIndex() == index) return student;
        }
        throw new IncorrectArgumentException("Index not associated with any color");
    }
}
