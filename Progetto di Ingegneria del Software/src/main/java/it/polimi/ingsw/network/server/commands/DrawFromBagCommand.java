package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;

import java.io.Serializable;

public class DrawFromBagCommand implements Command, Serializable {

    String playerCaller;

    public DrawFromBagCommand(String playerCaller) {
        this.playerCaller = playerCaller;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException, IncorrectPlayerException {
        controller.callDrawFromBag(playerCaller);
    }
}
