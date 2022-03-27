package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentDisc;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

public class CloudTile implements Tile {
    public String name;
    private HashMap<StudentDisc, Integer> students;

    public CloudTile(String cloudname) {
        name = cloudname;
    }

    public HashMap<StudentDisc, Integer> removeStudents() {
        HashMap<StudentDisc, Integer> returnedStudents = students;
        for (Map.Entry<StudentDisc, Integer> studentType : students.entrySet()){
            if (studentType.getValue() > 0) {
                studentType.setValue(0);
            }
        }
        return returnedStudents;
    }

    public void addStudents(HashMap<StudentDisc, Integer> students) {
    }

}
