package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

import java.io.IOException;

public class ClientAppCLI {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost",23023);
    }
}
