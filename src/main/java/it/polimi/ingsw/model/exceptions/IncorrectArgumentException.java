package it.polimi.ingsw.model.exceptions;

public class IncorrectArgumentException extends Exception {
    public IncorrectArgumentException() {
        super("The given parameter is incorrect");
    }

    public IncorrectArgumentException(String s) {
        super(s);
    }
}
