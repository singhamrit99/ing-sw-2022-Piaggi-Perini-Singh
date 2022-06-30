package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.NegativeValueException;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Amrit
 */
public class Bag {
    private EnumMap<Colors, Integer> students;
    private static Bag instance = new Bag();

    private Bag() {
        students = new EnumMap<>(Colors.class);
        students = StudentManager.createEmptyStudentsEnum();
    }

    /**
     * Getter method for bag instance
     *
     * @return this bag instance.
     */
    public static Bag getInstance() {
        return instance;
    }

    /**
     * Add students to the bag, in the beginning of the game and following some Character Card calls.
     *
     * @param studentsToAdd the students to add, in EnumMap form.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void addStudents(EnumMap<Colors, Integer> studentsToAdd) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(getStudents(), studentsToAdd);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    /**
     * Remove students from the bag following Character Card calls and Cloud refills.
     *
     * @param studentsToRemove The students to remove, in EnumMap form.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void removeStudents(EnumMap<Colors, Integer> studentsToRemove) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.removeStudent(getStudents(), studentsToRemove);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    /**
     * Method used to draw students from the bag, mainly for Cloud refills and some Character card calls.
     *
     * @param numberOfStudents the number of students to draw from the bag, expressed as an integer.
     * @return an EnumMap of Colors, Integer with the randomly picked students.
     * @throws NegativeValueException
     */
    public EnumMap<Colors, Integer> drawStudents(int numberOfStudents) throws NegativeValueException, IncorrectArgumentException {
        int type;
        int studentType = 5;

        EnumMap<Colors, Integer> studentsDrawn = StudentManager.createEmptyStudentsEnum();
        Random random = new Random();

        for (int i = 0; i < numberOfStudents; ) {
            type = random.nextInt(studentType);

            if (getStudents().containsKey(Colors.getStudent(type))) {
                studentsDrawn.put(Colors.getStudent(type), studentsDrawn.get(Colors.getStudent(type)) + 1);
                i++;
            }
        }
        removeStudents(studentsDrawn);
        return studentsDrawn;
    }

    /**
     * Method used to check if the bag has enough students for a requested task.
     *
     * @param numberOfStudents the number of students we're testing against.
     * @return true if we have enough students, false if we don't
     */
    public boolean hasEnoughStudents(int numberOfStudents) {
        int count = 0;
        for (Map.Entry<Colors, Integer> set : students.entrySet()) {
            count += set.getValue();
        }
        return count >= numberOfStudents;
    }

    /**
     * Setter for students parameter
     *
     * @param students the new value of students enumMap
     */
    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    /**
     * Getter for students
     *
     * @return students
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }
}
