package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ViewGUI;
import java.io.IOException;

public class ClientAppGUI {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost",23023);
        client.connect();
        ViewGUI viewGUI = new ViewGUI(client);
        viewGUI.start();
    }
}
