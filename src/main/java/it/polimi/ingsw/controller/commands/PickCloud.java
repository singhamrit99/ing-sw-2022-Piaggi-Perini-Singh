package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PickCloud implements Command {
    int cloudTileID;

    public PickCloud(int cloudTileID) {
        this.cloudTileID = cloudTileID;
    }

    @Override
    public void execute(Controller controller) {
        controller.callPickCloud(cloudTileID);
    }
}
