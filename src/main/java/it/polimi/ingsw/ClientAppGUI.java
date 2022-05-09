package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientCLI;
import it.polimi.ingsw.client.ClientGUI;

import java.io.IOException;

public class ClientAppGUI {
    public static void main(String[] args) throws IOException {
        ClientGUI clientGUI = new ClientGUI("127.0.0.1", 23023);
        clientGUI.run();
    }
}
