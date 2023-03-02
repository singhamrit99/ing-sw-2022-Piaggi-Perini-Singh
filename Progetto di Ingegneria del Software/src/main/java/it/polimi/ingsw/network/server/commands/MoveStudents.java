package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;

import java.io.Serializable;

public class MoveStudents implements Command, Serializable {
    String playerCaller;
    Colors color;
    String dest;

    public MoveStudents(String playerCaller, Colors color, String dest) {
        this.playerCaller = playerCaller;
        this.color = color;
        this.dest = dest;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }

    @Override
    public void execute(Controller controller) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, IncorrectStateException {
        controller.callMoveStudent(playerCaller, color, dest);
    }
}
