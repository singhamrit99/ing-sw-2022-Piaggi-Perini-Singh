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

    /**
     * StrippedIsland constructor called in Room when building StrippedModel.
     *
     * @param island the full Island this StrippedIsland is built from.
     */
    public StrippedIsland(Island island) {
        name = island.getName();
        this.towersColor = island.getTowersColor();
        numberOfTowers = island.getNumOfTowers();
        this.students = island.getStudents();
        this.hasNoEnterTile = island.hasNoEntryTile();
        this.hasMotherNature = island.hasMotherNature();
    }

    /**
     * Island name getter method.
     *
     * @return the island's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for towers color.
     *
     * @return Towers color.
     */
    public Towers getTowersColor() {
        return towersColor;
    }

    /**
     * Students on island getter method.
     *
     * @return students EnumMap.
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    /**
     * Getter method for number of towers on the island.
     *
     * @return The number of towers in the island (int)
     */
    public int getNumOfTowers() {
        return numberOfTowers;
    }

    /**
     * Getter method for hasNoEnterTile field.
     *
     * @return true or false depending on No Entry Tile presence.
     */
    public boolean hasNoEnterTile() {
        return hasNoEnterTile;
    }

    /**
     * Getter method for MotherNature field.
     *
     * @return true or false depending on Mother Nature presence.
     */
    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    /**
     * MotherNature setter method.
     *
     * @param hasMotherNature true or false depending on the presence of MN on the island.
     */
    public void setHasMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    /**
     * Setter method for students on the island.
     *
     * @param students updated students EnumMap.
     */
    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }

    /**
     * Number of towers setter method.
     *
     * @param numberOfTowers the updated number of towers.
     */
    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    /**
     * Towers color setter method.
     *
     * @param towersColor the towers' color.
     */
    public void setTowersColor(Towers towersColor) {
        this.towersColor = towersColor;
    }

    /**
     * Setter method for the presence of a No Entry Tile on this island.
     *
     * @param hasNoEnterTile true or false depending on the presence of a No Entry tile.
     */
    public void setHasNoEnterTile(boolean hasNoEnterTile) {
        this.hasNoEnterTile = hasNoEnterTile;
    }

    /**
     * Method called on unification of islands.
     */
    public void setDestroyed() {
        name = "EMPTY";
    }
}