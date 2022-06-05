package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Colors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

public class MoveStudents implements Command, Serializable {
    String playerCaller;
    EnumMap<Colors, ArrayList<String>> students;

    public MoveStudents(String playerCaller, EnumMap<Colors, ArrayList<String>> students) {
        this.playerCaller= playerCaller;
        this.students = students;
    }

    @Override
    public String getCaller() {
        return playerCaller;
    }
    @Override
    public void execute(Controller controller) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, IncorrectStateException {
        controller.callMoveStudent(playerCaller, students);
    }
}
