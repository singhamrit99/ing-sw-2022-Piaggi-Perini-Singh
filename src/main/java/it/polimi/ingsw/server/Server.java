package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.commands.Command;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server extends UnicastRemoteObject implements serverStub {
    private HashMap<String,ClientConnection> users;
    private HashMap<String, Room> rooms;

    public Server() throws RemoteException {
        users = new HashMap<>();
        rooms = new HashMap<>();
    }

    @Override
    public synchronized void registerUser(String name) throws RemoteException {
        ClientConnection c = new ClientConnection(name);
        users.put(name,c);
        System.out.println("user '" + name + "' is in Waiting List");
    }

    @Override
    public synchronized void deregisterConnection(String username) throws RemoteException {
        ClientConnection clientToRemove = users.get(username);
        if(rooms.containsKey(clientToRemove.getRoom()))rooms.get(clientToRemove.getRoom()).removeUser(clientToRemove);
        users.remove(clientToRemove);
    }

    @Override
    public synchronized HashMap<String, Room> getRoomsList() throws RemoteException {
        return rooms;
    }

    @Override
    public synchronized void createRoom(String roomName, ClientConnection user) throws RemoteException {
        ArrayList<ClientConnection> newRoomUsers = new ArrayList<ClientConnection>();
        newRoomUsers.add(user);
        Room newRoom = new Room(roomName,newRoomUsers);
    }

    @Override
    public synchronized void joinRoom(String roomName, ClientConnection user) throws RemoteException {

    }

    @Override
    public synchronized void getLobbyInfo(String playercaller) throws RemoteException {

    }

    @Override
    public synchronized void setExpertMode(String playercaller, String roomName) throws RemoteException {

    }

    @Override
    public synchronized void startGame(String playercaller) throws RemoteException {

    }


    @Override
    public synchronized void performGameAction(Command gameAction)throws RemoteException {
        if(users.containsKey(gameAction.getCaller())){
            if(users.get(gameAction.getCaller()).isPlaying()){
                rooms.get(users.get(gameAction.getCaller()).getRoom()).commandInvoker(gameAction);
            }
        }
    }


    public synchronized String testPing()throws RemoteException{
        return "ping";
    }

}

