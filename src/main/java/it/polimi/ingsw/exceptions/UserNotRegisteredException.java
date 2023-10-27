package it.polimi.ingsw.exceptions;

public class UserNotRegisteredException extends Exception{
    public UserNotRegisteredException() { super("User not registered");}
    public UserNotRegisteredException(String s) {
        super(s);
    }
}
