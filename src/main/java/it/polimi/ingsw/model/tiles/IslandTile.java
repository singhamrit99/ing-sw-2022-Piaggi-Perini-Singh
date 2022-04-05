package it.polimi.ingsw.model.tiles;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.EnumMap;
import java.util.Map;

public class IslandTile {
    private String name;
    private EnumMap<Colors, Integer> students;
    private int numberOfTowers;
    private Towers towersColor;
    private boolean hasMotherNature;
    private boolean hasNoEntryTile;

    public IslandTile(String islandName) {
        name = islandName;
        hasMotherNature = false;
        towersColor = Towers.WHITE;
        numberOfTowers = 0;
        hasNoEntryTile = false;
        this.students = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            students.put(color, 0);
        }
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

    public void addStudents(EnumMap<Colors, Integer> summedStudents) throws IncorrectArgumentException {
        EnumMap<Colors, Integer> tmp = getStudents();
        for (Map.Entry<Colors, Integer> studentsNewEnumMap : summedStudents.entrySet()) {
            if (studentsNewEnumMap.getValue() >= 0) {
                //if (tmp.containsKey(studentsNewEnumMap.getKey())) {
                tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue() + tmp.get(studentsNewEnumMap.getKey()));
               /* } else {
                    tmp.put(studentsNewEnumMap.getKey(), studentsNewEnumMap.getValue());
                }*/
            } else {
                throw new IncorrectArgumentException("EnumMap is not correct");
            }
        }
        students = tmp;
    }

    public void sumTowers(int towers) throws IncorrectArgumentException {
        numberOfTowers += towers;
        if (numberOfTowers < 0) throw new IncorrectArgumentException();
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

}