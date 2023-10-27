package it.polimi.ingsw.exceptions;

public class UserNotInRoomException extends Exception{
    public UserNotInRoomException() { super("The user is not inside a room");}
    public UserNotInRoomException(String s) {
        super(s);
    }
}
