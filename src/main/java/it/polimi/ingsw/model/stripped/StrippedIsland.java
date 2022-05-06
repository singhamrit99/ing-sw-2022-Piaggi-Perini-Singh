package it.polimi.ingsw.model.stripped;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import java.util.EnumMap;

public class StrippedIsland {
    final private String name;
    final private EnumMap<Colors, Integer> students;
    final int numberOfTowers;
    final private Towers towersColor;
    final boolean hasNoEnterTile;

    public StrippedIsland(String islandName, Towers towersColor, int numOfTowers,
                          EnumMap<Colors,Integer> students, boolean hasNoEnterTile) {
        name = islandName;
        this.towersColor = towersColor;
        numberOfTowers = numOfTowers;
        this.students = students;
        this.hasNoEnterTile = hasNoEnterTile;
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
}