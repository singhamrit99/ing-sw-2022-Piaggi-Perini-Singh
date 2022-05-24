package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.AssistantCardNotFoundException;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.exceptions.IncorrectStateException;

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
    public void execute(Controller controller) throws IncorrectPlayerException, IncorrectArgumentException, AssistantCardNotFoundException, IncorrectStateException {
        controller.callPlayAssistantCard(playerCaller, assistantCardName);
    }
}
