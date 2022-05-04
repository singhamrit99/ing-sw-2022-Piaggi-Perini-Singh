package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class MoveMotherNature implements Command {
    int distance;

    public MoveMotherNature(int distance) {
        this.distance = distance;
    }

    @Override
    public void execute(Controller controller) {
        controller.callMoveMotherNature(distance);
    }
}
