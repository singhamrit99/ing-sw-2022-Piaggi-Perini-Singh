package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientCLI;

import java.io.IOException;

public class ClientAppCLI {
    public static void main(String[] args) throws IOException {
        ClientCLI clientCLI = new ClientCLI("127.0.0.1", 23023);
        clientCLI.run();
    }
}