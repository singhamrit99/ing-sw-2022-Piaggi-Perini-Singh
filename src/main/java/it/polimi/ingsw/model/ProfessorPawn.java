package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;

/**
 * @author Amrit
 */
public class ProfessorPawn {
    private Colors professorColor;
    private boolean isActive;

    public ProfessorPawn(Colors professorColor) {
        isActive = false;
        this.professorColor = professorColor;
    }

    public void changeStatus() {
        isActive = !isActive;
    }
}
