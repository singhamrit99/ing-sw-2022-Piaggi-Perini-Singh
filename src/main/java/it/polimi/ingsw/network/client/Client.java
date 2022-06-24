package it.polimi.ingsw.network.client;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.view.GUI.controllerFX.GameViewController;
import it.polimi.ingsw.view.GUI.controllerFX.LobbyController;
import it.polimi.ingsw.view.GUI.controllerFX.RoomController;
import it.polimi.ingsw.view.UI;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.stripped.StrippedModel;
import it.polimi.ingsw.network.server.commands.Command;
import it.polimi.ingsw.network.server.serverStub;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client implements Runnable {
    final private String ip;
    final private int port;
    private serverStub server;
    private UI ui;
    private String nickname;
    private String clientRoom = null;
    public String view;
    private ArrayList<String> roomList;
    private ArrayList<String> playersList;
    private StrippedModel localModel;
    private boolean inGame;
    private boolean localModelLoaded;
    private boolean isMyTurn;
    private boolean userRegistered;
    private boolean drawnOut;
    private boolean roomExpertMode = false;
    int oldSize = 0;
    boolean firstRoomListRefactor = true;

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
        } catch (Exception e) {
            System.err.println("Client connection to server exception: " + e); //TODO
            e.printStackTrace();
        }
    }

    public void registerClient(String nickName) throws RemoteException, UserAlreadyExistsException {
        server.registerUser(nickName);
        this.nickname = nickName;
        userRegistered = true;
        new Thread(this::ping).start();
        new Thread(this).start();
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
            playersList = server.getPlayers(clientRoom); //ui purposes
            ui.roomJoin(playersList);
        } catch (RoomNotExistsException e) {
            e.printStackTrace();
        }
    }

    public void requestRoomJoin(String roomName) throws RemoteException, RoomInGameException,
            RoomNotExistsException, UserNotRegisteredException, RoomFullException {
        try {
            server.joinRoom(nickname, roomName);
            clientRoom = roomName;
            playersList = getNicknamesInRoom();
        } catch (UserNotInRoomException ignored) {
            ignored.printStackTrace();
        } catch (UserInRoomException problem) {
            problem.printStackTrace(); //TODO
        }
        ui.roomJoin(playersList);
    }

    public ArrayList<String> requestLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException {
        return server.getLobbyInfo(roomName);
    }

    public ArrayList<String> getRooms() throws RemoteException {
        return server.getRoomsList();
    }

    public ArrayList<String> getNicknamesInRoom() throws RemoteException, RoomNotExistsException, UserNotInRoomException {
        if (clientRoom == null) throw new UserNotInRoomException();
        return server.getPlayers(clientRoom);
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
            ui.roomsAvailable(roomList);
            oldSize = 0; //this is necessary for the correct reloading of the rooms list but maybe refactor name
            firstRoomListRefactor = true; //TODO name also of this
        }
    }

    public void leaveGame() throws UserNotRegisteredException, UserNotInRoomException, RemoteException {
        if (clientRoom == null) {
            throw new UserNotInRoomException();
        } else {
            server.leaveGame(nickname);
        }
    }

    public boolean isLeader() throws RemoteException, RoomNotExistsException, UserNotInRoomException {
        return getNicknamesInRoom().get(0).equals(nickname);
    }

    public void startGame() throws RemoteException, NotLeaderRoomException, UserNotInRoomException,
            RoomNotExistsException, NotEnoughPlayersException, UserNotRegisteredException {
        server.startGame(nickname);
    }

    public void roomListShow() {
        try {
            roomList = server.getRoomsList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ui.roomsAvailable(roomList);
    }

    @Override
    public void run() {
        while (userRegistered) {
            try {
                inGame = server.inGame(nickname);
                if (inGame) {
                    ArrayList<PropertyChangeEvent> newUpdates = server.getUpdates(nickname);
                    manageUpdates(newUpdates);
                } else {
                    roomList = server.getRoomsList();

                    if (view.equals(StringNames.LOBBY)) {
                        if (firstRoomListRefactor) {
                            roomListShow();
                            oldSize = roomList.size();
                            firstRoomListRefactor = false;
                        } else if (roomList.size() != oldSize) {
                            oldSize = roomList.size();
                            roomListShow();
                        }
                    }

                    if (view.equals(StringNames.ROOM)) {
                        //display and refresh playerList if in room
                        if (clientRoom != null) {
                            if (!getNicknamesInRoom().equals(playersList) || roomExpertMode != getExpertMode()) {
                                playersList = getNicknamesInRoom();
                                roomExpertMode = getExpertMode();
                                ui.roomJoin(playersList); //TODO refactor the NAME of this method
                            }
                        }
                    }
                }
                Thread.sleep(100);
            } catch (RemoteException | LocalModelNotLoadedException | InterruptedException | UserNotInRoomException | RoomNotExistsException e) {
                e.printStackTrace();
            } catch (UserNotRegisteredException e) {
                e.printStackTrace();
                userRegistered = false;
            }
        }
    }

    private void ping() {
        while (userRegistered) {
            try {
                server.ping(nickname);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (UserNotRegisteredException e) {
                userRegistered = false; //TODO
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void manageUpdates(ArrayList<PropertyChangeEvent> evtArray) throws LocalModelNotLoadedException {
        for (PropertyChangeEvent evt : evtArray) {
            switch (evt.getPropertyName()) {
                case "first-player":
                    if (nickname.equals(evt.getNewValue()))
                        setMyTurn(true);
                    if (localModel != null) {
                        try {
                            localModel.updateModel(evt);
                        } catch (BadFormattedLocalModelException badFormattedLocalModelException) {
                            badFormattedLocalModelException.printStackTrace();
                            badFormattedLocalModelException.printStackTrace();
                        }
                    } else {
                        throw new LocalModelNotLoadedException();
                    }
                    break;
                case "init":
                    localModel = (StrippedModel) evt.getNewValue();
                    localModelLoaded = true;
                    localModel.setUi(ui);
                    try {
                        view = StringNames.INGAME;
                        ui.startGame();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case "current-player":
                    setInGame(true);
                    if (nickname.equals(evt.getNewValue()))
                        isMyTurn = true;
                    if (localModel != null) {
                        try {
                            localModel.updateModel(evt);
                        } catch (BadFormattedLocalModelException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new LocalModelNotLoadedException();
                    }
                    break;
                case "game-finished":
                    try {
                        server.leaveRoom(nickname);
                    } catch (UserNotRegisteredException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (UserNotInRoomException e) {
                        e.printStackTrace();
                    }
                    LobbyController.setOpened(false);
                    RoomController.setOpened(false);
                    GameViewController.setOpened(false);
                    view = StringNames.LOBBY;
                    firstRoomListRefactor = true;
                    setInGame(false);
                    localModelLoaded = false;
                    localModel = null;
                    break;
                default:
                    if (localModel != null) {
                        try {
                            localModel.updateModel(evt);
                        } catch (BadFormattedLocalModelException e) {
                            e.printStackTrace();
                        }
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

    public boolean isRoomInGame(String roomName) throws RoomNotExistsException, RemoteException {
        return server.isInGame(roomName);
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

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
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

    public ArrayList<String> getLocalPlayerList() {
        return playersList;
    }

    public boolean getExpertMode() throws RemoteException, RoomNotExistsException {
        return Boolean.parseBoolean(server.getLobbyInfo(clientRoom).get(2));
    }
}

