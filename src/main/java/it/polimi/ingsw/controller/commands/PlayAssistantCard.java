package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayAssistantCard implements Command {
    String assistantCardName;

    public PlayAssistantCard(String assistantCardName) {
        this.assistantCardName = assistantCardName;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayAssistantCard(assistantCardName);
    }
}
