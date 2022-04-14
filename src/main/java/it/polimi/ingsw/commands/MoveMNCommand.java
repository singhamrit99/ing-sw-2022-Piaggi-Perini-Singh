package it.polimi.ingsw.commands;

import it.polimi.ingsw.controller.Controller;

public class MoveMNCommand implements Commands{
    Controller controller;

    public MoveMNCommand(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {

    controller.CallMoveMotherNature();


    }
}
