package it.polimi.ingsw.network.server.stripped;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.tiles.Island;

import java.io.Serializable;
import java.util.EnumMap;

public class StrippedIsland implements Serializable {
    private String name;
    private EnumMap<Colors, Integer> students;
    private int numberOfTowers;
    private Towers towersColor;
    private boolean hasNoEnterTile;
    private boolean hasMotherNature;

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
    public boolean hasNoEnterTile(){
        return hasNoEnterTile;
    }
    public boolean hasMotherNature() {
        return hasMotherNature;
    }
    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public void setStudents(EnumMap<Colors, Integer> students){
        this.students = students;
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    public void setTowersColor(Towers towersColor) {
        this.towersColor = towersColor;
    }

    public void setHasNoEnterTile(boolean hasNoEnterTile) {
        this.hasNoEnterTile = hasNoEnterTile;
    }

    public void setDestroyed(){
        name="EMPTY";
        System.out.println("DESTROYED");
    }
}