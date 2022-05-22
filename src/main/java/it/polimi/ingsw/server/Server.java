package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

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
    public synchronized ArrayList<String> getRoomsList() throws RemoteException {
        ArrayList<String> roomList = new ArrayList<>();
        for(Map.Entry<String,Room> entry : rooms.entrySet()){
            roomList.add(entry.getKey());
        }
        return roomList;
    }

    @Override
    public synchronized void createRoom(String username, String roomName) throws RemoteException {
        if(!rooms.containsKey(roomName) && users.containsKey(username)){
            ArrayList<ClientConnection> members = new ArrayList<>();
            members.add(users.get(username));
            Room newRoom = new Room(roomName,members);
            rooms.put(roomName,newRoom);
        }
    }

    @Override
    public synchronized void joinRoom(String playerCaller, String roomName)  throws RemoteException {
        if(users.containsKey(playerCaller) && rooms.containsKey(roomName)){
            if(users.get(playerCaller).getRoom()==null){
                ClientConnection userClient = users.get(playerCaller);
                Room desiredRoom = rooms.get(roomName);
                if(desiredRoom.getPlayers().size()<3){
                    desiredRoom.addUser(userClient);
                    userClient.setRoom(desiredRoom.getRoomName());
                }
            }
        }
    }

    @Override
    public synchronized void leaveRoom(String playerCaller, String roomName)  throws RemoteException {
        //to do
    }

    @Override
    public synchronized boolean getExpertModeRoom(String roomName) throws RemoteException {
        if(rooms.containsKey(roomName)){
            return rooms.get(roomName).getExpertMode();
        }
        throw new RemoteException();
    }

    @Override
    public synchronized ArrayList<String> getPlayers(String roomName) throws RemoteException {
        if(rooms.containsKey(roomName)){
            return rooms.get(roomName).getPlayers().stream().map(ClientConnection::getNickname).collect(toCollection(ArrayList::new));
        }
        throw new RemoteException();
    }

    @Override
    public synchronized void setExpertMode(String playerCaller, boolean expertMode) throws RemoteException {
        if(users.containsKey(playerCaller)){
            if(rooms.containsKey(users.get(playerCaller).getRoom())){
                ClientConnection userClient = users.get(playerCaller);
                Room targetRoom = rooms.get(userClient.getRoom());
                targetRoom.setExpertmode(expertMode);
            }
        }
    }

    @Override
    public synchronized void startGame(String playerCaller) throws RemoteException{
        if(users.containsKey(playerCaller)){
            if(rooms.containsKey(users.get(playerCaller).getRoom())){
                ClientConnection userClient = users.get(playerCaller);
                Room targetRoom = rooms.get(userClient.getRoom());
                if(targetRoom.getPlayers().get(0).getNickname().equals(playerCaller)){   //only leader of the Room (players.get(0) can start the game)
                    try {
                        targetRoom.startGame();
                    } catch (NegativeValueException e) {
                        throw new RuntimeException(e);
                    } catch (IncorrectArgumentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    @Override
    public synchronized void performGameAction(Command gameAction)throws RemoteException {
        if(users.containsKey(gameAction.getCaller())){
            if(users.get(gameAction.getCaller()).isPlaying()){
                rooms.get(users.get(gameAction.getCaller()).getRoom()).commandInvoker(gameAction);
            }
        }
    }

    @Override
    public synchronized ArrayList<PropertyChangeEvent> getUpdates(String playercaller)throws RemoteException{
        if(users.containsKey(playercaller)){
            ClientConnection callerClientConnection = users.get(playercaller);
            if(callerClientConnection.isPlaying()){
                Room callerRoom = rooms.get(callerClientConnection.getRoom());
                return callerRoom.getBuffer(callerClientConnection);
            }
        }
        throw new RemoteException();
    }

}

