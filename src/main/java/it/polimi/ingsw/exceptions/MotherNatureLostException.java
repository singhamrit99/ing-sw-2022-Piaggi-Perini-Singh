package it.polimi.ingsw.exceptions;

public class MotherNatureLostException extends Exception {
    public MotherNatureLostException(){
        super("Incorrect argument");
    }

    public MotherNatureLostException(String s){
        super(s);
    }
}
