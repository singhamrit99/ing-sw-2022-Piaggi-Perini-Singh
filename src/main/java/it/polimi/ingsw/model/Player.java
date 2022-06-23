package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AssistantCardNotFoundException;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;

import java.util.EnumMap;

/**
 * @author Amrit
 */
public class Player implements Comparable<Player> {
    private final String nickname;
    private SchoolBoard schoolBoard;
    private final Towers towerColor;
    private AssistantCardDeck assistantCardDeck;
    private AssistantCard playedAssistantCard;
    private Boolean assistantCardPlayedInThisTurn;
    private CharacterCard playedCharacterCard;
    private int coins;

    public Player(String nickname, Towers towerColors, int numberOfPlayers) {
        this.nickname = nickname;
        this.schoolBoard = new SchoolBoard(numberOfPlayers);
        this.towerColor = towerColors;
        assistantCardPlayedInThisTurn = false;
        assistantCardDeck = new AssistantCardDeck(nickname);
        assistantCardDeck.fillDeck();
        coins = 1;
    }

    public void playAssistantCard(String nameCard) throws AssistantCardNotFoundException {
        playedAssistantCard = assistantCardDeck.get(nameCard);
        assistantCardPlayedInThisTurn = true;
    }

    public void forgetAssistantCard() { //necessary to check if others players play the same assistantCard in THIS turn (it s necessary to 'reset' at the end of any planning phase)
        assistantCardPlayedInThisTurn = false;
    }

    public boolean hasPlayedAssistantInThisTurn() {
        return assistantCardPlayedInThisTurn;
    }

    public void addStudents(EnumMap<Colors, Integer> students) throws NegativeValueException {
        schoolBoard.addStudents(students);
    }

    public void moveStudents(EnumMap<Colors, Integer> move, EnumMap<Colors, Integer> remove) throws NegativeValueException, IncorrectArgumentException {
        EnumMap<Colors, Integer> total = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            total.put(color, move.get(color) + remove.get(color));
        }

        if (schoolBoard.hasEnoughStudents(total)) {
            System.out.println("Coins before: " + coins);
            if (move.size() != 0) coins += schoolBoard.moveStudents(move);
            if (remove.size() != 0) schoolBoard.removeStudents(remove);
            System.out.println("Coins after: " + coins);
        } else {
            throw new IncorrectArgumentException("EnumMap is incorrect");
        }
    }

    public void addProfessor(Colors student) {
        schoolBoard.addProfessor(student);
    }

    public void removeProfessor(Colors student) throws ProfessorNotFoundException {
        schoolBoard.removeProfessor(student);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(Integer.parseInt(playedAssistantCard.getImageName()), Integer.parseInt(otherPlayer.getPlayedAssistantCard().getImageName()));
    }

    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    public Towers getTowerColor() {
        return towerColor;
    }

    public AssistantCard getPlayedAssistantCard() {
        return playedAssistantCard;
    }

    public AssistantCardDeck getAssistantCardDeck() {
        return assistantCardDeck;
    }

    public int getPlayerTowers() {
        return schoolBoard.getTowers();
    }

    public int getNumOfStudent(Colors student) {
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

    public void removeCoins(int value) throws NegativeValueException, IncorrectArgumentException {
        if (value > coins) {
            throw new IncorrectArgumentException();
        }

        if (value >= 0) {
            coins -= value;
        } else {
            throw new NegativeValueException();
        }
    }

    public int getCoins() {
        return coins;
    }

    public CharacterCard getPlayedCharacterCard() {
        return playedCharacterCard;
    }

    public void setPlayedCharacterCard(CharacterCard characterCard) {
        playedCharacterCard = characterCard;
    }
}
