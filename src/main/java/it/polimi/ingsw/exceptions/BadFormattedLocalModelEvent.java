package it.polimi.ingsw.exceptions;

public class BadFormattedLocalModelEvent extends Exception{
    public BadFormattedLocalModelEvent() { super("Received a bad formatted event update.");}
    public BadFormattedLocalModelEvent(String s) {
        super(s);
    }
}
