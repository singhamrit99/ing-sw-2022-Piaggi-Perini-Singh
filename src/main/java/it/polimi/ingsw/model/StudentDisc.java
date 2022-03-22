package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;

/**
 * @author Amrit
 */
public class StudentDisc {
    private final Colors discColor;

    public StudentDisc(Colors color) {
        this.discColor = color;
    }

    public Colors getStudentDiscColor() {
        return this.discColor;
    }
}
