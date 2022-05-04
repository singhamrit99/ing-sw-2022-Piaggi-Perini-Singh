package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class MoveMotherNature implements Command {
    Controller controller;

    public MoveMotherNature(Controller controller, int distance) {
        this.controller = controller;
    }

    @Override
    public void execute() {controller.callMoveMotherNature();}
}
