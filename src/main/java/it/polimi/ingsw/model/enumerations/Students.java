package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.io.Serializable;

/**
 * @author Amrit
 */
public enum Students implements Serializable {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);


    private final int index;

    Students(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Students getStudent(int index) throws IncorrectArgumentException {
        for (Students student : values()) {
            if (student.getIndex() == index) return student;
        }
        throw new IncorrectArgumentException("Index not associated with any color");
    }
}
