package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;
import java.util.ArrayList;
import java.util.EnumMap;

public class MoveStudents implements Command {
    Controller controller;
    EnumMap<Colors, ArrayList<String>> students;

    public MoveStudents(Controller controller, EnumMap<Colors, ArrayList<String>> students) {
        this.controller = controller;
        this.students = students;
    }
    @Override
    public void execute() {controller.callMoveStudent(students);}
}
