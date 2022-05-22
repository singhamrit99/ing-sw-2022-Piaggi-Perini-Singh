package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.server.commands.Command;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toCollection;

public interface serverStub extends Remote {

    void registerUser(String name) throws RemoteException;

    void deregisterConnection(String username) throws RemoteException;

    ArrayList<String> getRoomsList() throws RemoteException;

    void createRoom(String username, String roomName) throws RemoteException;

    void joinRoom(String playerCaller, String roomName) throws RemoteException;

    void leaveRoom(String playerCaller, String roomName) throws RemoteException;

    boolean getExpertModeRoom(String roomName) throws RemoteException;

    ArrayList<String> getPlayers(String roomName) throws RemoteException;

    void setExpertMode(String playerCaller, String roomName, boolean expertMode) throws RemoteException;

    void startGame(String playerCaller) throws RemoteException;

    void performGameAction(Command gameAction) throws RemoteException;

    ArrayList<PropertyChangeEvent> getUpdates(String playercaller) throws RemoteException;

    ArrayList<String> getLobbyInfo(String roomName) throws RemoteException;

    boolean isExpertMode(String roomName) throws RemoteException;
}
