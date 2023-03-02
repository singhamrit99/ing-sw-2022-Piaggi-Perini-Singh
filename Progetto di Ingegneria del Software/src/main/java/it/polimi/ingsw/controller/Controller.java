package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.tiles.Island;
import it.polimi.ingsw.network.server.Room;

import java.util.ArrayList;
import java.util.EnumMap;

public class Controller {
    private Game game;

    /**
     * Creates and initializes the Game instance the Room will be playing in with its Players.
     * As with every other remote method in the project it is called with RMI.
     *
     * @param room         The Room that called this command.
     * @param expertMode   Tells the game if it has to start in Standard or Expert mode.
     * @param numOfPlayers Number of players.
     * @param nicknames    Nicknames of every player in the Room.
     * @return game: returns the Game to the Room so that it can be acted upon
     * @throws IncorrectArgumentException Thrown if the number of players is illegal (e.g. less than 1 or more than 4)
     * @throws NegativeValueException     No value in this game can be negative, so any negative number is an error of some kind.
     */
    public Game initializeGame(Room room, boolean expertMode, int numOfPlayers, ArrayList<String> nicknames) throws IncorrectArgumentException, NegativeValueException {
        if (numOfPlayers < 2 || numOfPlayers > 4) {
            throw new IncorrectArgumentException("Invalid number of players\n");
        } else if (numOfPlayers != nicknames.size()) {
            throw new IncorrectArgumentException("Number of players and nicknames array list length mismatch\n");
        }
        game = new Game(room, expertMode, numOfPlayers, nicknames);
        return game;
    }

    /**
     * Controller method that allows the players to move MN through the Controller.
     *
     * @param playerCaller The player that called the method. Since we require all players to have different nicknames the nickname itself is
     *                     sufficient for method invocation.
     * @param distance     The distance that the Player has chosen for Mother Nature's movement. Most exceptions stem from its incorrect value.
     * @throws IncorrectPlayerException   Thrown if the player that called this method is NOT the current player.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException If the PlayerCaller is null the method invocation as a whole is incorrect.
     * @throws MotherNatureLostException  If the game can't find MotherNature it will throw this exception.
     * @throws IncorrectStateException    Mother Nature can only be moved AFTER three students have been moved. Trying to call this method in
     *                                    any other circumstance (like the Planning phase or after moving MN) will result in this exception.
     */
    public void callMoveMotherNature(String playerCaller, int distance) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException,
            MotherNatureLostException, IncorrectStateException {
        if (playerCaller == null) throw new IncorrectArgumentException();
        if (distance <= 0) throw new NegativeValueException();
        game.moveMotherNature(playerCaller, distance);
    }

    /**
     * Controller method that allows the players to draw students to refill the Clouds.
     *
     * @param playerCaller The player that called the method. Since we require all players to have different nicknames the nickname itself is
     *                     *                     sufficient for method invocation.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectPlayerException   Players can't perform actions when it's not their turn: this is thrown if such an occurrence happens.
     * @throws IncorrectArgumentException Thrown if the playerCaller string is invalid (null or not present in the game).
     */
    public void callDrawFromBag(String playerCaller) throws NegativeValueException, IncorrectPlayerException, IncorrectArgumentException {
        if (playerCaller == null) throw new IncorrectArgumentException();
        game.drawFromBag(playerCaller);
    }

    /**
     * Controller method that allows the players to move students in the game (Entrance ->Dining, Entrance->Islands
     * and CharacterCards interactions).
     *
     * @param playerCaller The player that called the method. Since we require all players to have different nicknames the nickname itself is
     *                     *                     sufficient for method invocation.
     * @throws IncorrectPlayerException   Players can't perform actions when it's not their turn: this is thrown if such an occurrence happens.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if the playerCaller string is invalid (null or not present in the game) or the students EnumMap is incorrect.
     * @throws ProfessorNotFoundException Thrown if the professor assignment method fails to find the related professor, either because of a color
     *                                    mismatch or other internal error.
     * @throws IncorrectStateException    Players can't perform actions when it's not their turn: this is thrown if such an occurrence happens.
     */
    public void callMoveStudent(String playerCaller, Colors color, String dest) throws IncorrectPlayerException, NegativeValueException,
            IncorrectArgumentException, ProfessorNotFoundException, IncorrectStateException {
        if (playerCaller == null) throw new IncorrectArgumentException();
        if (color.getIndex() < 0 || color.getIndex() > 4) throw new IncorrectArgumentException();
        boolean found = false;
        for (Island island : game.getIslands()) {
            if (island.getName().equals(dest)) {
                found = true;
                break;
            }
        }
        if (!found) {
            if (!dest.equals("dining")) {
                throw new IncorrectArgumentException();
            }
        }

        game.moveStudents(playerCaller, color, dest);
    }

    /**
     * Controller method that allows the players to pick which Cloud tile to refill their Entrance with at the end of their turn.
     *
     * @param playerCaller The player that called the method. Since we require all players to have different nicknames the nickname itself is
     *                     sufficient for method invocation.
     * @param cloudTileID  The cloud chosen. Since it's used for display purposes in the GUI part of the application it's more convenient to keep it
     *                     as a string.
     * @throws IncorrectPlayerException   Players can't perform actions when it's not their turn: this is thrown if such an occurrence happens.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException check on the cloudTileID field.
     * @throws IncorrectStateException    Since the player can only pick a Cloud tile to refill their entrance AFTER playing three students and moving MN
     *                                    trying to do so in any other phase of the turn will result in this exception.
     */
    public void callPickCloud(String playerCaller, String cloudTileID) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, IncorrectStateException {
        if (playerCaller == null) throw new IncorrectArgumentException();
        if (cloudTileID == null) throw new IncorrectArgumentException();
        game.takeStudentsFromCloud(playerCaller, cloudTileID);
    }

    /**
     * Controller method that allows players to play Assistant Cards.
     *
     * @param playerCaller   The player that called the method. Since we require all players to have different nicknames the nickname itself is
     *                       *                      sufficient for method invocation.
     * @param playedCardName The Assistant Card chosen by the player. As always, its name doubles for image display purposes.
     * @throws IncorrectPlayerException       Players can't perform actions when it's not their turn: this is thrown if such an occurrence happens.
     * @throws IncorrectArgumentException     Thrown if the playerCaller string is invalid.
     * @throws AssistantCardNotFoundException Thrown if the playedCardName string is invalid or if the card has been removed after being played.
     * @throws IncorrectStateException        Thrown if this method is called in any phase but Planning Phase.
     */
    public void callPlayAssistantCard(String playerCaller, String playedCardName) throws IncorrectPlayerException, IncorrectArgumentException, AssistantCardNotFoundException, IncorrectStateException, NegativeValueException, AssistantCardAlreadyPlayed {
        if (playerCaller == null) throw new IncorrectArgumentException();
        if (playedCardName == null) throw new IncorrectArgumentException();
        game.playAssistantCard(playerCaller, playedCardName);
    }

    /**
     * First and simplest of the methods that call Character cards, due to requiring different parameters. This only requires the character ID, as it performs an automatic action.
     * All callPlayCharacterCard methods after this are overloaded due to specific card activation requirements.
     *
     * @param index The id of the played Character card.
     * @throws NegativeValueException     This game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if the index field is invalid.
     * @throws ProfessorNotFoundException Thrown by one of the cards' power if there are no professors to steal.
     * @throws NotEnoughCoinsException    Thrown if the Player doesn't have enough coins to buy this Character card.
     */
    public void callPlayCharacterCard(int index) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException {
        if (index < 0) throw new NegativeValueException();
        game.activateCharacterCard(index);
    }

    /**
     * Second of the methods that call Character cards. It requires the Character ID, a student and an island.
     *
     * @param index   The id of the played Character card.
     * @param student Because of the card's power it's more convenient to store the student choice as an int.
     * @param island  Ditto.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if one of these fields is invalid in some other way.
     * @throws ProfessorNotFoundException Thrown if the power activation results in a professor gain and an error occurs.
     * @throws NotEnoughCoinsException    Thrown if the Player doesn't have enough coins to buy this Character card.
     */
    public void callPlayCharacterCard(int index, int student, int island) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException {
        if (index < 0 || student < 0 || island < 0) throw new NegativeValueException();
        game.activateCharacterCard(index, student, island);
    }

    /**
     * Third and most complex overloading of the callPlayCharacterCard method, it requires two sets of student EnumMaps.
     *
     * @param index     The id of the played Character card.
     * @param students1 The first of the two Students EnumMaps.
     * @param students2 The second of the two Students EnumMaps.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException If any of the EnumMaps are not built correctly this exception is thrown.
     * @throws ProfessorNotFoundException Thrown if the power activation results in a professor gain and an error occurs.
     * @throws NotEnoughCoinsException    Thrown if the Player doesn't have enough coins to buy this Character card.
     */
    public void callPlayCharacterCard(int index, EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException {
        if (index < 0) throw new NegativeValueException();
        if (students1 == null || students2 == null) throw new IncorrectArgumentException();
        if (students1.isEmpty() || students2.isEmpty()) throw new IncorrectArgumentException();
        game.activateCharacterCard(index, students1, students2);
    }

    /**
     * Fourth and last overloading of the Character activation method, this requires only a choice from the player.
     *
     * @param index  The id of the played Character card.
     * @param choice The choice of the player, coded as an integer for convenience.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if either the index or the choice are invalid but not <0.
     * @throws ProfessorNotFoundException Thrown if the power activation results in a professor gain and an error occurs.
     * @throws NotEnoughCoinsException    Thrown if the Player doesn't have enough coins to buy this Character card.
     */
    public void callPlayCharacterCard(int index, int choice) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException {
        if (index < 0 || choice < 0) throw new NegativeValueException();
        game.activateCharacterCard(index, choice);
    }
}