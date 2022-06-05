package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface serverStub extends Remote {

    void registerUser(String name) throws RemoteException, UserAlreadyExistsException;

    void deregisterConnection(String username) throws RemoteException, UserNotRegisteredException;

    ArrayList<String> getRoomsList() throws RemoteException;

    void createRoom(String username, String roomName) throws RemoteException, RoomAlreadyExistsException, UserNotRegisteredException;

    void joinRoom(String username, String roomName) throws RemoteException, UserNotRegisteredException, RoomNotExistsException, RoomFullException;

    void leaveRoom(String username) throws RemoteException, UserNotInRoomException, UserNotRegisteredException;

    ArrayList<String> getPlayers(String roomName) throws RemoteException, RoomNotExistsException;

    ArrayList<String> getLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException;

    void setExpertMode(String username, boolean expertMode) throws RemoteException, UserNotInRoomException, NotLeaderRoomException, UserNotRegisteredException;

    void startGame(String username) throws RemoteException, NotLeaderRoomException,
            UserNotInRoomException, UserNotRegisteredException, RoomNotExistsException,NotEnoughPlayersException;

    boolean inGame(String username) throws RemoteException, UserNotRegisteredException;

    void performGameAction(String username, Command gameAction) throws RemoteException, MotherNatureLostException,
            NegativeValueException, AssistantCardNotFoundException, IncorrectArgumentException, IncorrectPlayerException,
            ProfessorNotFoundException, NotEnoughCoinsException, IncorrectStateException, UserNotRegisteredException, UserNotInRoomException;

    ArrayList<PropertyChangeEvent> getUpdates(String username) throws RemoteException, UserNotRegisteredException, UserNotInRoomException;

    void ping(String username) throws RemoteException, UserNotRegisteredException;

}
