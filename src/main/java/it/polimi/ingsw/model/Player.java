package it.polimi.ingsw.model;

import it.polimi.ingsw.AssistantCard;
import it.polimi.ingsw.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class Player implements Comparable {
    private String nickname;
    private SchoolBoard schoolBoard;
    private Colors towerColors;
    private AssistantCardDeck assistantCardDeck;
    private AssistantCard playedCard;
    private int characterCard;

    public Player(String nickname, Colors towerColors, int numberOfPlayers) {
        this.nickname = nickname;
        this.schoolBoard = new SchoolBoard(numberOfPlayers);
        this.towerColors = towerColors;

    }

    public void playAssistantCard(int index) {
        playedCard = assistantCardDeck.getAssistantCard(index);
        assistantCardDeck.getAssistantCard(index).setHasPlayed(true);
    }

    public boolean moveMotherNature(int distance) {
        return true;
    }

    public void moveStudents(HashMap<StudentDisc, Integer> students, ArrayList<Integer> destinations) throws IncorrectArgumentException {
        if (schoolBoard.hasEnoughStudents(students)) {
            if (students.size() == destinations.size()) {
                for (Integer destination : destinations) {
                    if (destination == 0) {
                        schoolBoard.moveStudents(students);
                    } else {
                        schoolBoard.removeStudents(students);
                    }
                }
            } else {
                throw new IncorrectArgumentException();
            }
        } else {
            throw new IncorrectArgumentException();
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
