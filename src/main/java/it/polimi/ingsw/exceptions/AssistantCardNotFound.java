package it.polimi.ingsw.exceptions;

public class AssistantCardNotFound extends Exception {
    public AssistantCardNotFound() {
        super("The given card name is incorrect");
    }

    public AssistantCardNotFound(String s) {
        super(s);
    }
}
