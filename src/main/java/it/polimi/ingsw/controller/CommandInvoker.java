package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;

public class CommandInvoker {
    Controller controller;

    public CommandInvoker(Controller controller) {
        this.controller = controller;
    }

    public void executeCommand(Command command)
    {
        command.execute(controller);
    }
}
