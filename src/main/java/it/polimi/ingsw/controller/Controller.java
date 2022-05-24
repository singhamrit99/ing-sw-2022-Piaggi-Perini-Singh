package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.Room;

import java.util.ArrayList;
import java.util.EnumMap;

public class Controller {
    private Game game;

    public Game initializeGame(Room room, boolean expertMode, int numOfPlayers, ArrayList<String> nicknames) throws IncorrectArgumentException, NegativeValueException {
        if (numOfPlayers < 1 || numOfPlayers > 4) {
            throw new IncorrectArgumentException("Invalid number of players\n");
        } else if (numOfPlayers != nicknames.size()) {
            throw new IncorrectArgumentException("Number of players and nicknames array list length mismatch\n");
        }
        game = new Game(room, expertMode, numOfPlayers, nicknames);

        return game;
    }

    public void callMoveMotherNature(String playerCaller, int distance) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, MotherNatureLostException, IncorrectStateException {
        game.moveMotherNature(playerCaller, distance);
    }

    public void callMoveStudent(String playerCaller, EnumMap<Colors, ArrayList<String>> students) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, IncorrectStateException {
        game.moveStudents(playerCaller, students);
    }

    public void callPickCloud(String playerCaller, int cloudTileID) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, IncorrectStateException {
        if (cloudTileID < 0 || cloudTileID > 3) {
            throw new IncorrectArgumentException();
        }
        game.takeStudentsFromCloud(playerCaller, cloudTileID);
    }

    public void callPlayAssistantCard(String playerCaller, String playedCardName) throws IncorrectPlayerException, IncorrectArgumentException, AssistantCardNotFound, IncorrectStateException {
        game.playAssistantCard(playerCaller, playedCardName);
    }

    public void callPlayCharacterCard(int index) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        game.activateCharacterCard(index);
    }

    public void callPlayCharacterCard(int index, int student, int island) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        game.activateCharacterCard(index, student, island);
    }

    public void callPlayCharacterCard(int index, EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        game.activateCharacterCard(index, students1, students2);
    }

    public void callPlayCharacterCard(int index, int choice) throws NegativeValueException, IncorrectArgumentException, ProfessorNotFoundException, NotEnoughCoinsException {
        game.activateCharacterCharacter(index, choice);
    }
}



