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
    private final int studentType = 5;

    public Bag(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    public Bag() {
        students = new EnumMap(Colors.class);
    }

    //TODO aggiungere metodo per controllare n di pedine e ritorno sia se ne ho poche o nulle

    public void addStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = getStudents();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), set.getValue() + studentsDiscs.get(set.getKey()));
                } else {
                    studentsDiscs.put(set.getKey(), set.getValue());
                }
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }

        }
        setStudents(studentsDiscs);
    }

    private void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = getStudents();

        for (Map.Entry<Colors, Integer> set : studentsToRemove.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), studentsDiscs.get(set.getKey()) - set.getValue());
                } else {
                    throw new IncorrectArgumentException("EnumMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }
        }
        setStudents(studentsDiscs);
    }

    public EnumMap<Colors, Integer> drawStudents(int numberOfStudents) throws IncorrectArgumentException {
        int type, quantity;
        EnumMap<Colors, Integer> studentsDrawn = new EnumMap(Colors.class);

        for (int i = 0; i < numberOfStudents; ) {
            type = (int) Math.floor(Math.random() * (studentType + 1));
            quantity = (int) Math.floor(Math.random() * (numberOfStudents - i) + 1);
            studentsDrawn.put(Colors.getStudent(type), quantity);
            i += quantity;
        }

        removeStudents(studentsDrawn);
        return studentsDrawn;
    }

    public boolean hasEnoughStudents(int numberOfStudents) throws IncorrectArgumentException {
        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (getStudents().containsKey(set.getKey())) {
                    if (getStudents().get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IncorrectArgumentException("EnumMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }
        }
        return true;
    }

    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }
}
