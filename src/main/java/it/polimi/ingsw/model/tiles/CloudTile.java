package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

public class CloudTile implements Tile {
    public String name;
    private EnumMap<Colors, Integer> students;

    public CloudTile(String name) {
        this.name = name;
        this.students= new EnumMap<>(Colors.class);
        for (Colors color: Colors.values()) {
            students.put(color, 0);
        }

    }

    public String getName() {
        return name;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    public EnumMap<Colors, Integer> removeStudents() {
        EnumMap<Colors, Integer> returnedStudents = students;
        for (Map.Entry<Colors, Integer> studentType : students.entrySet()) {
            if (studentType.getValue() > 0) {
                studentType.setValue(0);
            }
        }
        return returnedStudents;
    }

    public void addStudents(EnumMap<Colors, Integer> summedStudents) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> tmp = students;
        for (Map.Entry<Colors, Integer> studentsNewEnumMap : summedStudents.entrySet()) {
            if (studentsNewEnumMap.getValue() >= 0) {
                //if (tmp.containsKey(studentsNewEnumMap.getKey())) {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue() + tmp.get(studentsNewEnumMap.getKey()));
                /*} else {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue());
                }*/
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }
        }
        students = tmp;
    }

}
