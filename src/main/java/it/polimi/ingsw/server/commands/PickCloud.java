package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.exceptions.IncorrectStateException;
import it.polimi.ingsw.exceptions.NegativeValueException;

public class PickCloud implements Command {
    String playerCaller;
    int cloudTileID;

    public PickCloud(String playerCaller,int cloudTileID) {
        this.playerCaller=playerCaller;
        this.cloudTileID = cloudTileID;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, IncorrectStateException {
        controller.callPickCloud(playerCaller, cloudTileID);
    }
}
