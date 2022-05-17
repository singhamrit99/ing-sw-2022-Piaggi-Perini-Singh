package it.polimi.ingsw.exceptions;

public enum ControllerExceptions {
    INCORRECT_ARGUMENT_EXCEPTION(101, "IncorrectArgument"),
    INCORRECT_PLAYER_EXCEPTION(102, "WrongPlayer."),
    INCORRECT_STATE_EXCEPTION(103, "Incorrect phase"),
    MOTHER_NATURE_LOST_EXCEPTION(104, "CriticalError!MotherNature"),
    NEGATIVE_VALUE_EXCEPTION(105, "NegativeValue"),
    PROFESSOR_NOT_FOUND_EXCEPTION(106, "CriticalError!Professor"),
    NOT_ENOUGH_COINS_EXCEPTION(107, "NotEnoughCoins");
    final int errorCode;

    ControllerExceptions(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    final String message;
}
