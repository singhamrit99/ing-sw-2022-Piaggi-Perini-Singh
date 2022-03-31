package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class Bag {
    private HashMap<StudentDisc, Integer> students;
    private final int studentType = 5;

    public Bag(HashMap<StudentDisc, Integer> students) {
        this.students = students;
    }

    public Bag() {
        students = new HashMap<>();
    }

    //TODO aggiungere metodo per controllare n di pedine e ritorno sia se ne ho poche o nulle

    public void addStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getStudents();

        for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), set.getValue() + studentsDiscs.get(set.getKey()));
                } else {
                    studentsDiscs.put(set.getKey(), set.getValue());
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }

        }
        this.setStudents(studentsDiscs);
    }

    private void removeStudents(HashMap<StudentDisc, Integer> studentsToRemove) throws IncorrectArgumentException {
        HashMap<StudentDisc, Integer> studentsDiscs = this.getStudents();

        for (Map.Entry<StudentDisc, Integer> set : studentsToRemove.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    studentsDiscs.put(set.getKey(), studentsDiscs.get(set.getKey()) - set.getValue());
                } else {
                    throw new IncorrectArgumentException("HashMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        this.setStudents(studentsDiscs);
    }

    public HashMap<StudentDisc, Integer> drawStudents(int numberOfStudents) throws IncorrectArgumentException {
        int type, quantity;
        HashMap<StudentDisc, Integer> studentsDrawn = new HashMap<>();

        for (int i = 0; i < numberOfStudents; ) {
            type = (int) Math.floor(Math.random() * (studentType + 1));
            quantity = (int) Math.floor(Math.random() * (numberOfStudents - i) + 1);
            studentsDrawn.put(new StudentDisc(Colors.getColor(type)), quantity);
            i += quantity;
        }

        this.removeStudents(studentsDrawn);
        return studentsDrawn;
    }

    public boolean hasEnoughStudents(int numberOfStudents) throws IncorrectArgumentException {
        for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (getStudents().containsKey(set.getKey())) {
                    if (getStudents().get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IncorrectArgumentException("HashMap is not correct");
                }
            } else {
                throw new IncorrectArgumentException("HashMap is not correct");
            }
        }
        return true;
    }

    public void setStudents(HashMap<StudentDisc, Integer> students) {
        this.students = students;
    }

    public HashMap<StudentDisc, Integer> getStudents() {
        return students;
    }
}
