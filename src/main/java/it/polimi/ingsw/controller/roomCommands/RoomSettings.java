package it.polimi.ingsw.controller.roomCommands;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Room;

public class RoomSettings implements RoomCommand {

    String playerCaller;

    public RoomSettings(String playerCaller) {
        this.playerCaller = playerCaller;
    }
    @Override
    public void execute(Room room) {



    }
}
