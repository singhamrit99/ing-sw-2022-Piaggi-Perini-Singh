package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;

import java.io.Serializable;

public class PlayCharacterCardB implements Command, Serializable {
    int characterCardID;
    int student;
    int island;

    String playerCaller;
    public PlayCharacterCardB(String playerCaller, int ID, int student, int island) {
        this.playerCaller = playerCaller;
        this.characterCardID = ID;
        this.student = student;
        this.island = island;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }

    @Override
    public void execute(Controller controller) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        controller.callPlayCharacterCard(characterCardID, student, island);
    }
}
