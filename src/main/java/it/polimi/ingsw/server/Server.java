package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toCollection;

public class Server extends UnicastRemoteObject implements serverStub, Runnable{
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
    public synchronized void deregisterConnection(String username) throws RemoteException {
        if(users.containsKey(username)){
            if (rooms.containsKey(users.get(username).getRoom())){
                try{
                    leaveRoom(username);
                }
                catch(UserNotInRoomException e){
                    System.out.println("Error during client "+ username + " deletion");
                }
            }
            users.remove(username);
            System.out.println("De-registered "+ username + " because inactivity");
        }
        else{
            //TODO fare eccezione
        }
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
    public synchronized void createRoom(String username, String roomName) throws RemoteException {
        if (!rooms.containsKey(roomName) && users.containsKey(username)) {
            ArrayList<ClientConnection> members = new ArrayList<>();
            members.add(users.get(username));
            Room newRoom = new Room(roomName, members);
            rooms.put(roomName, newRoom);
        }
    }

    @Override
    public synchronized void joinRoom(String playerCaller, String roomName) throws RemoteException {
        if (users.containsKey(playerCaller) && rooms.containsKey(roomName)) {
            if (users.get(playerCaller).getRoom() == null) {
                ClientConnection userClient = users.get(playerCaller);
                Room desiredRoom = rooms.get(roomName);
                if (!desiredRoom.isInGame()) {
                    if (desiredRoom.getPlayers().size() < 3) {
                        desiredRoom.addUser(userClient);
                        userClient.setRoom(desiredRoom.getRoomName());
                        System.out.println(playerCaller + " joined room " + roomName);
                    }
                }
            }
        }
    }

    @Override
    public synchronized void leaveRoom(String playerCaller) throws RemoteException, UserNotInRoomException {
        if (users.containsKey(playerCaller)) {
            if(users.get(playerCaller).getRoom()!=null){
                String roomName = users.get(playerCaller).getRoom();
                rooms.get(roomName).getPlayers().remove(users.get(playerCaller));
                if(rooms.get(roomName).getPlayers().size()==0){
                    rooms.remove(roomName);
                    System.out.println("Room" + roomName + " deleted after"+ playerCaller + "left room");
                }
                else System.out.println("User " + playerCaller + " left room " + roomName);
                users.get(playerCaller).setRoom(null);
            }
            else throw new UserNotInRoomException();
        }
    }

    @Override
    public synchronized boolean getExpertModeRoom(String roomName) throws RemoteException {
        if (rooms.containsKey(roomName)) {
            return rooms.get(roomName).getExpertMode();
        }
        throw new RemoteException();
    }

    @Override
    public synchronized ArrayList<String> getPlayers(String roomName) throws RemoteException {

        if (rooms.containsKey(roomName)) {
            return rooms.get(roomName).getPlayers().stream().map(ClientConnection::getNickname).collect(toCollection(ArrayList::new));
        } else
            throw new RemoteException();
    }

    @Override
    public synchronized ArrayList<String> getLobbyInfo(String roomName) throws RemoteException {
        ArrayList<String> result = new ArrayList<>();
        result.add(roomName);
        result.add(getPlayers(roomName).get(0));
        result.add(valueOf(isExpertMode(roomName)));
        return result;
    }

    @Override
    public synchronized boolean isExpertMode(String roomName) throws RemoteException {
        if (rooms.containsKey(roomName))
            return rooms.get(roomName).getExpertMode();
        else
            throw new RemoteException();
    }

    @Override
    public synchronized void setExpertMode(String playerCaller, boolean expertMode) throws RemoteException, UserNotInRoomException,NotLeaderRoomException {
        if (users.containsKey(playerCaller)) {
            if(users.get(playerCaller).getRoom()!=null){
                System.out.println("Toggle expert mode request: room found\n");
                String roomName = users.get(playerCaller).getRoom();
                Room targetRoom = rooms.get(roomName);
                if (targetRoom.getPlayers().get(0).getNickname().equals(playerCaller)) {
                    System.out.println("Expert mode changed \n");
                    targetRoom.setExpertmode(expertMode);
                }
                else throw new NotLeaderRoomException();
            }
            else throw new UserNotInRoomException();
        }
    }

    @Override
    public synchronized void startGame(String playerCaller) throws RemoteException, NotLeaderRoomException, UserNotInRoomException {
        if (users.containsKey(playerCaller)) {
            if(users.get(playerCaller).getRoom()!=null){
                if (rooms.containsKey(users.get(playerCaller).getRoom())) {
                    ClientConnection userClient = users.get(playerCaller);
                    Room targetRoom = rooms.get(userClient.getRoom());
                    if (targetRoom.getPlayers().get(0).getNickname().equals(playerCaller)){   //only leader of the Room (players.get(0) can start the game)
                        try {
                            targetRoom.startGame();
                        } catch (NegativeValueException e) {
                            throw new RuntimeException(e);
                        } catch (IncorrectArgumentException e) {
                            throw new RuntimeException(e);
                        }
                        for (ClientConnection player : targetRoom.getPlayers()) {
                            player.setInGame(true);
                        }
                    }
                    else{
                        throw new NotLeaderRoomException();
                    }
                }
            }
            else{
                throw new UserNotInRoomException();
            }
        }
    }

    @Override
    public boolean inGame(String username) throws RemoteException {
        if (users.containsKey(username)) {
            return users.get(username).inGame();
        }
        return false; //TODO exception
    }

    @Override
    public synchronized void performGameAction(Command gameAction) throws RemoteException, MotherNatureLostException, NegativeValueException, AssistantCardNotFoundException, IncorrectArgumentException, IncorrectPlayerException, ProfessorNotFoundException, NotEnoughCoinsException, IncorrectStateException { //TODO ControllerException
        if (users.containsKey(gameAction.getCaller())) {
            if (users.get(gameAction.getCaller()).inGame()) {
                rooms.get(users.get(gameAction.getCaller()).getRoom()).commandInvoker(gameAction);
            }
        }
    }

    @Override
    public synchronized ArrayList<PropertyChangeEvent> getUpdates(String playerCaller) throws RemoteException {
        if (users.containsKey(playerCaller)) {
            ClientConnection callerClientConnection = users.get(playerCaller);
            if (callerClientConnection.inGame()) {
                Room callerRoom = rooms.get(callerClientConnection.getRoom());
                return callerRoom.getBuffer(callerClientConnection);
            }
        }
        throw new RemoteException();
    }

    @Override
    public synchronized void ping(String nickname) throws RemoteException{
        if(users.containsKey(nickname)){
            users.get(nickname).setUp();
        }
    }

    private void findDisconnectedUsers() throws RemoteException{
        for(ClientConnection client : users.values()){
            if(!client.isUp()){
                deregisterConnection(client.getNickname());
            }
            else client.setDown();
        }
    }

    @Override
    public void run() {
        while(serverActive){
            try {
                findDisconnectedUsers();
                Thread.sleep(1000); //must be double client ping timeout
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

