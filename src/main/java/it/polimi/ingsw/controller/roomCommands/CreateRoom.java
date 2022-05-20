package it.polimi.ingsw.controller.roomCommands;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Room;


public class CreateRoom implements RoomCommand {
    String playerCaller;

    public CreateRoom(String playerCaller) {
        this.playerCaller = playerCaller;
    }

    @Override
    public void execute(Room room) {

    }
}
