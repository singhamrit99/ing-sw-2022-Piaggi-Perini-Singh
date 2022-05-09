package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.enumerations.Colors;

import java.util.ArrayList;
import java.util.EnumMap;

public class StrippedBoard {
    private EnumMap<Colors, Integer> entrance;
    private EnumMap<Colors, Integer> dining;
    private EnumMap<Colors, Integer> coins;
    private ArrayList<Colors> professorsTable;
    private int numberOfTowers;

    public StrippedBoard(EnumMap<Colors, Integer> entrance, EnumMap<Colors, Integer> dining, EnumMap<Colors, Integer> coins, ArrayList<Colors> professorsTable, int numberOfTowers) {
        this.entrance = entrance;
        this.dining = dining;
        this.coins = coins;
        this.professorsTable = professorsTable;
        this.numberOfTowers = numberOfTowers;
    }

    public EnumMap<Colors, Integer> getEntrance() {
        return entrance;
    }

    public void setEntrance(EnumMap<Colors, Integer> entrance) {
        this.entrance = entrance;
    }

    public EnumMap<Colors, Integer> getDining() {
        return dining;
    }

    public void setDining(EnumMap<Colors, Integer> dining) {
        this.dining = dining;
    }

    public EnumMap<Colors, Integer> getCoins() {
        return coins;
    }

    public void setCoins(EnumMap<Colors, Integer> coins) {
        this.coins = coins;
    }

    public ArrayList<Colors> getProfessorsTable() {
        return professorsTable;
    }

    public void setProfessorsTable(ArrayList<Colors> professorsTable) {
        this.professorsTable = professorsTable;
    }

    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }
}
