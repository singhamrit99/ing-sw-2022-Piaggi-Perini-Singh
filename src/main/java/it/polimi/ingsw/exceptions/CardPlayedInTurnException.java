package it.polimi.ingsw.exceptions;

public class CardPlayedInTurnException extends Exception {
    public CardPlayedInTurnException() {
        super("Incorrect argument");
    }

    public CardPlayedInTurnException(String s) {
        super(s);
    }
}
