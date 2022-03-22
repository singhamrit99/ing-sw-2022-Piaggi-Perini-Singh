package it.polimi.ingsw.model.exceptions;

public class IncorrectStateException extends Exception {
    public IncorrectStateException(){
        super("Incorrect state");
    }

    public IncorrectStateException(String s){
        super(s);
    }
}
