package it.polimi.ingsw.exceptions;

public class RoomNotExistsException extends Exception{
    public RoomNotExistsException() { super("Room not exists");}
    public RoomNotExistsException(String s) {
        super(s);
    }
}
