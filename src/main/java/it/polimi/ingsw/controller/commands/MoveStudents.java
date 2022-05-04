package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.ArrayList;
import java.util.EnumMap;

public class MoveStudents implements Command {
    EnumMap<Colors, ArrayList<String>> students;

    public MoveStudents(EnumMap<Colors, ArrayList<String>> students) {
        this.students = students;
    }

    @Override
    public void execute(Controller controller) {
        controller.callMoveStudent(students);
    }
}
