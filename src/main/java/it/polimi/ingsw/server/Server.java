package it.polimi.ingsw.server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements Remote {
    private HashMap<ClientConnection, String> users;
    private HashMap<String, Room> rooms;

    public Server() throws IOException{
        users = new HashMap<>();
        rooms = new HashMap<>();
    }
    public synchronized void registerUser(String name) throws RemoteException {
        ClientConnection c = new ClientConnection(name);
        users.put(c, name);
        System.out.println("user '" + name + "' is in Waiting List");
    }

    public ArrayList<String> getUserNames() throws RemoteException {
        return new ArrayList<>(users.values());
    }

    public ArrayList<String> getRoomsList() throws RemoteException {
        return new ArrayList<>(rooms.keySet());
    }

    public Room getClientRoom(String roomName) throws RemoteException {
        return rooms.get(roomName);
    }

    public synchronized void deregisterConnection(ClientConnection c)throws RemoteException {
        System.out.println("Client deregistered");
        users.remove(c);
    }

    public synchronized void createRoom(String roomName, ClientConnection user)throws RemoteException {
        ArrayList<ClientConnection> userInNewRoom = new ArrayList<>();
        userInNewRoom.add(user);
        Room newRoom = new Room(roomName, userInNewRoom);
        rooms.put(newRoom.getRoomName(), newRoom);
        System.out.println("User " + users.get(user) + " just created " + roomName + "\n");
    }

    public synchronized void joinRoom(String roomName, ClientConnection user)throws RemoteException {
        if (rooms.containsKey(roomName)) {
            Room newUsers;
            newUsers = rooms.get(roomName);
            if (user.getRoom() != null) {
                Room oldLobby;
                oldLobby = rooms.get(user.getRoom());
                oldLobby.removeUser(user);
                System.out.println("Player " + user.getNickname() + " in lobby " + user.getRoom() + " changed lobby\n");
            }
            newUsers.addUser(user);
            rooms.replace(roomName, newUsers);
        }
    }

    public synchronized ArrayList<String> getNicknamesInRoom(String roomName) throws RemoteException {
        ArrayList<ClientConnection> players = rooms.get(roomName).getPlayers();
        ArrayList<String> nicknames = new ArrayList<>();
        for (ClientConnection cl : players) nicknames.add(cl.getNickname());
        return nicknames;
    }

    public synchronized boolean isExpertMode(String roomName)throws RemoteException {
        return rooms.get(roomName).getExpertMode();
    }

    public synchronized boolean isLeader(ClientConnection cl, String roomName)throws RemoteException {
        if (cl.getNickname().equals(getNicknamesInRoom(roomName).get(0))) {
            return true;
        } else {
            return false;
        }
    }
    public synchronized void setExpertModeRoom(String roomName, Boolean expertMode)throws RemoteException {
        rooms.get(roomName).setExpertmode(expertMode);
    }

    private void getRooms() throws RemoteException{
        //
    }

    private void getPlayersInRoom() throws RemoteException{
        //
    }

    public void getLobbyInfo(String playercaller) throws RemoteException{
        getPlayersInRoom();
        //
    }

    public void setExpertMode() throws RemoteException{
        //server.setExpertModeRoom(clientRoom, result);
    }

    public synchronized void requestRoomCreation(String playercaller) throws RemoteException {
        //if (!server.getRoomsList().contains(nameRoom))
        //  server.createRoom(nameRoom, this);
        //clientRoom = nameRoom;
    }

    public synchronized void requestRoomJoin() throws RemoteException {
//todo remember to check if there is already in the room etc.
    }

}

