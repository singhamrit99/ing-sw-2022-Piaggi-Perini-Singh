package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

public class StrippedBoard implements Serializable {
    private String owner;
    private EnumMap<Colors, Integer> entrance;
    private EnumMap<Colors, Integer> dining;
    private ArrayList<Colors> professorsTable;
    private int coins;
    private int numberOfTowers;
    private Towers colorsTowers;
    private AssistantCardDeck deck;
    private int moves;

    /**
     * StrippedBoard class constructor method. Most of the parameters are setup in game based on the corresponding SchoolBoard.
     *
     * @param boardOwner The name of the player that this board corresponds to.
     */
    public StrippedBoard(Player boardOwner) {
        this.owner = boardOwner.getNickname();
        SchoolBoard tmpBoard = boardOwner.getSchoolBoard();
        this.entrance = tmpBoard.getEntrance();
        this.dining = tmpBoard.getDining();
        this.professorsTable = tmpBoard.getProfessorsTable();
        this.coins = boardOwner.getCoins();
        this.numberOfTowers = tmpBoard.getTowers();
        this.colorsTowers = boardOwner.getTowerColor();
        this.deck = boardOwner.getAssistantCardDeck();
    }

    /**
     * Getter method for owner field.
     *
     * @return owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Getter method for entrance enumMap.
     *
     * @return entrance.
     */
    public EnumMap<Colors, Integer> getEntrance() {
        return entrance;
    }

    /**
     * Setter method for Entrance field.
     *
     * @param entrance the Student EnumMap the entrance has to be set to.
     */
    public void setEntrance(EnumMap<Colors, Integer> entrance) {
        this.entrance = entrance;
    }

    /**
     * Getter method for Dining field.
     *
     * @return the dining room.
     */
    public EnumMap<Colors, Integer> getDining() {
        return dining;
    }

    /**
     * Setter method for Dining field.
     *
     * @param dining the student enumMap the dining room has to be set to.
     */
    public void setDining(EnumMap<Colors, Integer> dining) {
        this.dining = dining;
    }

    /**
     * Getter method for Coins field.
     *
     * @return coins (int)
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Setter method for Coins field.
     *
     * @param coins the player's new coins.
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Getter method for the professors table
     *
     * @return the professors ArrayList.
     */
    public ArrayList<Colors> getProfessorsTable() {
        return professorsTable;
    }

    /**
     * Setter method for professors table field
     *
     * @param professorsTable the arrayList the professors table has to be set to.
     */
    public void setProfessorsTable(ArrayList<Colors> professorsTable) {
        this.professorsTable = professorsTable;
    }

    /**
     * Getter method for numberOfTowers field
     *
     * @return numberOfTowers
     */
    public int getNumberOfTowers() {
        return numberOfTowers;
    }

    /**
     * Setter method for numberOfTowers field
     *
     * @param numberOfTowers the updated number of towers.
     */
    public void setNumberOfTowers(int numberOfTowers) {
        this.numberOfTowers = numberOfTowers;
    }

    /**
     * Getter method for this player's tower colors.
     *
     * @return the color of their towers.
     */
    public Towers getColorsTowers() {
        return colorsTowers;
    }

    /**
     * Getter method for the Assistant Card deck.
     *
     * @return the Assistant Card deck.
     */
    public AssistantCardDeck getDeck() {
        return deck;
    }

    /**
     * Setter method for the assistant card deck.
     *
     * @param deck Updated deck.
     */
    public void setDeck(AssistantCardDeck deck) {
        this.deck = deck;
    }

    /**
     * Getter method for moves field (Mother Nature's moves)
     *
     * @return moves.
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Setter method for moves field
     *
     * @param moves updated moves.
     */
    public void setMoves(int moves) {
        this.moves = moves;
    }
}
