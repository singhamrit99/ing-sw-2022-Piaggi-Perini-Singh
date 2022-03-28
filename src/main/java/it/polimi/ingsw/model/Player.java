package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        int i = 0;
        HashMap<StudentDisc, Integer> studentsToMove = new HashMap<>();
        HashMap<StudentDisc, Integer> studentsToRemove = new HashMap<>();

        if (schoolBoard.hasEnoughStudents(students)) {
            if (students.size() == destinations.size()) {
                for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
                    if (destinations.get(i) != 1 && destinations.get(i) != 0) {
                        throw new IncorrectArgumentException();
                    } else {
                        if (destinations.get(i) == 0) {
                            studentsToMove.put(set.getKey(), set.getValue());
                        } else {
                            studentsToRemove.put(set.getKey(), set.getValue());
                        }
                        i++;
                    }
                }
            } else {
                throw new IncorrectArgumentException();
            }
        } else {
            throw new IncorrectArgumentException();
        }
        if (studentsToMove.size() != 0) schoolBoard.moveStudents(studentsToMove);
        if (studentsToRemove.size() != 0) schoolBoard.removeStudents(studentsToRemove);
    }

    public void moveProfessor(Colors color) throws IncorrectArgumentException {
        schoolBoard.moveProfessors(color);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(otherPlayer.getPlayedCard().getValue(), playedCard.getValue());
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
