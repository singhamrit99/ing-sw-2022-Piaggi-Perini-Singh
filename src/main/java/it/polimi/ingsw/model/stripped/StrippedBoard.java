package it.polimi.ingsw.model.stripped;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.ArrayList;
import java.util.EnumMap;

public class StrippedBoard {
    private String owner;
    private EnumMap<Colors, Integer> entrance;
    private EnumMap<Colors, Integer> dining;
    private ArrayList<Colors> professorsTable;
    private int coins;
    private int numberOfTowers;

    public StrippedBoard(Player boardOwner) {
        this.owner = boardOwner.getNickname();
        SchoolBoard tmpBoard = boardOwner.getSchoolBoard();
        this.entrance = tmpBoard.getEntrance();
        this.dining = tmpBoard.getDining();
        this.professorsTable = tmpBoard.getProfessorsTable();
        this.coins = boardOwner.getCoins();
        this.numberOfTowers = tmpBoard.getTowers();
    }

    public String getOwner() {
        return owner;
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
