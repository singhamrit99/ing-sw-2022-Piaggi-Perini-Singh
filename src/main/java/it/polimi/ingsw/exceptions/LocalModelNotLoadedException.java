package it.polimi.ingsw.exceptions;

public class LocalModelNotLoadedException extends Exception{
    public LocalModelNotLoadedException() { super("Critical Error! Local Model not loaded.");}
    public LocalModelNotLoadedException(String s) {
        super(s);
    }
}
