package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public interface Command {

    void execute(Controller controller);
}
