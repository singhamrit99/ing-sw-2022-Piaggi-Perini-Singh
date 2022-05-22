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

    public void registerUser(String name) throws RemoteException;

    public void deregisterConnection(String username) throws RemoteException;

    public ArrayList<String> getRoomsList() throws RemoteException;

    public void createRoom(String username, String roomName) throws RemoteException;

    public void joinRoom(String playerCaller, String roomName) throws RemoteException;

    public void leaveRoom(String playerCaller, String roomName) throws RemoteException;

    public boolean getExpertModeRoom(String roomName) throws RemoteException;

    public ArrayList<String> getPlayers(String roomName) throws RemoteException;

    public void setExpertMode(String playerCaller, boolean expertMode) throws RemoteException;

    public void startGame(String playerCaller) throws RemoteException;

    public void performGameAction(Command gameAction) throws RemoteException;

    public ArrayList<PropertyChangeEvent> getUpdates(String playercaller) throws RemoteException;
}
