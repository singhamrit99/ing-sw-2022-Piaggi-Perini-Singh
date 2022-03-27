package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Amrit
 */
public class Player implements Comparable<Player> {
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

    public void addStudents(HashMap<StudentDisc, Integer> students) throws IncorrectArgumentException {
        schoolBoard.addStudents(students);
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

    public void moveProfessor(ArrayList<ProfessorPawn> professorPawns) {
        schoolBoard.moveProfessors(professorPawns);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        if (otherPlayer.getPlayedCard().getValue() < playedCard.getValue()) return -1;
        if (otherPlayer.getPlayedCard().getValue() > playedCard.getValue()) return 1;
        return 0;
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public Colors getTowerColors() {
        return towerColors;
    }

    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    public int getPlayerTowers() {
        return schoolBoard.getTowers();
    }

    public int getStudentsByColor(Colors color) throws IncorrectArgumentException {
        return schoolBoard.getStudentsByColor(color);
    }

    public void moveTowers(int steps) {
        schoolBoard.moveTowers(steps);
    }
}
