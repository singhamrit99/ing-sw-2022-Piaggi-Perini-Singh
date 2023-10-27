package it.polimi.ingsw.exceptions;

public class FullDiningException extends Exception {
    public FullDiningException() {
        super("The values inserted are over the limit");
    }

    public FullDiningException(String s) {
        super(s);
    }
}
