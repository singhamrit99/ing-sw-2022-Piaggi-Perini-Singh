package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ViewCLI;
import it.polimi.ingsw.client.ViewGUI;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.server.commands.ServerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection implements Runnable {

    final private Socket socket;
    final private Server server;
    private ObjectOutputStream out;
    private Scanner in;
    private String clientRoom = null;
    private String nickname;
    private int mode;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            sendString("Welcome!\nWhat is your name?");
            nickname = in.nextLine();

            while (server.getUserNames().contains(nickname)) {
                sendString("Ops, there is another client with the same username! Choose another one please. \n");
                nickname = in.nextLine();
            }
            server.registerUser(this, nickname);
            sendString("Would you like to play in CLI(1) or GUI (2)?\n");
            mode = in.nextInt();
            while (mode<1 || mode> 2) {
                sendString("Whoops, that's not right! Try again: 1 to play in CLI or 2 to play in the GUI!\n");
                mode = in.nextInt();
            }
            if (mode == 2)
                sendString("Your preference has been set to GUI. You can change it at any time with the command MODE\n");
            else
                sendString("Your preference has been set to CLI. You can change it at any time with the command MODE\n");

            sendString("Possible options: \n JOIN to join a room; \n CREATE to create a new room;\n LOBBIES to list existing lobbies;" +
                    "\n PLAYERS to list players in current lobby; \n INFO to view your current room's information;\n CHANGE to toggle expert mode for the current lobby.\n MODE to change between CLI and GUI interface before the game starts.\n");

            String command;
            while (true) {
                command = in.nextLine().toLowerCase(Locale.ROOT);
                switch (command) {
                    case "join":
                        requestRoomJoin();
                        break;
                    case "create":
                        requestRoomCreation();
                        break;
                    case "players":
                        getPlayersInRoom();
                        break;
                    case "lobbies":
                        getRooms();
                        break;
                    case "info":
                        getLobbyInfo();
                        break;
                    case "change":
                        setExpertMode();
                        break;
                    case "mode":
                        changeMode();
                    default:
                        sendString("Command not recognized");
                        break;
                }
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error from client, " + e.getMessage());
            closeConnection();
        }
    }

    public void commandToClient(ServerCommand command){
        //TODO
    }

    public void commandToServer(Command command){
        //TODO
    }

    private void changeMode() {
        sendString("Would you like to play in CLI(1) or GUI (2)?\n");
        mode = in.nextInt();
        while (mode < 1 || mode > 2) {
            sendString("Whoops, that's not right! Try again: 1 to play in CLI or 2 to play in the GUI!\n");
            mode = in.nextInt();
        }
        if (mode == 2)
            sendString("Your preference has been set to GUI. You can change it at any time with the command MODE\n");
        else
            sendString("Your preference has been set to CLI. You can change it at any time with the command MODE\n");

    }

    private void getRooms() {
        sendString("List of lobbies:\n");
        sendArrayString(server.getRoomsList());
    }

    private void getPlayersInRoom() {
        sendString("List of players in room:\n");
        if (clientRoom != null) {
            sendArrayString(server.getNicknamesInRoom(clientRoom));
        } else {
            sendString("You're not in a room yet! Join one with the JOIN command or create a new one with CREATE!");
        }
    }

    public String getClientRoom() {
        return clientRoom;
    }

    public void getLobbyInfo() {
        sendString("Lobby Name: " + clientRoom + "\n");
        sendString("Leader " + server.getNicknamesInRoom(clientRoom).get(0));
        sendString("ExpertMode " + server.isExpertMode(clientRoom));
    }

    public void setExpertMode() {
        if (clientRoom != null) {
            if (server.isLeader(this, clientRoom)) {
                boolean result = false;
                sendString("Do you want to play in expert mode? Y/N");
                String answer;
                answer = in.nextLine().toLowerCase(Locale.ROOT);
                switch (answer) {
                    case "y": {
                        result = true;
                        sendString("Expert mode enabled!\n");
                        break;
                    }
                    case "n": {
                        result = false;
                        sendString("Expert mode disabled|\n");
                        break;
                    }
                    default:
                        sendString("Command not recognized\n");

                }
                server.setExpertModeRoom(clientRoom, result);
            } else
                sendString("You're not this lobby's leader, you can't do that!\n");
        } else
            sendString("You're not in a room now!\n");
    }

    public synchronized void requestRoomCreation() {
        sendString("Insert room name: \n");
        String nameRoom;
        nameRoom = in.nextLine();
        while (server.getRoomsList().contains(nameRoom)) {
            sendString("Ops, there is another room with the same name! Choose another one please. \n");
            nameRoom = in.nextLine();
        }
        server.createRoom(nameRoom, this);
        clientRoom = nameRoom;
    }

    public synchronized void requestRoomJoin() {
        String requestedRoom;
        sendString("Select the room: \n");
        if (server.getRoomsList().isEmpty()) sendString("There are no rooms, you can only create a new one");
        else {
            sendArrayString(server.getRoomsList());
            requestedRoom = in.nextLine();
            while (!server.getRoomsList().contains(requestedRoom)) {
                sendString("Ops, there are no rooms with that name\n");
                requestedRoom = in.nextLine();
            }
            if (requestedRoom.equals(clientRoom)) {
                sendString("You're already in that room!\n");
            } else {
                server.joinRoom(requestedRoom, this);
                clientRoom = requestedRoom;
                sendString("You entered room " + clientRoom + " successfully \n");
                sendString("Players in this room:");
                ArrayList<String> nicknamesInRoom = server.getNicknamesInRoom(clientRoom);
            }
        }
    }

    public void startView(Controller controller) {
        if (mode == 1) {
            ViewCLI view = new ViewCLI(controller);
            view.standardBehaviour();
        } else if (mode == 2) {
            ViewGUI view = new ViewGUI();
            view.standardBehaviour(controller);
        }
    }

    public synchronized void closeConnection() {
        sendString("Connection closed!");
        try {
            socket.close();
            server.deregisterConnection(this);
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
    }

    private synchronized void sendString(String message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private synchronized void sendArrayString(ArrayList<String> messageArray) {
        for (String message : messageArray) sendString(message);
    }
}
