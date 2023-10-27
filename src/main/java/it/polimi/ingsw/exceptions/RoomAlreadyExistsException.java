package it.polimi.ingsw.exceptions;

public class RoomAlreadyExistsException extends Exception{
    public RoomAlreadyExistsException() { super("Room already exists");}
    public RoomAlreadyExistsException(String s) {
        super(s);
    }
}
