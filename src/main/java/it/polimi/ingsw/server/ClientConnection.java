package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection implements Runnable {

    final private Socket socket;
    private ObjectOutputStream out;
    private Scanner in;
    final private Server server;
    private String clientRoom = null;
    private boolean isLeader = false;
    private String playerName;
    //only the lobby leader can start the game once everyone joined
    private boolean active = true;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public String getPlayerName() {
        return playerName;
    }

    private synchronized boolean isActive() {
        return active;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            sendString("Welcome!\nWhat is your name?");
            playerName = in.nextLine();

            while (server.getUserNames().contains(playerName)) {
                sendString("Ops, there is another client with the same username! Choose another one please. \n");
                playerName = in.nextLine();
            }
            server.registerUser(this, playerName);

            sendString("Possible options: \n JOIN to join a room; \n CREATE to create a new room; LOBBIES to list existing lobbies; PLAYERS to list players in current lobby;");

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
                        requestPlayersInLobby();
                        break;
                    case "lobbies":
                        requestLobbies();
                        break;
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

    private void requestLobbies() {
        sendString("List of lobbies:\n");
        for (String s : server.getRoomsList()) {
            sendString(s);
        }
    }

    private void requestPlayersInLobby() {

        sendString("List of players in lobby:\n");
        if (clientRoom != null) {
            ArrayList<ClientConnection> playersInRoom = server.getUserNamesInRoom(clientRoom);
            if (!playersInRoom.isEmpty()) {

                for (ClientConnection user: playersInRoom) {
                    if (user.isLeader)
                        sendString(user.getPlayerName()+ " *");
                    else
                        sendString(user.getPlayerName());

                }
            }
        } else {
            sendString("You're not in a room yet! Join one with the JOIN command or create a new one with CREATE!");
        }

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
        isLeader = true;
        clientRoom = nameRoom;
    }

    public synchronized void requestRoomJoin() {
        String requestedRoom;
        sendString("Select the room: \n");
        if (server.getRoomsList().isEmpty()) sendString("There are no rooms, you can only create a new one");
        else {
            sendArrayList(server.getRoomsList());
            requestedRoom = in.nextLine();
            while (!server.getRoomsList().contains(requestedRoom)) {
                sendString("Ops, there are no rooms with that name\n");
                requestedRoom = in.nextLine();
            }

            if (requestedRoom.equals(clientRoom)) {
                sendString("You're already in that room!\n");
            } else {
                isLeader = false;
                server.joinRoom(requestedRoom, this);
                clientRoom = requestedRoom;
                sendString("You entered room " + clientRoom + " successfully \n");
                sendString("Players in this room:");
                ArrayList<ClientConnection> playersInRoom = server.getUserNamesInRoom(clientRoom);
                if (!playersInRoom.isEmpty()) {
                    for (ClientConnection user: playersInRoom) {
                        if (user.isLeader)
                            sendString(user.getPlayerName()+ " *");
                        else
                            sendString(user.getPlayerName());

                    }
                    }
                }
            }
        }



    public String getClientRoom() {
        return clientRoom;
    }

    public synchronized void closeConnection() {
        sendString("Connection closed!");
        try {
            socket.close();
            server.deregisterConnection(this);
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }


    public synchronized void sendString(String message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized void sendArrayList(ArrayList<String> messageArray) {
        for (String message : messageArray) sendString(message);
    }

}
