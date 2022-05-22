package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;


public interface Command {

    String getCaller();
    void execute(Controller controller);
}
