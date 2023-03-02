package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.exceptions.NegativeValueException;

import java.util.EnumMap;

public class Island {
    private String name;
    private EnumMap<Colors, Integer> students;
    private int numberOfTowers;
    private Towers towersColor;
    private boolean hasMotherNature;
    private boolean hasNoEntryTile;

    /**
     * Islant tile constructor. Most fields here are initialized to their default values.
     * @param islandName The island Name pulled from the JSON.
     */
    public Island(String islandName) {
        name = islandName;
        hasMotherNature = false;
        towersColor = Towers.WHITE; //this value is ignored if numberOfTowers=0
        numberOfTowers = 0;
        hasNoEntryTile = false;
        students = new EnumMap<>(Colors.class);
        students = StudentManager.createEmptyStudentsEnum();
    }

    /**
     * Getter method for name field.
     * @return name of the island.
     */
    public String getName() {
        return name;
    }
    /**
     * Getter method for towers Color field.
     * @return name of the color of towers on the island (if any are present)
     */
    public Towers getTowersColor() {
        return towersColor;
    }

    /**
     * Setter method for the island's tower color
     * @param newColor the new color after a conquest.
     */
    public void setTowersColor(Towers newColor) {
        towersColor = newColor;
    }

    /**
     * Getter method for students on the island.
     * @return students on the island.
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    /**
     * Getter method for number of towers in the island.
     * @return number of towers on the island.
     */
    public int getNumOfTowers() {
        return numberOfTowers;
    }

    /**
     * Method used to add students to the island
     * @param summedStudents EnumMap of added students
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void addStudents(EnumMap<Colors, Integer> summedStudents) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(students, summedStudents);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    /**
     *Method called in case towers are to be added to the island, such as in a conquest.
     * @param towers The towers to be added to the island.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void sumTowers(int towers) throws NegativeValueException {
        numberOfTowers += towers;
        if (numberOfTowers < 0) throw new NegativeValueException();
    }

    /**
     *Getter method of boolean field hasMotherNature.
     * @return true or false depending if MotherNature is on this island.
     */
    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    /**
     *Method to call when MotherNature reaches this island.
     */
    public void moveMotherNature() {
        hasMotherNature = true;
    }

    /**
     *Method to call when MotherNature leaves this island.
     */
    public void removeMotherNature() {
        hasMotherNature = false;
    }

    /**
     *Method to call when the students on the island are modified.
     * @param students new students EnumMap.
     */
    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    /**
     *Method called when a No Entry Tile is placed on this island after the according character card's activation or when it's removed.
     * @param bool decides whether the No Entry tile is present.
     */
    public void setHasNoEntryTile(boolean bool) {
        hasNoEntryTile = bool;
    }

    /**
     *Getter method for hasNoEntryTile boolean field
     * @return whether the island has an No Entry tile on it.
     */
    public boolean hasNoEntryTile() {
        return hasNoEntryTile;
    }
}