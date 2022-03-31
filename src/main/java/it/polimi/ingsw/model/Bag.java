package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class Bag {
    private EnumMap<Students, Integer> students;
    private final int studentType = 5;

    public Bag(EnumMap<Students, Integer> students) {
        this.students = students;
    }

    public Bag() {
        students = new EnumMap(Students.class);
    }

    //TODO aggiungere metodo per controllare n di pedine e ritorno sia se ne ho poche o nulle

    public void addStudents(EnumMap<Students, Integer> students) throws IncorrectArgumentException {
        EnumMap<Students, Integer> studentsDiscs = getStudents();

        for (Map.Entry<Students, Integer> set : students.entrySet()) {
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

    private void removeStudents(EnumMap<Students, Integer> studentsToRemove) throws IncorrectArgumentException {
        EnumMap<Students, Integer> studentsDiscs = getStudents();

        for (Map.Entry<Students, Integer> set : studentsToRemove.entrySet()) {
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

    public EnumMap<Students, Integer> drawStudents(int numberOfStudents) throws IncorrectArgumentException {
        int type, quantity;
        EnumMap<Students, Integer> studentsDrawn = new EnumMap(Students.class);

        for (int i = 0; i < numberOfStudents; ) {
            type = (int) Math.floor(Math.random() * (studentType + 1));
            quantity = (int) Math.floor(Math.random() * (numberOfStudents - i) + 1);
            studentsDrawn.put(Students.getStudent(type), quantity);
            i += quantity;
        }

        removeStudents(studentsDrawn);
        return studentsDrawn;
    }

    public boolean hasEnoughStudents(int numberOfStudents) throws IncorrectArgumentException {
        for (Map.Entry<Students, Integer> set : students.entrySet()) {
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

    public void setStudents(EnumMap<Students, Integer> students) {
        this.students = students;
    }

    public EnumMap<Students, Integer> getStudents() {
        return students;
    }
}
