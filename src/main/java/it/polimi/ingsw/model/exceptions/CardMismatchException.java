package it.polimi.ingsw.model.exceptions;

public class CardMismatchException extends Exception{
    public CardMismatchException(){ super("Card value and type do not match");}
}
