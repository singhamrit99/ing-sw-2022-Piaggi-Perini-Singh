package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;

public class MoveMotherNature implements Command {
    String playerCaller;
    int distance;
    public MoveMotherNature(String playerCaller, int distance) {

        this.playerCaller = playerCaller;
        this.distance = distance;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }

    @Override
    public void execute(Controller controller) {
        controller.callMoveMotherNature(playerCaller, distance);
    }
}
