package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class SchoolBoard {
    private EnumMap<Students, Integer> entrance;
    private EnumMap<Students, Integer> dining;
    private ArrayList<Students> professorsTable;
    private int numberOfTowers;

    public SchoolBoard(int numberOfPlayers) {
        this.entrance = new EnumMap(Students.class);
        this.dining = new EnumMap(Students.class);
        this.professorsTable = new ArrayList<>();

        if (numberOfPlayers == 3) {
            numberOfTowers = 6;
        } else {
            numberOfTowers = 8;
        }
    }

    public void addStudents(EnumMap<Students, Integer> students) throws IncorrectArgumentException {
        EnumMap<Students, Integer> studentsDiscs = getEntrance();

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
        setEntrance(studentsDiscs);
    }

    public void removeStudents(EnumMap<Students, Integer> students) throws IncorrectArgumentException {
        EnumMap<Students, Integer> studentsDiscs = getEntrance();

        for (Map.Entry<Students, Integer> set : students.entrySet()) {
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
        setEntrance(studentsDiscs);
    }

    public void moveStudents(EnumMap<Students, Integer> students) throws IncorrectArgumentException {
        EnumMap<Students, Integer> studentsDiscs = getDining();

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
        removeStudents(studentsDiscs);
        setDining(studentsDiscs);
    }

    public void addProfessor(Students student) throws IncorrectArgumentException {
        professorsTable.add(student);
    }

    public void removeProfessor(Students student) throws IncorrectArgumentException {

        int index = professorsTable.indexOf(student);
        if (index != -1) {
            professorsTable.remove(index);
        }

        throw new IncorrectArgumentException();
    }

    public boolean hasEnoughStudents(EnumMap<Students, Integer> students) throws IncorrectArgumentException {
        EnumMap<Students, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<Students, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
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

    public void moveTowers(int towers) {
        this.numberOfTowers += towers;
    }

    private void setEntrance(EnumMap<Students, Integer> studentsDiscs) {
        this.entrance = studentsDiscs;
    }

    private EnumMap<Students, Integer> getEntrance() {
        return entrance;
    }

    private void setDining(EnumMap<Students, Integer> studentsDiscs) {
        this.dining = studentsDiscs;
    }

    private EnumMap<Students, Integer> getDining() {
        return this.dining;
    }

    public ArrayList<Students> getProfessorsTable() {
        return professorsTable;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public int getStudentsByColor(Students student) throws IncorrectArgumentException {
        if (dining.containsKey(student)) {
            return dining.get(student);
        }
        throw new IncorrectArgumentException();
    }

    public boolean hasProfessorOfColor(Students student) throws IncorrectArgumentException {
        return professorsTable.contains(student);
    }
}
