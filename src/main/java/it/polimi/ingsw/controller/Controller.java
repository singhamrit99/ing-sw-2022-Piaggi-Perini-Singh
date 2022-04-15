package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.ControllerExceptions;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.exceptions.MotherNatureLostException;
import java.util.ArrayList;
import java.util.EnumMap;

public class Controller {

    private Game game;

    public Controller(Game game) {
        this.game = game;
    }

    public void CallMoveMotherNature() {
        try {
            game.moveMotherNature(game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getPlayedCard().getMove());
        } catch (IncorrectStateException e) {
            manageException(e);
        } catch (MotherNatureLostException e) {
            manageException(e);
        } catch (IncorrectPlayerException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
    }

    public void CallMoveStudent(EnumMap<Colors, ArrayList<String>> students) {
        try {
            game.moveStudents(game.getCurrentPlayer().getNickname(), students);
        } catch (IncorrectStateException e) {
            manageException(e);
        } catch (IncorrectPlayerException e) {
            manageException(e);
        } catch (IncorrectArgumentException e) {
            manageException(e);
        }
    }

    public void CallPickCloud(int cloudTileID) {
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
        }
    }

    public void CallPlayAssistantCard(int playedCard) {
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
       /* public void CallPlayCharacterCard(){

        try{
            String nickname= game.getCurrentPlayer().getNickname();
           game.playCharacterCard(nickname)
        }
        catch(
    }*/

    public void manageException(IncorrectArgumentException e) {
        ControllerExceptions error = ControllerExceptions.INCORRECT_ARGUMENT_EXCEPTION;
        // game.error(error);
    }

    public void manageException(IncorrectPlayerException e) {
        ControllerExceptions error = ControllerExceptions.INCORRECT_PLAYER_EXCEPTION;
        // game.error(error);
    }

    public void manageException(IncorrectStateException e) {
        ControllerExceptions error = ControllerExceptions.INCORRECT_STATE_EXCEPTION;
        // game.error(error);
    }

    public void manageException(MotherNatureLostException e) {
        ControllerExceptions error = ControllerExceptions.MOTHER_NATURE_LOST_EXCEPTION;
        // game.error(error);
    }



}



