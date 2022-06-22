package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 */
public class Cloud {
    final private String name;
    private EnumMap<Colors, Integer> students;

    public Cloud(String name) {
        this.name = name;
        students = new EnumMap<>(Colors.class);
        students = StudentManager.createEmptyStudentsEnum();
    }

    public String getName() {
        return name;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    public EnumMap<Colors, Integer> drawStudents() {
        EnumMap<Colors, Integer> returnedStudents = StudentManager.createEmptyStudentsEnum();

        for (Colors color: Colors.values()){
            returnedStudents.put(color, students.get(color));
        }

        setStudents(StudentManager.createEmptyStudentsEnum());
        return returnedStudents;
    }

    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
