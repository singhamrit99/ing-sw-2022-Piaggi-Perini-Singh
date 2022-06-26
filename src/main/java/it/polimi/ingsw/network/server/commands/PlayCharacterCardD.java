package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;

import java.io.Serializable;

public class PlayCharacterCardD implements Command, Serializable {
    int characterCardID;
    int choice;
    String playerCaller;
    public PlayCharacterCardD(String playerCaller, int characterCardID, int choice) {
        this.playerCaller = playerCaller;
        this.characterCardID = characterCardID;
        this.choice = choice;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException {
        controller.callPlayCharacterCard(characterCardID, choice);
    }
}
