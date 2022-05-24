package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ViewCLI;
import it.polimi.ingsw.exceptions.NotLeaderRoomException;
import it.polimi.ingsw.exceptions.UserNotInRoomException;

import java.io.IOException;

public class ClientAppCLI {
    public static void main(String[] args) throws IOException, UserNotInRoomException, NotLeaderRoomException {
        Client client = new Client("localhost",23023);
        client.connect();
        ViewCLI viewCLI = new ViewCLI(client);
        viewCLI.Start();
    }
}
