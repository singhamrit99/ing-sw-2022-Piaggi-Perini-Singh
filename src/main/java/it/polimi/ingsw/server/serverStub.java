package it.polimi.ingsw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface serverStub extends Remote {

    public void registerUser(String name) throws RemoteException;

    public ArrayList<String> getRoomsList() throws RemoteException;

    public void deregisterConnection(ClientConnection c)throws RemoteException;

    public void createRoom(String roomName, ClientConnection user)throws RemoteException;

    public void joinRoom(String roomName, ClientConnection user)throws RemoteException;

    public void getLobbyInfo(String playercaller) throws RemoteException;

    public void setExpertMode(String playercaller,String roomName) throws RemoteException;

    public void requestRoomCreation(String playercaller,String roomName) throws RemoteException;

    public void requestRoomJoin(String playercaller, String roomName) throws RemoteException;
}
