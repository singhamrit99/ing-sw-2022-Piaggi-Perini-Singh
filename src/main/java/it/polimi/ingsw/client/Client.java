package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.stripped.StrippedModel;
import it.polimi.ingsw.server.commands.Command;
import it.polimi.ingsw.server.serverStub;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client implements Runnable {
    final private String ip;
    final private int port;
    private String nickname;
    private String clientRoom = null;
    private ArrayList<String> roomList;
    private ArrayList<String> playersList;
    private serverStub server;
    private boolean inGame;
    private StrippedModel localModel;
    private boolean localModelLoaded;
    private boolean isMyTurn;
    private boolean userRegistered;
    private boolean drawnOut;
    private View view;
    private int phase = 0;

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
        userRegistered = true;

        new Thread(this).start(); //it's important that the thread runs only after the correct registration!
    }

    public void deregisterClient() throws RemoteException, UserNotRegisteredException {
        if (userRegistered) {
            server.deregisterConnection(nickname);
            this.nickname = null;
            userRegistered = false;
        }
    }

    public void createRoom(String roomName) throws RemoteException, UserNotRegisteredException, RoomAlreadyExistsException {
        server.createRoom(nickname, roomName);
        clientRoom = roomName;
        try {
            playersList = server.getPlayers(clientRoom);
            view.roomJoin(playersList);
        } catch (RoomNotExistsException e) {
            e.printStackTrace();
        }
    }

    public void requestRoomJoin(String roomName) throws RemoteException, RoomNotExistsException, UserNotRegisteredException {
        server.joinRoom(nickname, roomName);
        clientRoom = roomName;
        playersList = getNicknamesInRoom(clientRoom);
        view.roomJoin(playersList);
    }

    public ArrayList<String> requestLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException {
        return server.getLobbyInfo(roomName);
    }

    public ArrayList<String> getRooms() throws RemoteException {
        return server.getRoomsList();
    }

    public ArrayList<String> getNicknamesInRoom(String roomName) throws RemoteException, RoomNotExistsException {
        return server.getPlayers(roomName);
    }

    public void setExpertMode(boolean value) throws RemoteException, NotLeaderRoomException, UserNotInRoomException, UserNotRegisteredException {
        server.setExpertMode(nickname, value);
    }

    public void leaveRoom() throws RemoteException, UserNotInRoomException, UserNotRegisteredException {
        if (clientRoom == null) {
            throw new UserNotInRoomException();
        } else {
            server.leaveRoom(nickname);
            clientRoom = null;
            roomList = getRooms();
            view.roomsAvailable(roomList);
        }
    }

    public boolean isLeader() throws RemoteException, RoomNotExistsException {
        return getNicknamesInRoom(clientRoom).get(0).equals(nickname);
    }

    public void startGame() throws RemoteException, NotLeaderRoomException, UserNotInRoomException, RoomNotExistsException, UserNotRegisteredException {
        server.startGame(nickname);
        view.startGame();
    }

    public void roomListShow() {
        try {
            roomList = server.getRoomsList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        view.roomsAvailable(roomList);
    }

    @Override
    public void run() {
        boolean first = true;
        int oldSize = 0;

        while (userRegistered) {
            try {
                if (inGame) {
                    //System.out.println("the room is playing and I'm in");
                    ArrayList<PropertyChangeEvent> newUpdates = server.getUpdates(nickname);
                    manageUpdates(newUpdates);
                } else {
                    try {
                        inGame = server.inGame(nickname);
                        roomList = server.getRoomsList();
                        if (GUI.view!=null) {
                            //display and refresh of room list
                            if (GUI.view.equals("lobby")) {
                                if (first) {
                                    roomListShow();
                                    oldSize = roomList.size();
                                    first = false;
                                } else if (roomList.size() != oldSize) {
                                    oldSize = roomList.size();
                                    roomListShow();
                                }
                            } else if (GUI.view.equals("room")) {
                                //refresh playerList if in room
                                if (clientRoom != null) {
                                    if (!getNicknamesInRoom(clientRoom).equals(playersList)) {
                                        playersList = getNicknamesInRoom(clientRoom);
                                        view.roomJoin(playersList);
                                    }
                                }
                            }
                        }
                        else//We're in CLI
                        {
                            if (ViewCLI.view.equals("lobby")) {
                                if (first) {
                                    roomListShow();
                                    oldSize = roomList.size();
                                    first = false;
                                } else if (roomList.size() != oldSize) {
                                    oldSize = roomList.size();
                                    roomListShow();
                                }
                            }
                            else if(ViewCLI.view.equals("room"))
                            {
                                if (clientRoom != null) {
                                    if (!getNicknamesInRoom(clientRoom).equals(playersList)) {
                                        playersList = getNicknamesInRoom(clientRoom);
                                        view.roomJoin(playersList);
                                    }
                                }
                            }


                        }
                    } catch (UserNotRegisteredException notRegisteredException) {
                        userRegistered = false;
                        break;
                    } catch (RoomNotExistsException e) {
                        e.printStackTrace();
                    }
                }
                Ping();
                Thread.sleep(500);
            } catch (RemoteException | LocalModelNotLoadedException | UserNotInRoomException | UserNotRegisteredException | InterruptedException e) {
                System.err.println("Client exception: " + e);
            }
        }
    }

    private void Ping() throws RemoteException, UserNotRegisteredException {
        server.ping(nickname);
    }

    private void manageUpdates(ArrayList<PropertyChangeEvent> evtArray) throws LocalModelNotLoadedException {
        for (PropertyChangeEvent evt : evtArray) {
            switch (evt.getPropertyName()) {
                case "first-player":
                    view.currentPlayer((String) evt.getNewValue());
                    if (nickname.equals(evt.getNewValue()))
                        setMyTurn(true);
                    if (localModel != null) {
                        localModel.updateModel(evt);
                    } else {
                        throw new LocalModelNotLoadedException();
                    }
                    break;
                case "change-phase":
                   // System.out.println("Received change phase event\n");
                    phase++;
                    if (phase > 4) {
                        phase = 0;
                    }
                    System.out.println("phase:" + phase);
                    if (phase==1)
                    {
                        if(nickname.equals((String) evt.getNewValue()))
                        {
                            System.out.println("It is my turn according to the assistant card I played.");
                            isMyTurn=true;
                        }
                        else
                        {
                            System.out.println("It is not my turn according to the assistant card I played.");
                            isMyTurn=false;
                        }
                    }
                    break;
                case "init":
                    System.out.println("Request for loading received\n");
                    localModel = (StrippedModel) evt.getNewValue();
                    localModelLoaded = true;
                    System.out.println("Local model loaded\n");
                    localModel.setUI(view);
                    System.out.println("Game ready! Press any key to continue.\n");
                    break;
                case "current-player":
                    if (nickname.equals(evt.getNewValue()))
                        isMyTurn = true;
                    if (localModel != null) {
                        localModel.updateModel(evt);
                    } else {
                        throw new LocalModelNotLoadedException();
                    }
                    break;
                default:
                    if (localModel != null) {
                        localModel.updateModel(evt);
                    } else {
                        throw new LocalModelNotLoadedException();
                    }

                    break;
            }
        }
    }

    public void performGameAction(Command command) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException,
            IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException,
            UserNotInRoomException, UserNotRegisteredException {
        server.performGameAction(nickname, command);
    }

    public boolean isInGame() {
        return inGame;
    }

    public StrippedModel getLocalModel() {
        return localModel;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public String getNickname() {
        return nickname;
    }

    public void setUI(View view) {
        this.view = view;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getPhase() {
        return phase;
    }

    public boolean isDrawnOut() {
        return drawnOut;
    }

    public void setDrawnOut(boolean drawnOut) {
        this.drawnOut = drawnOut;
    }

    public String getRoom() {
        return clientRoom;
    }
}

