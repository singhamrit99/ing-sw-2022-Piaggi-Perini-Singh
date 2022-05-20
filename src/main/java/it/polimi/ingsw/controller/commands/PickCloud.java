package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PickCloud implements Command {
    String playerCaller;
    int cloudTileID;

    public PickCloud(String playerCaller,int cloudTileID) {
        this.playerCaller=playerCaller;
        this.cloudTileID = cloudTileID;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPickCloud(playerCaller, cloudTileID);
    }
}
