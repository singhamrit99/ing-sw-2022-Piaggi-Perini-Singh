package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.GUI;

import java.io.IOException;

public class ClientAppGUI {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost",23023);
        //client.connect();
        GUI gui = new GUI(client);
        gui.start();
    }
}
