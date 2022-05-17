package it.polimi.ingsw.exceptions;

public class IncorrectPlayerException extends Exception {
    public IncorrectPlayerException(){
        super("Incorrect argument");
    }

    public IncorrectPlayerException(String s){
        super(s);
    }
}
