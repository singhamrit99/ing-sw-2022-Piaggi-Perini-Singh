package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;

public class ClientAppGUI {
    /**
     * GUI app launcher.
     * @param args jar startup arguments.
     * @throws IOException Input exception.
     */
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 23023);
        client.run();
        GUI gui = new GUI(client);
        gui.start();
    }
}