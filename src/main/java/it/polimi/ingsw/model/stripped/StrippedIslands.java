package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.tiles.IslandTile;

import java.util.ArrayList;
import java.util.EnumMap;

public class StrippedIslands {
    private String name;
    private EnumMap<Colors, Integer> returnStudents;
    private int numberOfTowers;
    private Towers towersColor;
    private boolean hasMotherNature;
    private boolean hasNoEntryTile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumMap<Colors, Integer> getReturnStudents() {
        return returnStudents;
    }

    public void setReturnStudents(EnumMap<Colors, Integer> returnStudents) {
        this.returnStudents = returnStudents;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public void setNumberOfTowers(int aumberOfTowers) {
        this.numberOfTowers = aumberOfTowers;
    }

    public Towers getTowersColor() {
        return towersColor;
    }

    public void setTowersColor(Towers towersColor) {
        this.towersColor = towersColor;
    }

    public boolean isHasMotherNature() {
        return hasMotherNature;
    }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public boolean isHasNoEntryTile() {
        return hasNoEntryTile;
    }

    public void setHasNoEntryTile(boolean hasNoEntryTile) {
        this.hasNoEntryTile = hasNoEntryTile;
    }
}
