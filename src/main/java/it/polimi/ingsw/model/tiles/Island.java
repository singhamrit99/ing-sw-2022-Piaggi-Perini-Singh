package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.NegativeValueException;

import java.util.EnumMap;

public class Island {
    private String name;
    private EnumMap<Colors, Integer> students;
    private int numberOfTowers;
    private Towers towersColor;
    private boolean hasMotherNature;
    private boolean hasNoEntryTile;

    public Island(String islandName) {
        name = islandName;
        hasMotherNature = false;
        towersColor = Towers.WHITE; //this value is ignored if numberOfTowers=0
        numberOfTowers = 0;
        hasNoEntryTile = false;
        students = new EnumMap<>(Colors.class);
        students = StudentManager.createEmptyStudentsEnum();
    }

    public String getName() {
        return name;
    }

    public Towers getTowersColor() {
        return towersColor;
    }

    public void setTowersColor(Towers newColor) {
        towersColor = newColor;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    public int getNumOfTowers() {
        return numberOfTowers;
    }

    public void addStudents(EnumMap<Colors, Integer> summedStudents) throws NegativeValueException {
        EnumMap<Colors, Integer> newStudents = StudentManager.addStudent(students, summedStudents);
        if (newStudents != null) {
            setStudents(newStudents);
        } else {
            throw new NegativeValueException();
        }
    }

    public void sumTowers(int towers) throws NegativeValueException {
        numberOfTowers += towers;
        if (numberOfTowers < 0) throw new NegativeValueException();
    }

    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    public void moveMotherNature() {
        hasMotherNature = true;
    }

    public void removeMotherNature() {
        hasMotherNature = false;
    }

    private void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    public void setHasNoEntryTile(boolean bool) {
        hasNoEntryTile = bool;
    }

    public boolean hasNoEntryTile() {
        return hasNoEntryTile;
    }
}