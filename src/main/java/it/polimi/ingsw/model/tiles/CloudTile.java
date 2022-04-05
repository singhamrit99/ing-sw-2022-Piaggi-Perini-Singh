package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

public class CloudTile{
    public String name;
    private EnumMap<Colors, Integer> students;

    public CloudTile(String name) {
        this.name = name;
        this.students = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            students.put(color, 0);
        }

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

    public void addStudents(EnumMap<Colors, Integer> summedStudents) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getStudents(), summedStudents);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
