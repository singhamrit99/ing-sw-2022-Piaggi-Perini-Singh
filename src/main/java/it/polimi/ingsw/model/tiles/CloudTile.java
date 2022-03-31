package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

public class CloudTile implements Tile {
    public String name;
    private EnumMap<Students, Integer> students;

    public CloudTile(String cloudname) {
        name = cloudname;
    }

    public EnumMap<Students, Integer> removeStudents() {
        EnumMap<Students, Integer> returnedStudents = students;
        for (Map.Entry<Students, Integer> studentType : students.entrySet()) {
            if (studentType.getValue() > 0) {
                studentType.setValue(0);
            }
        }
        return returnedStudents;
    }

    public void addStudents(EnumMap<Students, Integer> summedStudents) throws IncorrectArgumentException {
        EnumMap<Students, Integer> tmp = students;
        for (Map.Entry<Students, Integer> studentsNewEnumMap : summedStudents.entrySet()) {
            if (studentsNewEnumMap.getValue() >= 0) {
                if (tmp.containsKey(studentsNewEnumMap.getKey())) {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue() + tmp.get(studentsNewEnumMap.getKey()));
                } else {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue());
                }
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }
        }
        students = tmp;
    }

}
