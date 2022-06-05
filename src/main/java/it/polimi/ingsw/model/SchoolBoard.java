package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;

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
        entrance = new EnumMap<>(Colors.class);
        dining = new EnumMap<>(Colors.class);
        professorsTable = new ArrayList<>();
        coins = new EnumMap<>(Colors.class);

        if (numberOfPlayers == 3) {
            numberOfTowers = 6;
        } else {
            numberOfTowers = 8;
        }

        dining = StudentManager.createEmptyStudentsEnum();
        coins = StudentManager.createEmptyStudentsEnum();
    }

    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(entrance, studentsToAdd);
        if (newStudents != null) {
            setEntrance(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getEntrance(), studentsToRemove);
        if (newStudents != null) {
            setEntrance(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    public int moveStudents(EnumMap<Colors, Integer> studentsToMove) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getDining(), studentsToMove);
        int count = 0;

        if (newStudents != null) {
            removeStudents(studentsToMove);
            setDining(newStudents);
        } else {
            throw new NegativeValueException();
        }

        for (int i = 0; i < 3; i++) {
            for (Map.Entry<Colors, Integer> set : getDining().entrySet()) {
                if (set.getValue() >= 3) {
                    //se lo è controllo che il valore di coins sia stato messo già a 1, 2, 3
                    if (coins.get(set.getKey()) != set.getValue() / 3) {
                        //se è già fatto non faccio nulla altrimenti lo incremento
                        coins.put(set.getKey(), coins.get(set.getKey()) + 1);
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public void addProfessor(Colors professor) {
        professorsTable.add(professor);
    }

    public void removeProfessor(Colors professor) throws ProfessorNotFoundException {
        int index = professorsTable.indexOf(professor);
        if (index != -1) {
            professorsTable.remove(index);
            return;
        }

        throw new ProfessorNotFoundException();
    }

    public boolean hasEnoughStudents(EnumMap<Colors, Integer> students) throws IllegalArgumentException, NegativeValueException {
        EnumMap<Colors, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IllegalArgumentException("EnumMap is not correct");
                }
            } else {
                throw new NegativeValueException("EnumMap is not correct");
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

    public int getStudentsByColor(Colors student) throws IllegalArgumentException {
        if (dining.containsKey(student)) {
            return dining.get(student);
        }

        throw new IllegalArgumentException("student is not correct");
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

    public void removeDiningStudents(EnumMap<Colors, Integer> studentsToRemove) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getEntrance(), studentsToRemove);
        if (newStudents != null) {
            setDining(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    public void addStudentsDining(EnumMap<Colors, Integer> studentsToAddDining) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(entrance, studentsToAddDining);
        if (newStudents != null) {
            setDining(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }
}
