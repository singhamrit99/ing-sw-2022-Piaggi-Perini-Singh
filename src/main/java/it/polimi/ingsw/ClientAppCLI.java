package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;

public class ClientAppCLI {
    /**
     * Method that starts the CLI client.
     * @param args jar startup arguments.
     * @throws IOException Input exception.
     * @throws UserNotInRoomException The user that called this action is not in a room. Game exception.
     * @throws NotLeaderRoomException The user that called this action is not the room leader. Game exception.
     * @throws NotEnoughCoinsException The user that tried to activate this character card doesn't have enough coins to do so. Game exception.
     * @throws AssistantCardNotFoundException The provided assistant card can't be found. Game exception.
     * @throws NegativeValueException This game has no negative values, and any found are not correct.
     * @throws IncorrectStateException The action was performed at the wrong phase of the game. Game exception.
     * @throws MotherNatureLostException The game can't calculate Mother Nature's position. Game exception.
     * @throws ProfessorNotFoundException The professor assignment generated an error. Game exception.
     * @throws IncorrectPlayerException A player performed an action not in their turn. Game exception.
     * @throws IncorrectArgumentException A given parameter was incorrect. Game exception.
     * @throws UserNotRegisteredException The user that called the action was not correctly registered on the server.
     * @throws InterruptedException Waiting exception.
     * @throws RoomNotExistsException Thrown if the room that was looked up does not exist.
     * @throws LocalModelNotLoadedException Thrwon if the local model is null. Game exception.
     * @throws FullDiningException Thrown if the player tries to put more than 10 students of the same color in their dining room. Game exception.
     */
    public static void main(String[] args) throws IOException, UserNotInRoomException, NotLeaderRoomException, NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, UserNotRegisteredException, InterruptedException, RoomNotExistsException, LocalModelNotLoadedException, FullDiningException {
        Client client = new Client("localhost", 23023);
        client.run();
        CLI cli = new CLI(client);
        cli.Start();
    }
}
