package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface serverStub extends Remote {

    boolean isInGame(String roomName) throws RemoteException, RoomNotExistsException;

    void registerUser(String name) throws RemoteException, UserAlreadyExistsException, NameFieldException;

    void deregisterConnection(String username) throws RemoteException, UserNotRegisteredException;

    ArrayList<String> getRoomsList() throws RemoteException;

    void createRoom(String username, String roomName) throws RemoteException, RoomAlreadyExistsException, UserNotRegisteredException, NameFieldException;

    void joinRoom(String username, String roomName) throws RemoteException, RoomInGameException, UserNotRegisteredException, RoomNotExistsException, RoomFullException, UserInRoomException;

    void leaveRoom(String username) throws RemoteException, UserNotInRoomException, UserNotRegisteredException;

    ArrayList<String> getPlayers(String roomName) throws RemoteException, RoomNotExistsException;

    ArrayList<String> getLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException;

    void setExpertMode(String username, boolean expertMode) throws RemoteException, UserNotInRoomException, NotLeaderRoomException, UserNotRegisteredException;

    void startGame(String username) throws RemoteException, NotLeaderRoomException,
            UserNotInRoomException, UserNotRegisteredException, RoomNotExistsException, NotEnoughPlayersException, NegativeValueException, IncorrectArgumentException, InterruptedException;

    boolean inGame(String username) throws RemoteException, UserNotRegisteredException;

    void performGameAction(String username, Command gameAction) throws RemoteException, MotherNatureLostException,
            NegativeValueException, AssistantCardNotFoundException, IncorrectArgumentException, IncorrectPlayerException,
            ProfessorNotFoundException, NotEnoughCoinsException, IncorrectStateException, UserNotRegisteredException, UserNotInRoomException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed;

    ArrayList<PropertyChangeEvent> getUpdates(String username) throws RemoteException, UserNotRegisteredException, UserNotInRoomException;

    void ping(String username) throws RemoteException, UserNotRegisteredException;

    void leaveGame(String nickname) throws UserNotRegisteredException, RemoteException;
}
