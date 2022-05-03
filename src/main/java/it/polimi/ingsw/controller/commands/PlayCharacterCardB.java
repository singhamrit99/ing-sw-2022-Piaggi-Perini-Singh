package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayCharacterCardB implements Command {

    Controller controller;
    int assistantCardID;

    public PlayCharacterCardB(Controller controller, int assistantCardID) {
        this.controller = controller;
        this.assistantCardID = assistantCardID;
    }

    @Override
    public void execute() {
        controller.callPlayAssistantCard(assistantCardID); //TODO
    }
}
