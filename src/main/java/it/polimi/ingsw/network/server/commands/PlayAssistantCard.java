package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;

import java.io.Serializable;

public class PlayAssistantCard implements Command, Serializable {
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
    public void execute(Controller controller) throws IncorrectPlayerException, IncorrectArgumentException, AssistantCardNotFoundException, IncorrectStateException, NegativeValueException, AssistantCardAlreadyPlayed {
        controller.callPlayAssistantCard(playerCaller, assistantCardName);
    }
}
