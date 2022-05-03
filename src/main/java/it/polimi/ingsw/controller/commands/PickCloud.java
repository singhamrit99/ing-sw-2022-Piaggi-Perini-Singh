package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;

public class PickCloud implements Command {

    Controller controller;
    int cloudTileID;

    public PickCloud(Controller controller, int cloudTileID) {
        this.controller = controller;
        this.cloudTileID=cloudTileID;
    }
    @Override
    public void execute() {controller.callPickCloud(cloudTileID);}
}
