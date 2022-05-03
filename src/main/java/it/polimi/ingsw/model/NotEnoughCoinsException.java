package it.polimi.ingsw.model;

public class NotEnoughCoinsException extends Exception {

    public NotEnoughCoinsException() {
        super();
    }

    public NotEnoughCoinsException(String s) {
        super(s);
    }
}
