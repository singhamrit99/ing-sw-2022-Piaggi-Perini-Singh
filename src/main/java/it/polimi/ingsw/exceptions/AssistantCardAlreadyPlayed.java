package it.polimi.ingsw.exceptions;

public class AssistantCardAlreadyPlayed extends Throwable {
    public AssistantCardAlreadyPlayed() {
        super("The given card name is incorrect");
    }

    public AssistantCardAlreadyPlayed(String s) {
        super(s);
    }
}
