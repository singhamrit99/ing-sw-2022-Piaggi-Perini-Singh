package it.polimi.ingsw.exceptions;

public class AssistantCardAlreadyPlayed extends Exception {
    public AssistantCardAlreadyPlayed() {
        super("The given card name is incorrect");
    }

    public AssistantCardAlreadyPlayed(String s) {
        super(s);
    }
}
