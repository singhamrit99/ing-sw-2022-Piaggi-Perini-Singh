package it.polimi.ingsw.model.exceptions;

public class ProfessorNotFoundException extends Throwable {
    public ProfessorNotFoundException() {
        super("Professor not found");
    }

    public ProfessorNotFoundException(String s) {
        super(s);
    }
}
