package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.roomCommands.RoomCommand;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.server.Room;

public class RoomCommandInvoker {

    Room room;

    public RoomCommandInvoker(Room room) {
        this.room=room;
    }

    public void executeCommand(RoomCommand command) throws NegativeValueException, IncorrectArgumentException {
        command.execute(room);
    }
}
