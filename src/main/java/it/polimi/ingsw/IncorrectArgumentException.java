package it.polimi.ingsw;

public class IncorrectArgumentException extends Exception {
    public IncorrectArgumentException(){
        super("Incorrect argument");
    }

    public IncorrectArgumentException(String s){
        super(s);
    }
}
