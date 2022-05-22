package it.polimi.ingsw.server;

import it.polimi.ingsw.server.commands.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface serverStub extends Remote {
    public void registerUser(String name) throws RemoteException;

    public void deregisterConnection(String username) throws RemoteException;

    public HashMap<String, Room> getRoomsList() throws RemoteException;

    public void createRoom(String roomName, ClientConnection user) throws RemoteException;

    public void joinRoom(String roomName, ClientConnection user) throws RemoteException;

    public void getLobbyInfo(String playercaller) throws RemoteException;

    public void setExpertMode(String playercaller, String roomName) throws RemoteException;

    public void startGame(String playercaller) throws RemoteException;

    public void performGameAction(Command gameAction)throws RemoteException;

    public String testPing()throws RemoteException;
}
