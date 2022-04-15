package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class Bag {
    private EnumMap<Colors, Integer> students;
    private static Bag instance = new Bag();

    private Bag() {
        students = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            students.put(color, 0);
        }
    }

    public static Bag getInstance() {
        return instance;
    }

    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getStudents(), studentsToAdd);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getStudents(), studentsToRemove);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public EnumMap<Colors, Integer> drawStudents(int numberOfStudents) throws IncorrectArgumentException {
        int type, quantity;
        int studentType = 5;

        EnumMap<Colors, Integer> studentsDrawn = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            studentsDrawn.put(color, 0);
        }

        for (int i = 0; i < numberOfStudents; ) {
            type = (int) Math.floor(Math.random() * studentType);
            quantity = (int) Math.floor(Math.random() * ((numberOfStudents - i) - 1) + 1);

            if (getStudents().containsKey(Colors.getStudent(type))) {
                if (getStudents().get(Colors.getStudent(type)) >= quantity) {
                    studentsDrawn.put(Colors.getStudent(type), studentsDrawn.get(Colors.getStudent(type)) + quantity);
                    i += quantity;
                }
            }
        }
        removeStudents(studentsDrawn);
        return studentsDrawn;
    }

    public boolean hasEnoughStudents(int numberOfStudents) {
        int count = 0;
        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            count += set.getValue();
        }
        return count >= numberOfStudents;
    }

    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }
}
