package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayAssistantCard implements Command {
    int assistantCardID;

    public PlayAssistantCard(int assistantCardID) {
        this.assistantCardID = assistantCardID;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayAssistantCard(assistantCardID);
    }
}
