package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.stripped.StrippedModel;
import it.polimi.ingsw.server.commands.Command;
import it.polimi.ingsw.server.commands.PlayCharacterCardA;
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
    private String clientRoom= null;
    private ArrayList<String> roomList;
    private serverStub server;
    private boolean inGame;
    private StrippedModel localModel;
    private boolean localModelLoaded;
    private boolean userRegistered;
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        inGame = false;
        localModelLoaded = false;
        userRegistered = false;
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
        userRegistered=true;
        new Thread(this).start();
        //it's important that the thread runs only after the correct registration!
    }

    public void deregisterClient() throws RemoteException{
        if(userRegistered){
            server.deregisterConnection(nickname);
            this.nickname = null;
            userRegistered=false;
        }
    }
    public void createRoom(String roomName) throws RemoteException {
        server.createRoom(nickname, roomName);
        clientRoom=roomName;
    }
    public void requestRoomJoin(String roomName) throws RemoteException {
        server.joinRoom(nickname, roomName);
        clientRoom= roomName;

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
    public void setExpertMode(boolean value) throws RemoteException, NotLeaderRoomException, UserNotInRoomException {
        server.setExpertMode(nickname, value);
    }
    public void leaveRoom() throws RemoteException, UserNotInRoomException {
        if (clientRoom==null)
        {
            throw new UserNotInRoomException();
        }
        else {
            server.leaveRoom(nickname);
            clientRoom = null;
        }
    }

    public boolean isLeader() throws RemoteException {
        return getNicknamesInRoom(clientRoom).get(0).equals(nickname);
    }

    public void startGame() throws  RemoteException, NotLeaderRoomException, UserNotInRoomException {
        server.startGame(nickname);
    }


    @Override //TODO DEPRECATED CODE, REPLACE WITH THREAD POOL EXECUTOR
    public void run() {
         while (userRegistered) {
            try {
                if(inGame){
                    System.out.println("the room is playing");
                    ArrayList<PropertyChangeEvent> newUpdates = server.getUpdates(nickname);
                    manageUpdates(newUpdates);
                }
                else {
                    inGame=server.inGame(nickname);
                    roomList = server.getRoomsList();
                }

                        Ping();

                Thread.sleep(500); //should be half of server ping timeout
            } catch (RemoteException | InterruptedException | LocalModelNotLoadedException e) {
                System.err.println("Client exception: " + e);
            }
        }
    }

    private void Ping() throws  RemoteException{
        server.ping(nickname);
       //System.out.println("Ho pingato il server");
    }
    private void manageUpdates(ArrayList<PropertyChangeEvent> evtArray) throws LocalModelNotLoadedException {
        for(PropertyChangeEvent evt: evtArray){
            switch (evt.getPropertyName()){
                case "init":
                    localModel = (StrippedModel) evt.getNewValue();
                    break;
                case "message":
                    //notify message to view TODO
                    break;
                default:
                    if(localModel!=null){
                        localModel.updateModel(evt);
                    }
                    else{
                        throw new LocalModelNotLoadedException();
                    }
                    break;
            }
        }
    }

    public void performGameAction(Command command) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        server.performGameAction(command);
    }
}
