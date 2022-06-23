package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.lang.Thread.*;
import static java.util.stream.Collectors.toCollection;

public class Server extends UnicastRemoteObject implements serverStub, Runnable {
    private HashMap<String, ClientConnection> users;
    private HashMap<String, Room> rooms;
    private boolean serverActive;

    public Server() throws RemoteException {
        users = new HashMap<>();
        rooms = new HashMap<>();
        serverActive = true;
    }

    @Override
    public synchronized void registerUser(String name) throws RemoteException, UserAlreadyExistsException {
        ClientConnection c = new ClientConnection(name);
        if (!users.containsKey(name))
            users.put(name, c);
        else throw new UserAlreadyExistsException();
        System.out.println("user '" + name + "' is registered in the server");
    }

    @Override
    public synchronized void deregisterConnection(String username) throws RemoteException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (users.get(username).getRoom() != null) {
            try {
                leaveRoom(username);
            } catch (UserNotInRoomException | UserNotRegisteredException ignored) {
            }
        }
        users.remove(username);
        System.out.println("De-registered " + username + " because inactivity");
    }

    @Override
    public synchronized ArrayList<String> getRoomsList() throws RemoteException {
        ArrayList<String> roomList = new ArrayList<>();
        for (Map.Entry<String, Room> entry : rooms.entrySet()) {
            roomList.add(entry.getKey());
        }
        return roomList;
    }

    @Override
    public synchronized void createRoom(String username, String roomName) throws RemoteException, RoomAlreadyExistsException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (rooms.containsKey(roomName)) throw new RoomAlreadyExistsException();
        ArrayList<ClientConnection> members = new ArrayList<>();
        Room newRoom = new Room(roomName, members);
        rooms.put(roomName, newRoom);
        try {
            joinRoom(username, roomName);
        } catch (RoomNotExistsException | RoomFullException | RoomInGameException ignored) {
        }
    }

    @Override
    public synchronized void joinRoom(String username, String roomName) throws RemoteException, RoomInGameException, UserNotRegisteredException, RoomNotExistsException, RoomFullException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        //if (users.get(username).getRoom() != null) throw new UserInRoomException();
        ClientConnection userClient = users.get(username);
        Room desiredRoom = rooms.get(roomName);
        if (!desiredRoom.isInGame()) {
            if (desiredRoom.getPlayers().size() < 4) {
                desiredRoom.addUser(userClient);
                userClient.setRoom(desiredRoom.getRoomName());
                System.out.println(username + " joined room " + roomName);
            }
            else throw new RoomFullException();
        }
        else{
            throw new RoomInGameException();
        }
    }

    @Override
    public synchronized boolean isInGame(String roomName)throws RoomNotExistsException{
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        return rooms.get(roomName).isInGame();
    }

    @Override
    public synchronized void leaveRoom(String username) throws RemoteException, UserNotInRoomException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        ClientConnection user = users.get(username);
        if (user.getRoom() == null) throw new UserNotInRoomException();
        String roomName = user.getRoom();
        rooms.get(roomName).removeUser(user);
/*
        if(rooms.get(roomName).isInGame()){
            rooms.get(roomName).kickOut();
        }
*/
        if (rooms.get(roomName).getPlayers().size() == 0) {
            rooms.remove(roomName);
            System.out.println("Room " + roomName + " deleted after " + username + " left room");
        }
        user.setRoom(null);
    }


    @Override
    public synchronized ArrayList<String> getPlayers(String roomName) throws RemoteException, RoomNotExistsException {
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        return rooms.get(roomName).getPlayers().stream().map(ClientConnection::getNickname).collect(toCollection(ArrayList::new));
    }

    @Override
    public synchronized ArrayList<String> getLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException {
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        ArrayList<String> result = new ArrayList<>();
        result.add(roomName);
        result.add(getPlayers(roomName).get(0));
        result.add(valueOf(isExpertMode(roomName)));
        return result;
    }

    private synchronized boolean isExpertMode(String roomName) { //only called by the server
        return rooms.get(roomName).getExpertMode();
    }

    @Override
    public synchronized void setExpertMode(String username, boolean expertMode) throws RemoteException, UserNotInRoomException, NotLeaderRoomException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (users.get(username).getRoom() == null) throw new UserNotInRoomException();
        System.out.println("Toggle expert mode request: room found\n");
        String roomName = users.get(username).getRoom();
        Room targetRoom = rooms.get(roomName);
        if (targetRoom.getPlayers().get(0).getNickname().equals(username)) {
            System.out.println("Expert mode changed \n");
            targetRoom.setExpertmode(expertMode);
        } else throw new NotLeaderRoomException();
    }

    @Override
    public synchronized void startGame(String username) throws RemoteException, NotLeaderRoomException,
            UserNotInRoomException, UserNotRegisteredException, RoomNotExistsException , NotEnoughPlayersException{
        System.out.println("Start game request received\n");
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        ClientConnection user = users.get(username);
        if (user.getRoom() == null) throw new UserNotInRoomException();
        if (!rooms.containsKey(user.getRoom())) throw new RoomNotExistsException();
        Room targetRoom = rooms.get(user.getRoom());
        if(targetRoom.getPlayers().size()<2) throw new NotEnoughPlayersException();
        if (targetRoom.getPlayers().get(0).getNickname().equals(username)) {   //only leader of the Room (players.get(0) can start the game)
            try {
                System.out.println("Game starting...");
                targetRoom.startGame();
            } catch (NegativeValueException | IncorrectArgumentException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (ClientConnection player : targetRoom.getPlayers()) {
                player.setInGame(true);
            }
        } else {
            throw new NotLeaderRoomException();
        }
    }

    @Override
    public synchronized boolean inGame(String username) throws RemoteException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        return users.get(username).inGame();
    }

    @Override
    public synchronized void performGameAction(String username, Command gameAction) throws RemoteException, MotherNatureLostException,
            NegativeValueException, AssistantCardNotFoundException, IncorrectArgumentException, IncorrectPlayerException,
            ProfessorNotFoundException, NotEnoughCoinsException, IncorrectStateException, UserNotRegisteredException, UserNotInRoomException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (users.get(username).getRoom() == null) throw new UserNotInRoomException();
        if (users.get(username).inGame()) {
            rooms.get(users.get(gameAction.getCaller()).getRoom()).commandInvoker(gameAction);
        }
    }

    @Override
    public synchronized ArrayList<PropertyChangeEvent> getUpdates(String username) throws RemoteException, UserNotRegisteredException, UserNotInRoomException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        ClientConnection userCl = users.get(username);
        if (!rooms.containsKey(userCl.getRoom())) throw new UserNotInRoomException();
        if (!userCl.inGame()) return new ArrayList<>();
        Room callerRoom = rooms.get(userCl.getRoom());
        return callerRoom.getBuffer(userCl);
    }

    @Override
    public synchronized void ping(String username) throws RemoteException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        users.get(username).setUp();
    }

    private synchronized void findDisconnectedUsers() {
        for (ClientConnection client : users.values()) {
            if (!client.isUp()) {
                try {
                    deregisterConnection(client.getNickname());
                } catch (RemoteException | UserNotRegisteredException ignored) {
                }
            } else client.setDown();
        }
    }

    @Override
    public void run() {
        while (serverActive) {
            try {
                findDisconnectedUsers();
            } catch (ConcurrentModificationException ignored) {
            }
            try {
                sleep(2000); //must be double client ping timeout
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

