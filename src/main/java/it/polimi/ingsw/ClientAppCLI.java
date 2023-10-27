package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;

public class ClientAppCLI {
    /**
     * Method that starts the CLI client.
     *
     * @param args jar startup arguments.
     * @throws InterruptedException  thrown when thread is interrupted from sleep state.
     */
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("localhost", 23023);
        client.run();
        CLI cli = new CLI(client);
        cli.Start();
    }
}
