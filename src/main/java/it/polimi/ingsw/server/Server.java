package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Remote {

    private static final int PORT = 23023;
    final private ServerSocket serverSocket;
    final private ExecutorService executor = Executors.newFixedThreadPool(128);
    private HashMap<ClientConnection, String> users;
    private HashMap<String, Room> rooms;

    public Server() throws IOException, RemoteException, AlreadyBoundException {
        System.out.println("Binding server implementation to registry...");
        Registry registry = LocateRegistry.getRegistry();
        registry.bind("server", this);
        System.out.println("Waiting for invocations from clients...");
    }
    public void run() {
        users = new HashMap<>();
        rooms = new HashMap<>();
        System.out.println("Server has started");
        while (true) {
            try {
                Socket newSocket = serverSocket.accept();
                ClientConnection socketConnection = new ClientConnection(newSocket, this);
                System.out.println("Just connected a new dude! #");
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    public synchronized void registerUser(ClientConnection c, String name) {
        users.put(c, name);
        System.out.println("user '" + name + "' is in Waiting List");
    }

    public ArrayList<String> getUserNames() {
        return new ArrayList<>(users.values());
    }

    public ArrayList<String> getRoomsList() {
        return new ArrayList<>(rooms.keySet());
    }

    public Room getClientRoom(String roomName) {
        return rooms.get(roomName);
    }

    public synchronized void deregisterConnection(ClientConnection c) {
        System.out.println("Client deregistered");
        users.remove(c);
    }

    public synchronized void createRoom(String roomName, ClientConnection user) {
        ArrayList<ClientConnection> userInNewRoom = new ArrayList<>();
        userInNewRoom.add(user);
        Room newRoom = new Room(roomName, userInNewRoom);
        rooms.put(newRoom.getRoomName(), newRoom);
        System.out.println("User " + users.get(user) + " just created " + roomName + "\n");
    }

    public synchronized void joinRoom(String roomName, ClientConnection user) {
        if (rooms.containsKey(roomName)) {
            Room newUsers;
            newUsers = rooms.get(roomName);
            if (user.getClientRoom() != null) {
                Room oldLobby;
                oldLobby = rooms.get(user.getClientRoom());
                oldLobby.removeUser(user);
                System.out.println("Player " + user.getNickname() + " in lobby " + user.getClientRoom() + " changed lobby\n");
            }
            newUsers.addUser(user);
            rooms.replace(roomName, newUsers);
        }
    }

    public synchronized ArrayList<String> getNicknamesInRoom(String roomName) {
        ArrayList<ClientConnection> players = rooms.get(roomName).getPlayers();
        ArrayList<String> nicknames = new ArrayList<>();
        for (ClientConnection cl : players) nicknames.add(cl.getNickname());
        return nicknames;
    }

    public synchronized boolean isExpertMode(String roomName) {
        return rooms.get(roomName).getExpertMode();
    }

    public synchronized boolean isLeader(ClientConnection cl, String roomName) {
        if (cl.getNickname().equals(getNicknamesInRoom(roomName).get(0))) {
            return true;
        } else {
            return false;
        }
    }
    public synchronized void setExpertModeRoom(String roomName, Boolean expertMode) {
        rooms.get(roomName).setExpertmode(expertMode);
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

    public void getLobbyInfo(String playercaller) {
        getPlayersInRoom();
        sendString("Lobby Name: " + clientRoom + "\n");
        sendString("Leader " + server.getNicknamesInRoom(clientRoom).get(0));
        sendString("ExpertMode " + server.isExpertMode(clientRoom));

    }

    public void setExpertMode() {
        //server.setExpertModeRoom(clientRoom, result);
    }

    public synchronized void requestRoomCreation(String playercaller) {
        //if (!server.getRoomsList().contains(nameRoom))
        //  server.createRoom(nameRoom, this);
        //clientRoom = nameRoom;
    }

    public synchronized void requestRoomJoin() {
//todo remember to check if there is already in the room etc.
    }

}

