package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;

public class GUI {
    public static Client client;

    public GUI(Client client) {
        GUI.client = client;
    }

    /**
     * Starts the GUI
     */
    public void start() {
        Application.launch(GUILauncher.class);
    }
}