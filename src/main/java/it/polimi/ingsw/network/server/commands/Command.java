package it.polimi.ingsw.network.server.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;


public interface Command {

    String getCaller();

    /**
     * The general command
     *
     * @param controller Every command is passed to the controller, which then invokes the corresponding action in the Model.
     * @throws IncorrectPlayerException       Thrown if the player that called the method isn't the current player.
     * @throws NegativeValueException         As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException     Thrown if any of the parameters used by the method are invalid.
     * @throws MotherNatureLostException      Thrown if the game can't calculate Mother Nature's position.
     * @throws IncorrectStateException        Thrown if the method is called in an illegal phase.
     * @throws ProfessorNotFoundException     Thrown in case of an error regarding professors.
     * @throws NotEnoughCoinsException        Thrown if the player that tried to play the character card doesn't have enough coins to buy it.
     * @throws AssistantCardNotFoundException Thrown if the given string doesn't correspond to any Assistant card in the deck.
     */
    void execute(Controller controller) throws IncorrectPlayerException, NegativeValueException, IncorrectArgumentException, MotherNatureLostException, IncorrectStateException, ProfessorNotFoundException, NotEnoughCoinsException, AssistantCardNotFoundException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed;
}
