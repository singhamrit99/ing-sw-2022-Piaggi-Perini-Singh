package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class SchoolBoard {
    private EnumMap<Colors, Integer> entrance;
    private EnumMap<Colors, Integer> dining;
    private ArrayList<Colors> professorsTable;
    private int numberOfTowers;

    public SchoolBoard(int numberOfPlayers) {
        this.entrance = new EnumMap(Colors.class);
        this.dining = new EnumMap(Colors.class);
        this.professorsTable = new ArrayList<>();

        if (numberOfPlayers == 3) {
            numberOfTowers = 6;
        } else {
            numberOfTowers = 8;
        }
    }

    public void addStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = getEntrance();

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
        setEntrance(studentsDiscs);
    }

    public void removeStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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

    public void moveStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = getDining();

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
        removeStudents(studentsDiscs);
        setDining(studentsDiscs);
    }

    public void addProfessor(Colors student) throws IncorrectArgumentException {
        professorsTable.add(student);
    }

    public void removeProfessor(Colors student) throws IncorrectArgumentException {

        int index = professorsTable.indexOf(student);
        if (index != -1) {
            professorsTable.remove(index);
        }

        throw new IncorrectArgumentException();
    }

    public boolean hasEnoughStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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

    private void setEntrance(EnumMap<Colors, Integer> studentsDiscs) {
        this.entrance = studentsDiscs;
    }

    private EnumMap<Colors, Integer> getEntrance() {
        return entrance;
    }

    private void setDining(EnumMap<Colors, Integer> studentsDiscs) {
        this.dining = studentsDiscs;
    }

    private EnumMap<Colors, Integer> getDining() {
        return this.dining;
    }

    public ArrayList<Colors> getProfessorsTable() {
        return professorsTable;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public int getStudentsByColor(Colors student) throws IncorrectArgumentException {
        if (dining.containsKey(student)) {
            return dining.get(student);
        }
        throw new IncorrectArgumentException();
    }

    public boolean hasProfessorOfColor(Colors student) throws IncorrectArgumentException {
        return professorsTable.contains(student);
    }
}
