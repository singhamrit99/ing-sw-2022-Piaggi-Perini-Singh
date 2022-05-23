package it.polimi.ingsw.exceptions;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException() { super("User already exists");}
    public UserAlreadyExistsException(String s) {
        super(s);
    }
}
