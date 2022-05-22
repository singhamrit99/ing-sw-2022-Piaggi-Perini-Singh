package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;

public class PlayAssistantCard implements Command {
    String playerCaller;
    String assistantCardName;

    public PlayAssistantCard(String playerCaller,String assistantCardName) {
        this.playerCaller= playerCaller;
        this.assistantCardName = assistantCardName;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) {
        controller.callPlayAssistantCard(playerCaller, assistantCardName);
    }
}
