package it.polimi.ingsw.model;

import it.polimi.ingsw.AssistantCard;
import it.polimi.ingsw.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amrit
 */
public class Player {
    private String nickname;
    private ArrayList<AssistantCardDeck> assistantCardDeck;
    private AssistantCard playedCard;
    private Colors towerColors;
    private int characterCard;

    public void playAssistantCard(int index){

    }

    public void moveMotherNature(int distance){

    }

    public void moveStudents(HashMap<StudentDisc, Integer> students, ArrayList<Integer> destination){

    }

    public void insertStudentsFromCloud(HashMap<StudentDisc, Integer> students){

    }
}
