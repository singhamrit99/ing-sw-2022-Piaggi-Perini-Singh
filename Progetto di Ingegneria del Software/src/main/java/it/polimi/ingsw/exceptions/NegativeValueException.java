package it.polimi.ingsw.exceptions;

public class NegativeValueException extends Throwable {
    public NegativeValueException() {
        super("The parameter's value is negative");
    }

    public NegativeValueException(String s) {
        super(s);
    }
}
