package it.polimi.ingsw.exceptions;

public class NameFieldException extends Exception{
    public NameFieldException() { super("Nickname field wrong");}
    public NameFieldException(String s) {
        super(s);
    }
}
