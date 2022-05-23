package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.UserAlreadyExistsException;
import it.polimi.ingsw.server.serverStub;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

public class Client {
    final private String ip;
    final private int port;

    private serverStub server;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        connect();
    }

    public void connect() {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(ip, port);
            // Looking up the registry for the remote object
            server = (serverStub) registry.lookup("server");
            System.out.println("connection done");
            ViewCLI viewCLI = new ViewCLI(this);
            viewCLI.Start();


        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    public void sendName(String nickName) throws RemoteException, UserAlreadyExistsException {
        server.registerUser(nickName);
    }

    public void createRoom(String username, String roomName) throws RemoteException {
        server.createRoom(username, roomName);
    }

    public void requestRoomJoin(String playerCaller, String roomName) throws RemoteException {
        server.joinRoom(playerCaller, roomName);
    }

    public ArrayList<String> requestLobbyInfo(String roomName) throws RemoteException {
        return server.getLobbyInfo(roomName);
    }

    public ArrayList<String> getRooms() throws RemoteException {
        return server.getRoomsList();
    }

    public ArrayList<String> getNicknamesInRoom(String roomName) throws RemoteException {
        return server.getPlayers(roomName);
    }

    public boolean getLeader(String playercaller, String roomName) {
        return true;
    }

    public void setExpertMode(String playerCaller, String roomName, boolean value) throws RemoteException {
        server.setExpertMode(playerCaller, roomName, value);
    }

    public void leaveRoom(String playercaller, String roomName) throws RemoteException {
        server.leaveRoom(playercaller, roomName);
    }

    /*
    @Override
    public void run() {
         while (true) {
            try {
                Thread.sleep(1000);
            } catch (RemoteException | InterruptedException e) {
                System.err.println("Remote exception: " + e.toString());
            }
        }

    }
    */
}
