package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUI.controller.Controller;
import it.polimi.ingsw.client.GUI.controller.ResourcesPath;
import it.polimi.ingsw.client.GUI.controller.RoomListController;
import it.polimi.ingsw.client.UI;
import javafx.application.Application;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUI implements UI {
    public static Client client;
    public static Controller controller;

    private final AtomicBoolean isDoing;

    public GUI(Client client) {
        this.isDoing = new AtomicBoolean(false);
        GUI.client = client;
        GUI.client.setUI(this);
    }

    /**
     * Starts the GUI
     */
    public void start() {
        Application.launch(GUILauncher.class);
    }

    public void startAction() {
        if (!isDoing.get()) {
            isDoing.set(true);
        }
    }

    public void stopAction() {
        if (isDoing.get()) {
            isDoing.set(false);
        }
    }

    public boolean isDoing() {
        return this.isDoing.get();
    }

    /**
     * Starts the window that shows the list of rooms available
     *
     * @param rooms list of rooms
     */
    public void roomsAvailable(ArrayList<String> rooms) {
        Platform.runLater(() -> {
            RoomListController roomListController = new RoomListController(this);
            roomListController.setRoomsList(rooms);

            Controller.startStage(ResourcesPath.ROOM_LIST, roomListController);
            controller.closeStage();
        });
    }
}