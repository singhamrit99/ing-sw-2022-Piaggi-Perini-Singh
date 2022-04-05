package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class SchoolBoard {
    private EnumMap<Colors, Integer> entrance;
    private EnumMap<Colors, Integer> dining;
    private EnumMap<Colors, Integer> coins;
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

        coins = new EnumMap<Colors, Integer>(Colors.class);
        for (Colors colors : Colors.values()) {
            coins.put(colors, 0);
        }
    }

    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getEntrance(), studentsToAdd);
        if (newStudents != null) {
            setEntrance(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getEntrance(), studentsToRemove);
        if (newStudents != null) {
            setEntrance(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public int moveStudents(EnumMap<Colors, Integer> studentsToMove) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getDining(), studentsToMove);
        int count = 0;

        if (newStudents != null) {
            removeStudents(studentsToMove);
            setDining(newStudents);
        } else {
            throw new IncorrectArgumentException();
        }

        /*Muovo 3 studenti di un tipo RED = 3               RED = 4             RED = 6
                                        cRED = 0 cRed = 1   cRED = 0 cRED = 1   cRED = 1 cRED = 2
          si        si      si
          RED, 1    RED, 1  non fa nulla
         */
        for (Map.Entry<Colors, Integer> set : getDining().entrySet()) {
            if (set.getValue() >= 3) {
                //se lo è controllo che il valore di coins sia stato messo già a 1, 2, 3
                if (coins.get(set.getKey()) != set.getValue() / 3) {
                    //se è già fatto non faccio nulla altrimenti lo incremento
                    coins.put(set.getKey(), set.getValue() / 3);
                    count++;
                }
            }
        }

        return count;
    }

    public void addProfessor(Colors professor) {
        professorsTable.add(professor);
    }

    public void removeProfessor(Colors professor) throws IncorrectArgumentException {
        int index = professorsTable.indexOf(professor);
        if (index != -1) {
            professorsTable.remove(index);
            return;
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
        numberOfTowers += towers;
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

    public boolean hasProfessorOfColor(Colors professor) {
        return professorsTable.contains(professor);
    }

    private void setEntrance(EnumMap<Colors, Integer> students) {
        this.entrance = students;
    }

    public EnumMap<Colors, Integer> getEntrance() {
        return entrance;
    }

    private void setDining(EnumMap<Colors, Integer> students) {
        this.dining = students;
    }

    public EnumMap<Colors, Integer> getDining() {
        return this.dining;
    }
}
