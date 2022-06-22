package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.exceptions.IncorrectStateException;
import it.polimi.ingsw.exceptions.NegativeValueException;

import java.io.Serializable;

public class PickCloud implements Command, Serializable {
    String playerCaller;
    String cloudTileID;

    public PickCloud(String playerCaller,String cloudTileID) {
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
