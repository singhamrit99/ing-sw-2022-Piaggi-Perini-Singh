package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;

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
    public void execute(Controller controller) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, MotherNatureLostException, IncorrectStateException {
        controller.callMoveMotherNature(playerCaller, distance);
    }
}
