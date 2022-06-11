package it.polimi.ingsw.network.server.stripped;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
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

    public StrippedBoard(Player boardOwner) {
        this.owner = boardOwner.getNickname();
        SchoolBoard tmpBoard = boardOwner.getSchoolBoard();
        this.entrance = tmpBoard.getEntrance();
        this.dining = tmpBoard.getDining();
        this.professorsTable = tmpBoard.getProfessorsTable();
        this.coins = boardOwner.getCoins();
        this.numberOfTowers = tmpBoard.getTowers();
        this.colorsTowers = boardOwner.getTowerColor();
        this.deck= boardOwner.getAssistantCardDeck();
        ArrayList<AssistantCard> temp= boardOwner.getAssistantCardDeck().getAllCards();
        AssistantCard add = null;
        int i=0;

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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
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

    public Towers getColorsTowers(){
        return colorsTowers;
    }

    public AssistantCardDeck getDeck() {
        return deck;
    }

    public void setDeck(AssistantCardDeck deck) {
        this.deck = deck;
    }
}
