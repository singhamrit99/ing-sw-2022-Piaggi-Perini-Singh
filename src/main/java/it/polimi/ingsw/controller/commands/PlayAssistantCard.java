package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayAssistantCard implements Command {
    String playerName;
    String assistantCardName;

    public PlayAssistantCard(String playerName,String assistantCardName) {
        this.playerName= playerName;
        this.assistantCardName = assistantCardName;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPlayAssistantCard(playerName, assistantCardName);
    }
}
