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

    /**
     * Player class constructor
     *
     * @param nickname        The player's nickname.
     * @param towerColors     The player's Tower colors. If it's a 4 player game 2 players will be on the same team with the same colors.
     * @param numberOfPlayers The number of players in the game. Used for method calls.
     */
    public Player(String nickname, Towers towerColors, int numberOfPlayers) {
        this.nickname = nickname;
        this.schoolBoard = new SchoolBoard(numberOfPlayers);
        this.towerColor = towerColors;
        assistantCardPlayedInThisTurn = false;
        assistantCardDeck = new AssistantCardDeck(nickname);
        playedCharacterCard = null;
        playedAssistantCard = null;
        assistantCardDeck.fillDeck();
        coins = 1;
    }

    /**
     * Method used to play an Assistant Card from the Player's personal deck.
     *
     * @param nameCard The Name of the assistant card to play.
     * @throws AssistantCardNotFoundException If the card associated with the string can't be found or the string is incorrect this exception is thrown.
     */
    public void playAssistantCard(String nameCard) throws AssistantCardNotFoundException {
        playedAssistantCard = assistantCardDeck.get(nameCard);
        assistantCardPlayedInThisTurn = true;
    }

    /**
     * Method used to make sure that the player plays and assistant card before the end of the Planning Phase.
     */
    public void forgetAssistantCard() { //necessary to check if others players play the same assistantCard in THIS turn (it s necessary to 'reset' at the end of any planning phase)
        assistantCardPlayedInThisTurn = false;
    }

    /**
     * Returns whether this player has played an assistant card this turn.
     *
     * @return true or false.
     */
    public boolean hasPlayedAssistantInThisTurn() {
        return assistantCardPlayedInThisTurn;
    }

    /**
     * Method used to add students to the Player's SchoolBoard.
     *
     * @param students the students that need to be added.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void addStudents(EnumMap<Colors, Integer> students) throws NegativeValueException {
        schoolBoard.addStudents(students);
    }

    /**
     * Method that moves the students across the Dining Room and Entrance. Also checks if the player needs to be awarded coins based on the number
     * of the students in their dining room.
     *
     * @param move   The students that are being moved.
     * @param remove The students that are being removed.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if any of the parameters used by the method are invalid.
     */
    public void moveStudents(EnumMap<Colors, Integer> move, EnumMap<Colors, Integer> remove) throws NegativeValueException, IncorrectArgumentException {
        EnumMap<Colors, Integer> total = new EnumMap<>(Colors.class);
        for (Colors color : Colors.values()) {
            total.put(color, move.get(color) + remove.get(color));
        }

        if (schoolBoard.hasEnoughStudents(total)) {
            if (move.size() != 0) coins += schoolBoard.moveStudents(move);
            if (remove.size() != 0) schoolBoard.removeStudents(remove);
        } else {
            throw new IncorrectArgumentException("EnumMap is incorrect");
        }
    }

    /**
     * Adds the professor of the provided color to this player's board.
     *
     * @param student The color of the professor that needs to be added.
     */
    public void addProfessor(Colors student) {
        schoolBoard.addProfessor(student);
    }

    /**
     * Removes the professor of the provided color from this player's board.
     *
     * @param student The color of the professor that needs to be removed.
     */
    public void removeProfessor(Colors student) throws ProfessorNotFoundException {
        schoolBoard.removeProfessor(student);
    }

    /**
     * Method used to compare players.
     *
     * @param otherPlayer the player I'm comparing agains.
     * @return whether we're the same player or not.
     */
    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(Integer.parseInt(playedAssistantCard.getImageName()), Integer.parseInt(otherPlayer.getPlayedAssistantCard().getImageName()));
    }

    /**
     * Returns the player's schoolboard.
     *
     * @return SchoolBoard
     */
    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
    }

    /**
     * Returns the player's Tower Color.
     *
     * @return Tower Color
     */
    public Towers getTowerColor() {
        return towerColor;
    }

    /**
     * Returns the player's played Assistant Card.
     *
     * @return played Assistant Card
     */
    public AssistantCard getPlayedAssistantCard() {
        return playedAssistantCard;
    }

    /**
     * Returns the player's Assistant Card Deck.
     *
     * @return Assistant Card Deck
     */
    public AssistantCardDeck getAssistantCardDeck() {
        return assistantCardDeck;
    }

    /**
     * Returns the player's towers.
     *
     * @return towers
     */
    public int getPlayerTowers() {
        return schoolBoard.getTowers();
    }

    /**
     * Returns the number of students of a certain color.
     *
     * @param student the color of the student I'm checking.
     * @return number of students of that color in my board (either dining or entrance depending on what the method is called on)
     */
    public int getNumOfStudent(Colors student) {
        return schoolBoard.getStudentsByColor(student);
    }

    /**
     * Method used in towers movement.
     *
     * @param num number of towers to move.
     */
    public void moveTowers(int num) {
        schoolBoard.moveTowers(num);
    }

    /**
     * Checks if this player possesses the professor of a certain color (used in conquest logic)
     *
     * @param student The color of the professor to check
     * @return true or false depending on the value.
     */
    public boolean hasProfessorOfColor(Colors student) {
        return schoolBoard.hasProfessorOfColor(student);
    }

    /**
     * Returns the player's nickname.
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Removes coins from player after successful character card activation.
     *
     * @param value the number of coins to remove.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if any of the parameters used by the method are invalid.
     */
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

    /**
     * Returns the player's coins.
     *
     * @return coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Returns the player's played character card.
     *
     * @return played character card
     */
    public CharacterCard getPlayedCharacterCard() {
        return playedCharacterCard;
    }

    /**
     * Method used to set a specific character card as "played".
     *
     * @param characterCard the character card that needs to be changed.
     */
    public void setPlayedCharacterCard(CharacterCard characterCard) {
        playedCharacterCard = characterCard;
    }
}
