package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;

import java.util.EnumMap;
import java.util.Map;

public class Cloud {
    public String name;
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
        EnumMap<Colors, Integer> returnedStudents = students;
        for (Map.Entry<Colors, Integer> studentType : students.entrySet()) {
            if (studentType.getValue() > 0) {
                studentType.setValue(0);
            }
        }
        return returnedStudents;
    }

    public void addStudents(EnumMap<Colors, Integer> summedStudents) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getStudents(), summedStudents);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
