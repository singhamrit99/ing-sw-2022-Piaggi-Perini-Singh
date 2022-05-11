package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.server.Room;

import java.util.ArrayList;
import java.util.EnumMap;

public class Controller {
    private Game game;

    public Game initializeGame(Room room, boolean expertMode, int numOfPlayers, ArrayList<String> nicknames) throws IncorrectArgumentException {
        if (numOfPlayers < 1 || numOfPlayers > 4) {
            throw new IncorrectArgumentException("Invalid number of players\n");
        } else if (numOfPlayers != nicknames.size()) {
            throw new IncorrectArgumentException("Number of players and nicknames array list lenght mismatch\n");
        }
        try {
            game = new Game(room,expertMode, numOfPlayers, nicknames);
        } catch (NegativeValueException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
        return game;
    }

    public void callMoveMotherNature(int distance) {
        try {
            game.moveMotherNature(game.getCurrentPlayer().getNickname(), distance);
        } catch (IncorrectStateException e) {
            manageException(e);
        } catch (MotherNatureLostException e) {
            manageException(e);
        } catch (IncorrectPlayerException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        }
    }

    public void callMoveStudent(EnumMap<Colors, ArrayList<String>> students) {
        try {
            game.moveStudents(game.getCurrentPlayer().getNickname(), students);
        } catch (IncorrectStateException e) {
            manageException(e);
        } catch (IncorrectPlayerException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        } catch (ProfessorNotFoundException e) {
            manageException(e);
        }
    }

    public void callPickCloud(int cloudTileID) {
        try {
            if (cloudTileID < 0 || cloudTileID > 3) {
                throw new IncorrectArgumentException();
            }
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
        try {
            game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), cloudTileID);
        } catch (IncorrectStateException e) {
            manageException(e);
        } catch (IncorrectPlayerException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        }
    }

    public void callPlayAssistantCard(int playedCard) {
        try {
            if (playedCard < 0 || playedCard > 11) {
                throw new IncorrectArgumentException();
            }
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
        try {
            String nickname = game.getCurrentPlayer().getNickname();
            game.playAssistantCard(nickname, playedCard);
        } catch (IncorrectStateException e) {
            manageException(e);
        } catch (IncorrectPlayerException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
    }

    public void callPlayCharacterCard(int index) {


        try {
            game.activateCharacterCard(index);
        } catch (NotEnoughCoinsException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        } catch (ProfessorNotFoundException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
    }

    public void callPlayCharacterCard(int index, int student, int island) {
        try {
            game.activateCharacterCard(index, student, island);
        } catch (NotEnoughCoinsException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        } catch (ProfessorNotFoundException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
    }

    public void callPlayCharacterCard(int index, EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
        try {
            game.activateCharacterCard(index, students1, students2);
        } catch (NotEnoughCoinsException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        } catch (ProfessorNotFoundException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
    }

    public void callPlayCharacterCard(int index, int choice) {
        try {
            game.activateCharacterCharacter(index, choice);
        } catch (NotEnoughCoinsException e) {
            manageException(e);
        } catch (NegativeValueException e) {
            manageException(e);
        } catch (ProfessorNotFoundException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }

    }

    public void manageException(NotEnoughCoinsException e) {
        ControllerExceptions error = ControllerExceptions.NOT_ENOUGH_COINS_EXCEPTION;
        game.error(error);
    }

    public void manageException(IncorrectArgumentException e) {
        ControllerExceptions error = ControllerExceptions.INCORRECT_ARGUMENT_EXCEPTION;
        game.error(error);
    }

    public void manageException(NegativeValueException e) {
        ControllerExceptions error = ControllerExceptions.NEGATIVE_VALUE_EXCEPTION;
        game.error(error);
    }

    public void manageException(IncorrectPlayerException e) {
        ControllerExceptions error = ControllerExceptions.INCORRECT_PLAYER_EXCEPTION;
        game.error(error);
    }

    public void manageException(IncorrectStateException e) {
        ControllerExceptions error = ControllerExceptions.INCORRECT_STATE_EXCEPTION;
        game.error(error);
    }

    public void manageException(MotherNatureLostException e) {
        ControllerExceptions error = ControllerExceptions.MOTHER_NATURE_LOST_EXCEPTION;
        game.error(error);
    }

    public void manageException(ProfessorNotFoundException e) {
        ControllerExceptions error = ControllerExceptions.PROFESSOR_NOT_FOUND_EXCEPTION;
        game.error(error);
    }
}



