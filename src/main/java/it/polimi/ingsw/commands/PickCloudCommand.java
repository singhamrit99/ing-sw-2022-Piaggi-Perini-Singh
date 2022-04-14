package it.polimi.ingsw.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.tiles.CloudTile;

public class PickCloudCommand implements Commands{

    Controller controller;
    int cloudTileID;

    public PickCloudCommand(Controller controller, int cloudTileID) {
        this.controller = controller;
        this.cloudTileID=cloudTileID;
    }

    @Override
    public void execute() {
        controller.CallPickCloud(cloudTileID);
    }
}
