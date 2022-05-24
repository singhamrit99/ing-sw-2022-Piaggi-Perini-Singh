package it.polimi.ingsw.exceptions;

public class NotLeaderRoomException extends Exception{
    public NotLeaderRoomException() { super("Only leader can start the game");}
    public NotLeaderRoomException(String s) {
        super(s);
    }
}
