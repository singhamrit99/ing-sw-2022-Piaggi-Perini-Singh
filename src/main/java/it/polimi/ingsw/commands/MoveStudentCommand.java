package it.polimi.ingsw.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.ArrayList;
import java.util.EnumMap;

public class MoveStudentCommand implements Commands{


    Controller controller;
    EnumMap<Colors, ArrayList<String>> students;

    public MoveStudentCommand(Controller controller, EnumMap<Colors, ArrayList<String>> students) {
        this.controller = controller;
        this.students = students;
    }

    @Override
    public void execute() {

        controller.CallMoveStudent(students);



    }
}
