package it.polimi.ingsw.exceptions;

public class BadFormattedLocalModelException extends Exception{
    public BadFormattedLocalModelException() { super("Received a bad formatted event update.");}
    public BadFormattedLocalModelException(String s) {
        super(s);
    }
}
