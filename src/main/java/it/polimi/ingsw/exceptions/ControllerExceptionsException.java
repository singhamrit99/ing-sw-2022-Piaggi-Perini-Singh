package it.polimi.ingsw.exceptions;

public enum ControllerExceptionsException {
    INCORRECT_ARGUMENT_EXCEPTION(101, "Game logic error! Incorrect argument provided."),
    INCORRECT_PLAYER_EXCEPTION(102, "Game logic error! Wrong player."),
    INCORRECT_STATE_EXCEPTION(103, "Game logic error! Incorrect phase of the game."),
    MOTHER_NATURE_LOST_EXCEPTION(104, "Game logic error! Can't find Mother Nature."),
    NEGATIVE_VALUE_EXCEPTION(105, "Game logic error! Negative value."),
    PROFESSOR_NOT_FOUND_EXCEPTION(106, "Game logic error! Can't find professor."),
    NOT_ENOUGH_COINS_EXCEPTION(107, "Game logic error! Not enough coins to play card."),
    ASSISTANT_CARD_NOT_FOUND (108, "Game logic error! AssistantCard not found.");
    final int errorCode;

    ControllerExceptionsException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    final String message;
}
