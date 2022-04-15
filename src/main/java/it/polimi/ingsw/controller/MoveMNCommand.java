package it.polimi.ingsw.controller;

public class MoveMNCommand implements Commands {
    Controller controller;

    public MoveMNCommand(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {controller.callMoveMotherNature();}
}
