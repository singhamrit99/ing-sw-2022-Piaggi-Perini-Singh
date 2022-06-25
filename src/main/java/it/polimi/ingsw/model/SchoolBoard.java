package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDiningException;
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

    /**
     * School Board constructor
     *
     * @param numberOfPlayers number of players in the game.
     */
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

        entrance = StudentManager.createEmptyStudentsEnum();
        dining = StudentManager.createEmptyStudentsEnum();
        coins = StudentManager.createEmptyStudentsEnum();
    }

    /**
     * Method used to add students to the Entrance.
     *
     * @param studentsToAdd The EnumMap that contains the students that need to be added.
     * @throws NegativeValueException
     */
    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(entrance, studentsToAdd);
        if (newStudents != null) {
            setEntrance(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    /**
     * Removes the students from the entrance, such as when moving to the dining room, islands or character cards.
     *
     * @param studentsToRemove The EnumMap that contains the students that need to be added.
     * @throws NegativeValueException
     */
    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getEntrance(), studentsToRemove);
        if (newStudents != null) {
            setEntrance(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    /**
     * Method used to move students from the Entrance to the Dining Room and checks if coins are to be awarded
     *
     * @param studentsToMove The students that need to be moved to the Dining Room
     * @return The number of coins awarded (if any)
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
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

    /**
     * Method used to add a professor to the player's Professors Table
     *
     * @param professor The professor's color.
     */
    public void addProfessor(Colors professor) {
        professorsTable.add(professor);
    }

    /**
     * Method used to remove a professor from the player's Professors Table
     *
     * @param professor The professor's color.
     * @throws ProfessorNotFoundException Throws an exception if the index is in the invalid setup value.
     */
    public void removeProfessor(Colors professor) throws ProfessorNotFoundException {
        int index = professorsTable.indexOf(professor);
        if (index != -1) {
            professorsTable.remove(index);
            return;
        }

        throw new ProfessorNotFoundException();
    }

    /**
     * Method that checks if there are enough students to perform some actions.
     *
     * @param students The Student EnumMap to check.
     * @return true or false depending on the outcome
     * @throws IllegalArgumentException Thrown when the EnumMap is incorrect
     * @throws NegativeValueException   Thrown when some values in the EnumMap are <0.
     */
    public boolean hasEnoughStudents(EnumMap<Colors, Integer> students) throws IllegalArgumentException, NegativeValueException {
        EnumMap<Colors, Integer> studentsDiscs = this.getEntrance();

        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            if (set.getValue() >= 0) {
                if (studentsDiscs.containsKey(set.getKey())) {
                    if (studentsDiscs.get(set.getKey()) < set.getValue()) {
                        return false;
                    }
                } else {
                    throw new IllegalArgumentException("EnumMap is incorrect");
                }
            } else {
                throw new NegativeValueException("EnumMap is not correct");
            }
        }
        return true;
    }

    /**
     * Method used to take back towers after losing an island.
     *
     * @param towers number of towers to add back.
     */
    public void moveTowers(int towers) {
        numberOfTowers += towers;
    }

    /**
     * Returns the professors' table.
     *
     * @return Professor table.
     */
    public ArrayList<Colors> getProfessorsTable() {
        return professorsTable;
    }

    /**
     * Returns the number of towers.
     *
     * @return Number of towers.
     */
    public int getTowers() {
        return numberOfTowers;
    }

    /**
     * Given a student EnumMap it returns the number of students of a certain color.
     *
     * @param student The student enumMap
     * @return The number of students of that color
     * @throws IllegalArgumentException EnumMap doesn't contain the provided Color Key.
     */
    public int getStudentsByColor(Colors student) throws IllegalArgumentException {
        if (dining.containsKey(student)) {
            return dining.get(student);
        }

        throw new IllegalArgumentException("student is not correct");
    }

    /**
     * Checks if the player board has the professor of the given color.
     *
     * @param professor The color to check
     * @return true or false depending on the outcome
     */
    public boolean hasProfessorOfColor(Colors professor) {
        return professorsTable.contains(professor);
    }

    /**
     * Entrance setter.
     *
     * @param students Students to set the entrance to.
     */
    private void setEntrance(EnumMap<Colors, Integer> students) {
        this.entrance = students;
    }

    /**
     * Entrance getter.
     *
     * @return entrance
     */
    public EnumMap<Colors, Integer> getEntrance() {
        return entrance;
    }

    /**
     * Dining setter.
     *
     * @param students students
     */
    private void setDining(EnumMap<Colors, Integer> students) {
        this.dining = students;
    }

    /**
     * Dining room getter
     *
     * @return dining.
     */
    public EnumMap<Colors, Integer> getDining() {
        return this.dining;
    }

    /**
     * Removes students from Dining room
     *
     * @param studentsToRemove Students to be removed from Dining room.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void removeDiningStudents(EnumMap<Colors, Integer> studentsToRemove) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getDining(), studentsToRemove);
        if (newStudents != null) {
            setDining(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    /**
     * Adds students to Dining Room
     *
     * @param studentsToAddDining Students to be added to Dining room.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void addStudentsDining(EnumMap<Colors, Integer> studentsToAddDining) throws NegativeValueException, FullDiningException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(dining, studentsToAddDining);

        if (newStudents != null) {
            if (!checkColorsNum(newStudents, 10)) throw new FullDiningException();
        }

        if (newStudents != null) {
            setDining(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    private boolean checkColorsNum(EnumMap<Colors, Integer> studentsToCheck, int max) {
        for (Colors color : Colors.values()) {
            if (studentsToCheck.get(color) > max) {
                return false;
            }
        }

        return true;
    }
}
