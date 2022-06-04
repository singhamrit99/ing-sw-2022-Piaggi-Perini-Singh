package it.polimi.ingsw.exceptions;

public class NotEnoughPlayersException extends Exception{
    public NotEnoughPlayersException() { super("You are alone in this room!");}
    public NotEnoughPlayersException(String s) {
        super(s);
    }
}
