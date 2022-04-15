package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

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

    public static Colors getStudent(int index) throws IllegalArgumentException {
        for (Colors student : values()) {
            if (student.getIndex() == index) return student;
        }
        throw new IllegalArgumentException("Index not associated with any color");
    }
}
