package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;


public interface Command {

    String getCaller();
    void execute(Controller controller) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, MotherNatureLostException, IncorrectStateException, ProfessorNotFoundException, NotEnoughCoinsException, AssistantCardNotFoundException;
}
