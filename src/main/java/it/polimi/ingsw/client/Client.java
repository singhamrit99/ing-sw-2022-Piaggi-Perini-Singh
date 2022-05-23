package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.UserAlreadyExistsException;
import it.polimi.ingsw.server.serverStub;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client implements Runnable{
    final private String ip;
    final private int port;

    private String nickname;
    private serverStub server;
    private boolean inGame;
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        inGame = false;
    }

    public void connect() {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            server = (serverStub) registry.lookup("server");
            System.out.println("connection done");
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    public void registerClient(String nickName) throws RemoteException, UserAlreadyExistsException {
        server.registerUser(nickName);
        this.nickname = nickName;
        run();
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

    @Override
    public void run() {
         while (true) {
            try {
                if(inGame){
                    System.out.println("the room is playing");
                    ArrayList<PropertyChangeEvent> newUpdates = server.getUpdates(nickname);
                }
                else {
                    if(server.inGame(nickname))inGame=true;
                    else inGame=false;
                }

                Thread.sleep(2000);
            } catch (RemoteException | InterruptedException e) {
                System.err.println("Remote exception: " + e.toString());
            }
        }

    }
}
