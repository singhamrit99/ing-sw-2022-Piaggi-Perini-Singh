package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Amrit
 */
public class Player implements Comparable<Player> {
    private final String nickname;
    private SchoolBoard schoolBoard;
    private final Towers towerColor;
    private AssistantCardDeck assistantCardDeck;
    private AssistantCard playedCard;
    private int coins;

    public Player(String nickname, Towers towerColors, int numberOfPlayers) {
        this.nickname = nickname;
        this.schoolBoard = new SchoolBoard(numberOfPlayers);
        this.towerColor = towerColors;
        assistantCardDeck = new AssistantCardDeck();
        assistantCardDeck.fillDeck();
        coins = 1;
    }

    public void playAssistantCard(int index) {
        playedCard = assistantCardDeck.get(index);
        assistantCardDeck.get(index).setHasPlayed(true);
    }

    public boolean moveMotherNature(int distance) {
        return distance <= playedCard.getValue();
    }

    public void addStudents(EnumMap<Colors, Integer> students) throws IncorrectArgumentException {
        schoolBoard.addStudents(students);
    }

    public void moveStudents(EnumMap<Colors, Integer> students, ArrayList<Integer> destinations) throws IncorrectArgumentException {
        int i = 0;
        EnumMap<Colors, Integer> studentsToMove = new EnumMap(Colors.class);
        EnumMap<Colors, Integer> studentsToRemove = new EnumMap(Colors.class);

        if (schoolBoard.hasEnoughStudents(students)) {
            for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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
        if (studentsToMove.size() != 0) {
            coins += schoolBoard.moveStudents(studentsToMove);
        }
        if (studentsToRemove.size() != 0) schoolBoard.removeStudents(studentsToRemove);
    }

    public void addProfessor(Colors student) {
        schoolBoard.addProfessor(student);
    }

    public void removeProfessor(Colors student) throws IncorrectArgumentException {
        schoolBoard.removeProfessor(student);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(otherPlayer.getPlayedCard().getValue(), playedCard.getValue());
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public Towers getTowerColor() {
        return towerColor;
    }

    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    public AssistantCardDeck getAssistantCardDeck() {
        return assistantCardDeck;
    }

    public int getPlayerTowers() {
        return schoolBoard.getTowers();
    }

    public int getNumOfStudent(Colors student) throws IncorrectArgumentException {
        return schoolBoard.getStudentsByColor(student);
    }

    public void moveTowers(int num) {
        schoolBoard.moveTowers(num);
    }

    public boolean hasProfessorOfColor(Colors student) {
        return schoolBoard.hasProfessorOfColor(student);
    }

    public String getNickname() {
        return nickname;
    }

    public void removeCoins(int value) throws IncorrectArgumentException {
        if (value >= 0) {
            coins -= value;
        } else {
            throw new IncorrectArgumentException();
        }
    }

    public int getCoins() {
        return coins;
    }
}
