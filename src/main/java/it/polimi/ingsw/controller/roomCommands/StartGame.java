package it.polimi.ingsw.controller.roomCommands;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Room;

public class StartGame implements  RoomCommand{
    @Override
    public void execute(Room room) throws NegativeValueException, IncorrectArgumentException {
        room.startGame();

    }
}
