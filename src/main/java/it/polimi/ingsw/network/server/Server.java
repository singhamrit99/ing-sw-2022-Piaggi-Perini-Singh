package it.polimi.ingsw.network.server;

import it.polimi.ingsw.StringNames;
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
    private final HashMap<String, ClientConnection> users;
    private HashMap<String, Room> rooms;
    private boolean serverActive;

    /**
     * Server class builder
     *
     * @throws RemoteException Thrown in case of a network error.
     */
    public Server() throws RemoteException {
        users = new HashMap<>();
        rooms = new HashMap<>();
        serverActive = true;
    }

    /**
     * Registers a new user on the server.
     *
     * @param name the name of the user to register.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserAlreadyExistsException Thrown in case of a duplicate user.
     */
    @Override
    public synchronized void registerUser(String name) throws RemoteException, UserAlreadyExistsException, NameFieldException {
        controlName(name);
        ClientConnection c = new ClientConnection(name);
        if (!users.containsKey(name))
            users.put(name, c);
        else throw new UserAlreadyExistsException();
        System.out.println("User " + name + " registered.");
    }

    /**
     * to find wrong names of roomName and clientName
     *
     * @param name
     * @throws NameFieldException
     */
    private void controlName(String name) throws NameFieldException {
        if (name == null || name.trim().isEmpty() || name.length() > 14) {
            throw new NameFieldException();
        }
    }


    /**
     * De-Registers connections.
     *
     * @param username The username of the user to remover.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown if the selected player can't be found on the server.
     */
    @Override
    public synchronized void deregisterConnection(String username) throws RemoteException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (users.get(username).getRoom() != null) {
            try {
                String roomName = users.get(username).getRoom();
                if (rooms.containsKey(roomName)) {
                    if (rooms.get(roomName).isInGame())
                        leaveGame(username); // leaveGame() so that the other players are notified
                    else leaveRoom(username);
                }
            } catch (UserNotInRoomException | UserNotRegisteredException ignored) {
            }
        }
        users.remove(username);
        System.out.println("User " + username + " de-registered because disconnection.");
    }

    /**
     * Returns the list of available rooms on the server.
     *
     * @return the list of available rooms.
     * @throws RemoteException Thrown in case of a network error.
     */
    @Override
    public synchronized ArrayList<String> getRoomsList() throws RemoteException {
        ArrayList<String> roomList = new ArrayList<>();
        for (Map.Entry<String, Room> entry : rooms.entrySet()) {
            roomList.add(entry.getKey());
        }
        return roomList;
    }

    /**
     * Creates a room and sets the user that created it as its leader.
     *
     * @param username The user that called this action.
     * @param roomName The name of the room.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws RoomAlreadyExistsException Thrown if the provided room name already exists on the server.
     * @throws UserNotRegisteredException Thrown in case of client error(user is not present on the server).
     */
    @Override
    public synchronized void createRoom(String username, String roomName) throws RemoteException, RoomAlreadyExistsException, UserNotRegisteredException, NameFieldException {
        controlName(roomName);
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (rooms.containsKey(roomName)) throw new RoomAlreadyExistsException();
        ArrayList<ClientConnection> members = new ArrayList<>();
        Room newRoom = new Room(roomName, members);
        rooms.put(roomName, newRoom);
        System.out.println("Room " + roomName + " created by user " + username);
        try {
            joinRoom(username, roomName);
        } catch (RoomNotExistsException | UserInRoomException | RoomFullException | RoomInGameException ignored) {
        }
    }

    /**
     * The method used to join an existing room.
     *
     * @param username The user that is requesting the action
     * @param roomName The room the user wants to join.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws RoomInGameException        The room that the client tried to join was already playing a game of Eriantys
     * @throws UserNotRegisteredException Thrown in case of client error(user is not present on the server).
     * @throws RoomNotExistsException     The requested room can't be found on the server.
     * @throws RoomFullException          The room already has 4 players.
     * @throws UserInRoomException        The user is already in the room.
     */
    @Override
    public synchronized void joinRoom(String username, String roomName) throws RemoteException, RoomInGameException, UserNotRegisteredException, RoomNotExistsException, RoomFullException, UserInRoomException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        if (users.get(username).getRoom() != null) throw new UserInRoomException();
        ClientConnection userClient = users.get(username);
        Room desiredRoom = rooms.get(roomName);
        if (!desiredRoom.isInGame()) {
            if (desiredRoom.getPlayers().size() < 4) {
                desiredRoom.addUser(userClient);
                userClient.setRoom(desiredRoom.getRoomName());
            } else throw new RoomFullException();
        } else {
            throw new RoomInGameException();
        }
        System.out.println("Room " + roomName + " joined by user " + username);
    }

    /**
     * Returns if the given room is playing or not.
     *
     * @param roomName the chosen room.
     * @return true or false depending on the outcome.
     * @throws RoomNotExistsException The requested room can't be found on the server.
     */
    @Override
    public synchronized boolean isInGame(String roomName) throws RoomNotExistsException {
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        return rooms.get(roomName).isInGame();
    }

    /**
     * Method used to leave a room previously entered. In case of the leader leaving the room the next highest player will be appointed leader.
     * If there are no more players in the room it will be deleted.
     *
     * @param username The player that is trying to leave the room.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotInRoomException     Thrown if this method is called when the user is not inside a room.
     * @throws UserNotRegisteredException Thrown if the provided username is not present on the server.
     */
    @Override
    public synchronized void leaveRoom(String username) throws RemoteException, UserNotInRoomException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        ClientConnection user = users.get(username);
        if (user.getRoom() == null) throw new UserNotInRoomException();
        user.setInGame(false);
        String roomName = user.getRoom();
        rooms.get(roomName).removeUser(user);
        user.setRoom(null);
        if (rooms.get(roomName).getPlayers().size() == 0) { //complete deletion of the room in case is empty
            rooms.remove(roomName);
            System.out.println("Room " + roomName + " deleted after that " + username + " leaved the room");
        } else System.out.println("User " + username + " leaved room " + roomName);
    }

    /**
     * Method used by the first player who leave the room while the game is being played.
     * This method is only accessible from inside the room. It's similar to leaveRoom, but it
     * also notifies all the other players that the game is finished because the leaving.
     *
     * @param username The player that wants to leave the game.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown if the provided username is not present on the server.
     */
    public synchronized void leaveGame(String username) throws RemoteException, UserNotRegisteredException {
        String roomName = users.get(username).getRoom();
        try {
            leaveRoom(username);
        } catch (UserNotInRoomException ignored) {
        }

        PropertyChangeEvent gameFinishedEvent = new PropertyChangeEvent(this, "game-finished", username, null);
        rooms.get(roomName).notifyPlayerInGameLeaves(gameFinishedEvent);
    }

    /**
     * Returns the players in a room.
     *
     * @param roomName the room we're querying.
     * @return the list of players in the room.
     * @throws RemoteException        Thrown if the provided username is not present on the server.
     * @throws RoomNotExistsException The provided room name can't be found on the server.
     */
    @Override
    public synchronized ArrayList<String> getPlayers(String roomName) throws RemoteException, RoomNotExistsException {
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        return rooms.get(roomName).getPlayers().stream().map(ClientConnection::getNickname).collect(toCollection(ArrayList::new));
    }

    /**
     * Returns various lobby information.
     *
     * @param roomName The name of the queried room.
     * @return room name, players and whether it is set to expert or standard mode.
     * @throws RemoteException        Thrown in case of a network error.
     * @throws RoomNotExistsException Thrown if the provided username is not present on the server.
     */
    @Override
    public synchronized ArrayList<String> getLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException {
        if (!rooms.containsKey(roomName)) throw new RoomNotExistsException();
        ArrayList<String> result = new ArrayList<>();
        result.add(roomName);
        result.add(getPlayers(roomName).get(0));
        result.add(valueOf(isExpertMode(roomName)));
        return result;
    }

    /**
     * Getter method for expert mode given a room name.
     *
     * @param roomName the room to query.
     * @return true or false depending on expert mode setting
     */
    private synchronized boolean isExpertMode(String roomName) { //only called by the server
        return rooms.get(roomName).getExpertMode();
    }

    /**
     * Method to set a particular room to expert mode. Can only be done by the room's leader.
     *
     * @param username   The user that requested this action
     * @param expertMode The value expert mode is to be set to.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotInRoomException     Thrown if the user is not in a room.
     * @throws NotLeaderRoomException     Thrown if the user is not the leader of the room.
     * @throws UserNotRegisteredException Thrown if the provided username is not present on the server.
     */
    @Override
    public synchronized void setExpertMode(String username, boolean expertMode) throws RemoteException, UserNotInRoomException, NotLeaderRoomException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (users.get(username).getRoom() == null) throw new UserNotInRoomException();
        String roomName = users.get(username).getRoom();
        Room targetRoom = rooms.get(roomName);
        if (targetRoom.getPlayers().get(0).getNickname().equals(username)) targetRoom.setExpertmode(expertMode);
        else throw new NotLeaderRoomException();
    }

    /**
     * Method used to start the game
     *
     * @param username The player that requested this action.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws NotLeaderRoomException     Thrown when the player that tried
     * @throws UserNotInRoomException     Thrown when the user is not in a room.
     * @throws UserNotRegisteredException Thrown if the provided username is not present on the server.
     * @throws RoomNotExistsException     Thrown if the provided room name isn't present on the server.
     * @throws NotEnoughPlayersException  Thrown when there's only 1 player in the room and a game is trying to start.
     */
    @Override
    public synchronized void startGame(String username) throws RemoteException, NotLeaderRoomException,
            UserNotInRoomException, UserNotRegisteredException, RoomNotExistsException, NotEnoughPlayersException,
            NegativeValueException, IncorrectArgumentException, InterruptedException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        ClientConnection user = users.get(username);
        if (user.getRoom() == null) throw new UserNotInRoomException();
        if (!rooms.containsKey(user.getRoom())) throw new RoomNotExistsException();
        Room targetRoom = rooms.get(user.getRoom());
        if (targetRoom.getPlayers().size() < 2) throw new NotEnoughPlayersException();
        //only leader of the Room (players.get(0) can start the game)
        if (targetRoom.getPlayers().get(0).getNickname().equals(username)) {
            targetRoom.startGame();
            System.out.println("Game in " + targetRoom.getRoomName() + " started");
            for (ClientConnection player : targetRoom.getPlayers()) player.setInGame(true);
        } else {
            throw new NotLeaderRoomException();
        }
    }

    /**
     * Used to check if a client is in game.
     *
     * @param username the user to check.
     * @return whether the user is in the room or not.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown if the provided username is not present on the server.
     */
    @Override
    public synchronized boolean inGame(String username) throws RemoteException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        return users.get(username).inGame();
    }

    /**
     * Method used to perform a game action.
     *
     * @param username   The user that called this action
     * @param gameAction the Action Command.
     * @throws RemoteException                Thrown in case of a network error.
     * @throws MotherNatureLostException      Thrown if the game can't calculate Mother Nature's position.
     * @throws NegativeValueException         As always, this game has no negative values, and any found are automatically incorrect.
     * @throws AssistantCardNotFoundException Thrown when the provided assistant card name can't be found in the deck.
     * @throws IncorrectArgumentException     Thrown if any of the parameters used by the method are invalid.
     * @throws IncorrectPlayerException       Thrown if the player that called the method isn't the current player.
     * @throws ProfessorNotFoundException     If the method call causes a professor gain or loss and that generates an error this exception is thrown.
     * @throws NotEnoughCoinsException        Thrown if the player that tried to play the card doesn't have enough coins to buy it.
     * @throws IncorrectStateException        Thrown if action is being performed in an invalid phase of the turn.
     * @throws UserNotRegisteredException     Thrown if the provided username is not present on the server.
     * @throws UserNotInRoomException         Thrown when the user is not in a room.
     */
    @Override
    public synchronized void performGameAction(String username, Command gameAction) throws RemoteException, MotherNatureLostException,
            NegativeValueException, AssistantCardNotFoundException, IncorrectArgumentException, IncorrectPlayerException,
            ProfessorNotFoundException, NotEnoughCoinsException, IncorrectStateException, UserNotRegisteredException, UserNotInRoomException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        if (users.get(username).getRoom() == null) throw new UserNotInRoomException();
        if (users.get(username).inGame()) {
            rooms.get(users.get(gameAction.getCaller()).getRoom()).commandInvoker(gameAction);
        }
    }

    /**
     * Method used by the clients to get the updates of the game. Each client has his 'buffer of game events' .
     *
     * @param username The user that requested the update.
     * @return The event buffer.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown if the provided username is not present on the server.
     * @throws UserNotInRoomException     Thrown when the user is not in a room.
     */
    @Override
    public synchronized ArrayList<PropertyChangeEvent> getUpdates(String username) throws RemoteException, UserNotRegisteredException, UserNotInRoomException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        ClientConnection userCl = users.get(username);
        if (!rooms.containsKey(userCl.getRoom())) throw new UserNotInRoomException();
        if (!userCl.inGame()) return new ArrayList<>();
        Room callerRoom = rooms.get(userCl.getRoom());
        return callerRoom.getBuffer(userCl);
    }

    /**
     * Method used to find disconnected users. It is based on 'ping-pong system' .
     */
    @Override
    public void run() {
        while (serverActive) {
            try {
                findDisconnectedUsers();
                sleep(500); //must be double client ping timeout
            } catch (ConcurrentModificationException concurrentError) {
                System.out.println("Server warning: concurrent modification while disconnecting managed.");
            } catch (InterruptedException e) {
                System.out.println("Server error: server run() interrupted.");
            }
        }
    }

    /**
     * Method to check if any clients are disconnected. It uses a 'ping-pong' system style to
     * find the players who ping the server. If the client has correctly marked its ping value on 'up'
     * the server set it down (pong). If the client has disconnected, the server found the ping value has 'down'
     * and de-register the client.
     */
    private synchronized void findDisconnectedUsers() {
        ArrayList<ClientConnection> usersToRemove = new ArrayList<>();

        synchronized (users) {
            for (ClientConnection client : users.values()) {
                if (!client.isUp()) {
                    usersToRemove.add(client);
                } else client.setDown();
            }
            for (ClientConnection clientToRemove : usersToRemove) {
                try {
                    deregisterConnection(clientToRemove.getNickname());
                } catch (RemoteException | UserNotRegisteredException ignored) {} //inside the server can't be 'remoteException' and UserNotRegistered is not harmful in this case
            }
        }
    }

    /**
     * Ping method used by clients to set 'up' or to 'ping' their connection.
     * The ping is registered in the specific client connection that represents the client.
     *
     * @param username The client name.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown when the user is not in a room.
     */
    @Override
    public synchronized void ping(String username) throws RemoteException, UserNotRegisteredException {
        if (!users.containsKey(username)) throw new UserNotRegisteredException();
        users.get(username).setUp();
    }

}

