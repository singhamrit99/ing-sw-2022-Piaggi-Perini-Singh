package it.polimi.ingsw.exceptions;

public class AssistantCardNotFoundException extends Exception {
    public AssistantCardNotFoundException() {
        super("The given card name is incorrect");
    }

    public AssistantCardNotFoundException(String s) {
        super(s);
    }
}
