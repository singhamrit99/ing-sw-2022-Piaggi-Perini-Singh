package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;

public class ClientAppCLI {
    public static void main(String[] args) throws IOException, UserNotInRoomException, NotLeaderRoomException, NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, UserNotRegisteredException, InterruptedException, RoomNotExistsException, LocalModelNotLoadedException, FullDiningException {
        Client client = new Client("localhost", 23023);
        client.run();
        CLI cli = new CLI(client);
        cli.Start();
    }
}
