package it.polimi.ingsw.model;

import it.polimi.ingsw.AssistantCard;
import it.polimi.ingsw.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amrit
 */
public class Player implements Comparable{
    private String nickname;
    private SchoolBoard schoolBoard;
    private Colors towerColors;
    private AssistantCardDeck assistantCardDeck;
    private AssistantCard playedCard;
    private int characterCard;

    public Player(String nickname, Colors towerColors, int numberOfPlayers){
        this.nickname = nickname;
        this.schoolBoard = new SchoolBoard(numberOfPlayers);
        this.towerColors = towerColors;

    }

    public void playAssistantCard(int index){
        assistantCardDeck.getAssistantCard(index).setHasPlayed(true);

    }

    public boolean moveMotherNature(int distance){
        return true;
    }

    public void moveStudents(HashMap<StudentDisc, Integer> students, ArrayList<Integer> destination){

    }

    public void insertStudentsFromCloud(HashMap<StudentDisc, Integer> students){

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
