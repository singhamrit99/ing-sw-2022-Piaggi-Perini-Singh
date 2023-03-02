package it.polimi.ingsw.exceptions;

public class RoomInGameException extends Exception{
    public RoomInGameException() { super("The room members are playing now");}
    public RoomInGameException(String s) {
        super(s);
    }
}
