package it.polimi.ingsw.exceptions;

public class UserInRoomException extends Exception{
    public UserInRoomException() { super("The user is already inside a room");}
    public UserInRoomException(String s) {
        super(s);
    }
}
