package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements serverStub {
    private HashMap<ClientConnection, String> users;
    private HashMap<String, Room> rooms;

    public Server() throws RemoteException {
        users = new HashMap<>();
        rooms = new HashMap<>();
    }

    @Override
    public synchronized void registerUser(String name) throws RemoteException {
        ClientConnection c = new ClientConnection(name);
        users.put(c, name);
        System.out.println("user '" + name + "' is in Waiting List");
    }

    @Override
    public ArrayList<String> getRoomsList() throws RemoteException {
        return null;
    }

    @Override
    public void deregisterConnection(ClientConnection c) throws RemoteException {

    }

    @Override
    public void createRoom(String roomName, ClientConnection user) throws RemoteException {

    }

    @Override
    public void joinRoom(String roomName, ClientConnection user) throws RemoteException {

    }

    @Override
    public void getLobbyInfo(String playercaller) throws RemoteException {

    }

    @Override
    public void setExpertMode(String playercaller, String roomName) throws RemoteException {

    }

    @Override
    public void requestRoomCreation(String playercaller, String roomName) throws RemoteException {

    }

    @Override
    public void requestRoomJoin(String playercaller, String roomName) throws RemoteException {

    }

}

