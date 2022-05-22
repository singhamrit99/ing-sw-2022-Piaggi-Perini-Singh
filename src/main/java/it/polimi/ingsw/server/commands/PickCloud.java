package it.polimi.ingsw.server.commands;

import it.polimi.ingsw.controller.Controller;

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
    public void execute(Controller controller) {
        controller.callPickCloud(playerCaller, cloudTileID);
    }
}
