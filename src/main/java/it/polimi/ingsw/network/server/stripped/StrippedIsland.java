package it.polimi.ingsw.network.server.stripped;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.tiles.Island;

import java.io.Serializable;
import java.util.EnumMap;

public class StrippedIsland implements Serializable {
    final private String name;
    final private EnumMap<Colors, Integer> students;
    final int numberOfTowers;
    final private Towers towersColor;
    final boolean hasNoEnterTile;
    public boolean hasMotherNature;

    public StrippedIsland(Island island) {
        name = island.getName();
        this.towersColor = island.getTowersColor();
        numberOfTowers = island.getNumOfTowers();
        this.students = island.getStudents();
        this.hasNoEnterTile = island.hasNoEntryTile();
        this.hasMotherNature = island.hasMotherNature();
    }
    public String getName() {
        return name;
    }
    public Towers getTowersColor() {
        return towersColor;
    }
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }
    public int getNumOfTowers() {
        return numberOfTowers;
    }
    public boolean isHasNoEnterTile(){
        return hasNoEnterTile;
    }

    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }
}