package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

public class CloudTile implements Tile {
    public String name;
    private HashMap<Colors, Integer> students;

    public CloudTile(String cloudname) {
        name = cloudname;
    }

    public HashMap<Colors, Integer> removeStudents() {
        HashMap<Colors, Integer> returnedStudents = students;
        for (Map.Entry<Colors, Integer> studentType : students.entrySet()) {
            if (studentType.getValue() > 0) {
                studentType.setValue(0);
            }
        }
        return returnedStudents;
    }

    public void addStudents(HashMap<Colors, Integer> summedStudents) throws IncorrectArgumentException {
        HashMap<Colors, Integer> tmp = students;
        for (Map.Entry<Colors, Integer> studentsNewHashMap : summedStudents.entrySet()) {
            if (studentsNewHashMap.getValue() >= 0) {
                if (tmp.containsKey(studentsNewHashMap.getKey())) {
                    tmp.put(studentsNewHashMap.getKey(), studentsNewHashMap.getValue() + tmp.get(studentsNewHashMap.getKey()));
                } else {
                    tmp.put(studentsNewHashMap.getKey(), studentsNewHashMap.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        students = tmp;
    }

}
