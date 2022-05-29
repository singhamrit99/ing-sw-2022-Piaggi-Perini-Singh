package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;

import java.io.Serializable;

public class PlayCharacterCardA implements Command, Serializable {
    int characterCardID;
    String  playerCaller;
    public PlayCharacterCardA(String playerCaller, int ID) {
        this.playerCaller =playerCaller;
        this.characterCardID = ID;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        controller.callPlayCharacterCard(characterCardID);
    }
}
