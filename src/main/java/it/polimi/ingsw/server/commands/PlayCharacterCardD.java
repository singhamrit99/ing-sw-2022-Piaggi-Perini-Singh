package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;

public class PlayCharacterCardD implements Command {
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
    public void execute(Controller controller) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        controller.callPlayCharacterCard(characterCardID, choice);
    }
}
