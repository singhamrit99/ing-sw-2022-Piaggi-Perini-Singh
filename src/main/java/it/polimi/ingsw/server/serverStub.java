package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toCollection;

public interface serverStub extends Remote {

    void registerUser(String name) throws RemoteException, UserAlreadyExistsException;

    void deregisterConnection(String username) throws RemoteException;

    ArrayList<String> getRoomsList() throws RemoteException;

    boolean inGame(String username) throws RemoteException;
    void createRoom(String username, String roomName) throws RemoteException;

    void joinRoom(String playerCaller, String roomName) throws RemoteException;

    public void leaveRoom(String playerCaller) throws RemoteException,UserNotInRoom;

    boolean getExpertModeRoom(String roomName) throws RemoteException;

    ArrayList<String> getPlayers(String roomName) throws RemoteException;

    public void setExpertMode(String playerCaller, boolean expertMode) throws RemoteException,UserNotInRoom,NotLeaderRoomException;

    public void startGame(String playerCaller) throws RemoteException, NotLeaderRoomException, UserNotInRoom;

    void performGameAction(Command gameAction) throws RemoteException, MotherNatureLostException, NegativeValueException, AssistantCardNotFound, IncorrectArgumentException, IncorrectPlayerException, ProfessorNotFoundException, NotEnoughCoinsException, IncorrectStateException;

    ArrayList<PropertyChangeEvent> getUpdates(String playercaller) throws RemoteException;

    ArrayList<String> getLobbyInfo(String roomName) throws RemoteException;

    boolean isExpertMode(String roomName) throws RemoteException;
}
