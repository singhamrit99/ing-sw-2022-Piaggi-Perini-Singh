package it.polimi.ingsw.controller.roomCommands;


import it.polimi.ingsw.server.Room;

public class GetRoomInfo implements RoomCommand {

    String playerCaller;

    public GetRoomInfo(String playerCaller) {
        this.playerCaller = playerCaller;
    }
    @Override
    public void execute(Room room)
    {

    }

    }

