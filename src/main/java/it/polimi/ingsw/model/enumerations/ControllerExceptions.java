package it.polimi.ingsw.model.enumerations;

import it.polimi.ingsw.model.exceptions.MotherNatureLostException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;

public enum ControllerExceptions {

    INCORRECT_ARGUMENT_EXCEPTION(101, "Game logic error! Incorrect argument provided."),
    INCORRECT_PLAYER_EXCEPTION(102, "Game logic error! Wrong player."),
    INCORRECT_STATE_EXCEPTION(103, "Game logic error! Incorrect phase of the game."),
    MOTHER_NATURE_LOST_EXCEPTION(104, "Game logic error! Can't find Mother Nature."),
    NEGATIVE_VALUE_EXCEPTION(105, "Game logic error! Negative value."),
    PROFESSOR_NOT_FOUND_EXCEPTION(106, "Game logic error! Can't find professor.");
    int errorcode;

    ControllerExceptions(int errorcode, String message) {
        this.errorcode = errorcode;
        this.message = message;
    }

    String message;
}
