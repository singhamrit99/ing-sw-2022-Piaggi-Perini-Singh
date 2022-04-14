package it.polimi.ingsw.controller;

public class PickCloudCommand implements Commands {

    Controller controller;
    int cloudTileID;

    public PickCloudCommand(Controller controller, int cloudTileID) {
        this.controller = controller;
        this.cloudTileID=cloudTileID;
    }

    @Override
    public void execute() {controller.CallPickCloud(cloudTileID);}
}
