package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class SchoolBoard {
    private HashMap<Colors, Integer> entrance;
    private HashMap<Colors, Integer> dining;
    private ArrayList<Colors> professorsTable;
    private int numberOfTowers;

    public SchoolBoard(int numberOfPlayers) {
        this.entrance = new HashMap<>();
        this.dining = new HashMap<>();
        this.professorsTable = new ArrayList<>();

        if (numberOfPlayers == 3) {
            numberOfTowers = 6;
        } else {
            numberOfTowers = 8;
        }
    }

    public void addStudents(HashMap<Colors, Integer> students) throws IncorrectArgumentException {
        HashMap<Colors, Integer> studentsDiscs = getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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
        setEntrance(studentsDiscs);
    }

    public void removeStudents(HashMap<Colors, Integer> students) throws IncorrectArgumentException {
        HashMap<Colors, Integer> studentsDiscs = getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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
        setEntrance(studentsDiscs);
    }

    public void moveStudents(HashMap<Colors, Integer> students) throws IncorrectArgumentException {
        HashMap<Colors, Integer> studentsDiscs = getDining();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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
        removeStudents(studentsDiscs);
        setDining(studentsDiscs);
    }

    public void addProfessor(Colors color) throws IncorrectArgumentException {
        professorsTable.add(color);
    }

    public void removeProfessor(Colors color) throws IncorrectArgumentException {

        int index = professorsTable.indexOf(color);
        if (index != -1) {
            professorsTable.remove(index);
        }

        throw new IncorrectArgumentException();
    }

    public boolean hasEnoughStudents(HashMap<Colors, Integer> students) throws IncorrectArgumentException {
        HashMap<Colors, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
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

    public void moveTowers(int towers) {
        this.numberOfTowers += towers;
    }

    private void setEntrance(HashMap<Colors, Integer> studentsDiscs) {
        this.entrance = studentsDiscs;
    }

    private HashMap<Colors, Integer> getEntrance() {
        return entrance;
    }

    private void setDining(HashMap<Colors, Integer> studentsDiscs) {
        this.dining = studentsDiscs;
    }

    private HashMap<Colors, Integer> getDining() {
        return this.dining;
    }

    public ArrayList<Colors> getProfessorsTable() {
        return professorsTable;
    }

    public int getTowers() {
        return numberOfTowers;
    }

    public int getStudentsByColor(Colors color) throws IncorrectArgumentException {
        if (dining.containsKey(color)) {
            return dining.get(color);
        }
        throw new IncorrectArgumentException();
    }

    public boolean hasProfessorOfColor(Colors color) throws IncorrectArgumentException {
        return professorsTable.contains(color);
    }
}
