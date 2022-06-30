package it.polimi.ingsw.network.client;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.commands.Command;
import it.polimi.ingsw.network.server.serverStub;
import it.polimi.ingsw.network.server.stripped.StrippedModel;
import it.polimi.ingsw.view.GUI.controllerFX.*;
import it.polimi.ingsw.view.UI;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
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
    private boolean roomExpertMode = false;
    int oldRoomListSize = 0;
    boolean firstRoomListRefresh = true;

    /**
     * Client class constructor. Ip and Port are needed for connectivity purposes.
     * Also initializes inGame, localModelLoaded and userRegistered fields to false.
     *
     * @param ip   The Ip to connect to.
     * @param port The port to connect to.
     */
    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        inGame = false;
        localModelLoaded = false;
        userRegistered = false;
    }

    /**
     * Method used to connect to the server.
     */
    private void connect() throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(ip, port);
        server = (serverStub) registry.lookup("server");
    }

    /**
     * Method used to register a new client to the server with a unique username.
     *
     * @param nickName the username chosen by the new player.
     * @throws NotBoundException          Thrown if the registry has problems to find the server binding.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserAlreadyExistsException Thrown if the chosen name is already on the server.
     * @throws NameFieldException         Thrown if the chosen name is blank
     */
    public void registerClient(String nickName) throws NotBoundException, RemoteException,
            UserAlreadyExistsException, NameFieldException {
        connect();
        server.registerUser(nickName);
        this.nickname = nickName;
        userRegistered = true;
        new Thread(this::ping).start();
        new Thread(this).start();
    }



    /**
     * Used to deregister a client.
     *
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown if the chosen name is already on the server.
     */
    public void deregisterClient() throws RemoteException, UserNotRegisteredException {
        if (userRegistered) {
            server.deregisterConnection(nickname);
            this.nickname = null;
            userRegistered = false;
        }
    }

    /**
     * Method used to request a room creation from the server.
     *
     * @param roomName the name of the room to create.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotRegisteredException Thrown if the chosen name is already on the server.
     * @throws RoomAlreadyExistsException Thrown if the chosen room name is already on the server.
     * @throws NameFieldException         Thrown if the chosen name is blank
     */
    public void createRoom(String roomName) throws RemoteException, UserNotRegisteredException,
            RoomAlreadyExistsException, NameFieldException {
        server.createRoom(nickname, roomName);
        clientRoom = roomName;
        try {
            playersList = server.getPlayers(clientRoom); //ui purposes
            ui.roomJoin(playersList);
        } catch (RoomNotExistsException ignoredBecauseJustCreated) {}
    }

    /**
     * Method used to send a request to join a room.
     *
     * @param roomName the name of the room the request is sent to.
     * @throws RemoteException            Thrown in case of a network error.
     * @throws RoomInGameException        Thrown if the room is already playing Eriantys.
     * @throws RoomNotExistsException     Thrown if the given room name doesn't exist on the server.
     * @throws UserNotRegisteredException Thrown if the method is called by an invalid user.
     * @throws RoomFullException          Thrown if the room we're trying to join already has 4 players in it.
     */
    public void requestRoomJoin(String roomName) throws RemoteException, RoomInGameException,
            RoomNotExistsException, UserNotRegisteredException, RoomFullException, UserInRoomException {
        try {
            server.joinRoom(nickname, roomName);
            clientRoom = roomName;
            playersList = getNicknamesInRoom();
            ui.roomJoin(playersList);
        } catch (UserNotInRoomException ignored) {} //after joining ...
    }

    /**
     * Method used to request lobby info.
     *
     * @param roomName The room we're requesting information from
     * @return The information asked (players, room name, whether it's a Standard or Expert play room)
     * @throws RemoteException        Thrown in case of a network error.
     * @throws RoomNotExistsException Thrown if the given room name doesn't exist on the server.
     */
    public ArrayList<String> requestLobbyInfo(String roomName) throws RemoteException, RoomNotExistsException {
        return server.getLobbyInfo(roomName);
    }

    /**
     * Returns the rooms on the server
     *
     * @return List of rooms on the server.
     * @throws RemoteException Thrown in case of a network error.
     */
    public ArrayList<String> getRooms() throws RemoteException {
        return server.getRoomsList();
    }

    /**
     * Returns the nicknames of every player in the room.
     *
     * @return nicknames String ArrayList.
     * @throws RemoteException        Thrown in case of a network error.
     * @throws RoomNotExistsException Thrown if the given room name doesn't exist on the server.
     * @throws UserNotInRoomException Thrown if the user tries to call this action when not in a room.
     */
    public ArrayList<String> getNicknamesInRoom() throws RemoteException, RoomNotExistsException, UserNotInRoomException {
        if (clientRoom == null) throw new UserNotInRoomException();
        return server.getPlayers(clientRoom);
    }

    /**
     * Sends a request to switch the game mode to Expert Mode or back to Standard Mode.
     *
     * @param value the boolean value corresponding with the choice (true=switch)
     * @throws RemoteException            Thrown in case of a network error.
     * @throws NotLeaderRoomException     Thrown if this method is accessed by any player that isn't the room's leader.
     * @throws UserNotInRoomException     Thrown if the user tries to call this action when not in a room.
     * @throws UserNotRegisteredException Thrown if the method is called by an invalid user.
     */
    public void setExpertMode(boolean value) throws RemoteException, NotLeaderRoomException, UserNotInRoomException, UserNotRegisteredException {
        server.setExpertMode(nickname, value);
    }

    /**
     * Method used to leave a room.
     *
     * @throws RemoteException            Thrown in case of a network error.
     * @throws UserNotInRoomException     Thrown if the user tries to call this action when not in a room.
     * @throws UserNotRegisteredException Thrown if the method is called by an invalid user.
     */
    public void leaveRoom() throws RemoteException, UserNotInRoomException, UserNotRegisteredException {
        if (clientRoom == null) {
            throw new UserNotInRoomException();
        } else {
            server.leaveRoom(nickname);
            clientRoom=null;
        }
    }

    public void refreshScreenLobby() throws RemoteException {
        view = StringNames.LOBBY;
        LobbyController.setOpened(false);
        RoomController.setOpened(false);
        GameViewController.setOpened(false);
        GameOverController.setOpened(false);
        localModelLoaded = false;
        setInGame(false);
        localModel = null;
        clientRoom = null;
        oldRoomListSize = 0; //this is necessary for the correct reloading of the rooms
        firstRoomListRefresh = true;
        roomListShow();
    }

    public void leaveGame() throws UserNotRegisteredException, UserNotInRoomException, RemoteException {
        if (clientRoom == null) {
            throw new UserNotInRoomException();
        } else {
            server.leaveGame(nickname);
            refreshScreenLobby();
        }
    }

    /**
     * Returns the room leader.
     *
     * @return the room leader's nickname(String)
     * @throws RemoteException        Thrown in case of a network error.
     * @throws RoomNotExistsException Thrown if the given room name doesn't exist on the server.
     * @throws UserNotInRoomException Thrown if the user tries to call this action when not in a room.
     */
    public boolean isLeader() throws RemoteException, RoomNotExistsException, UserNotInRoomException {
        return getNicknamesInRoom().get(0).equals(nickname);
    }

    /**
     * Method used to send the Start Game request.
     *
     * @throws RemoteException            Thrown in case of a network error.
     * @throws NotLeaderRoomException     Thrown if this method is accessed by any player that isn't the room's leader.
     * @throws UserNotInRoomException     Thrown if the user tries to call this action when not in a room.
     * @throws RoomNotExistsException     Thrown if the given room name doesn't exist on the server.
     * @throws NotEnoughPlayersException  Thrown if there aren't enough players to start the game.
     * @throws UserNotRegisteredException Thrown if the method is called by an invalid user.
     */
    public void startGame() throws RemoteException, NotLeaderRoomException, UserNotInRoomException,
            RoomNotExistsException, NotEnoughPlayersException, UserNotRegisteredException,
            NegativeValueException, IncorrectArgumentException, InterruptedException {
        server.startGame(nickname);
    }

    /**
     * Method used to show the list of available rooms
     */
    public void roomListShow() throws RemoteException {
        roomList = server.getRoomsList();
        ui.roomsAvailable(roomList);
    }

    /**
     * Run method that continuously listens to update events coming from the Game Listener.
     */
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
                        if (firstRoomListRefresh) {
                            roomListShow();
                            oldRoomListSize = roomList.size();
                            firstRoomListRefresh = false;
                        } else if (roomList.size() != oldRoomListSize) {
                            oldRoomListSize = roomList.size();
                            roomListShow();
                        }
                    }

                    if (view.equals(StringNames.ROOM)) {
                        //display and refresh playerList if in room
                        if (clientRoom != null) {
                            if (!getNicknamesInRoom().equals(playersList) || roomExpertMode != getExpertMode()) {
                                playersList = getNicknamesInRoom();
                                roomExpertMode = getExpertMode();
                                ui.roomJoin(playersList); //method used also to refresh the screen
                            }
                        }
                    }
                }
                Thread.sleep(50);
            } catch (RemoteException c){
                System.out.println(StringNames.REMOTE);
                userRegistered = false;
            } catch (LocalModelNotLoadedException l){
                System.out.println(StringNames.LOCAL_MODEL_ERROR);
            } catch (InterruptedException i){
                System.out.println(StringNames.INTERRUPTED);
            } catch (UserNotInRoomException u){
                System.out.println(StringNames.USER_NOT_IN_ROOM);
            }catch (RoomNotExistsException e) {
                System.out.println(StringNames.ROOM_NOT_EXISTS);
            } catch (UserNotRegisteredException e) {
                System.out.println(StringNames.USER_NOT_REGISTERED);
                userRegistered = false;
            }
        }
    }

    /**
     * Pink method to make sure the client is connected at all times. If the client misses too many pings they will be disconnected automatically.
     */
    private void ping() {
        while (userRegistered) {
            try {
                server.ping(nickname);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
                userRegistered = false;
            } catch (UserNotRegisteredException e) {
                System.out.println(StringNames.USER_NOT_REGISTERED);
                userRegistered = false;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
        }
    }

    /**
     * Method that actually manages the events received in the run method.
     *
     * @param evtArray The events buffer that need to be managed and sorted via their identificators.
     * @throws LocalModelNotLoadedException    Thrown if localModel field is null.
     * @throws BadFormattedLocalModelException Thrown if localModel is present but incorrectly built.
     */
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
                        System.out.println(StringNames.REMOTE);
                        userRegistered=false;
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
                            System.out.println(StringNames.LOCAL_MODEL_ERROR);
                        }
                    } else {
                        System.out.println(StringNames.LOCAL_MODEL_ERROR);
                    }
                    break;
                case "game-finished":
                    try {
                        leaveRoom();
                    } catch (RemoteException e) {
                        System.out.println(StringNames.REMOTE);
                    } catch (UserNotInRoomException e) {
                        System.out.println(StringNames.USER_NOT_IN_ROOM);
                    } catch (UserNotRegisteredException e) {
                        System.out.println(StringNames.USER_NOT_REGISTERED);
                    }
                    ui.gameOver((String) evt.getOldValue(), (String) evt.getNewValue());
                    break;
                default:
                    if (localModel != null) {
                        try {
                            localModel.updateModel(evt);
                        } catch (BadFormattedLocalModelException e) {
                            System.out.println(StringNames.LOCAL_MODEL_ERROR);
                        }
                    } else {
                        System.out.println(StringNames.LOCAL_MODEL_ERROR);
                    }
                    break;
            }
        }
    }

    /**
     * Method used to perform game actions through Commands infrastructure.
     *
     * @param command The command that is then forwarded to the Invoker.
     * @throws NotEnoughCoinsException        Thrown if the player that tried to play a character card doesn't have enough coins to buy it.
     * @throws AssistantCardNotFoundException Thrown if the Assistant card string provided is invalid.
     * @throws NegativeValueException         As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectStateException        Thrown when an action is performed in an invalid phase of the game.
     * @throws MotherNatureLostException      Thrown when the game can't calculate Mother Nature's position.
     * @throws ProfessorNotFoundException     Thrown when a professor search generates an error.
     * @throws IncorrectPlayerException       Thrown if the player that called the method isn't the current player.
     * @throws RemoteException                Thrown in case of a network error.
     * @throws IncorrectArgumentException     Thrown if any of the parameters used by the method are invalid.
     * @throws UserNotInRoomException         Thrown if the user tries to call this action when not in a room.
     * @throws UserNotRegisteredException     Thrown if the method is called by an invalid user.
     */
    public void performGameAction(Command command) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException,
            IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException,
            UserNotInRoomException, UserNotRegisteredException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        server.performGameAction(nickname, command);
    }

    /**
     * Getter method for inGame field
     *
     * @return inGame
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Returns whether the given room is playing Eriantys.
     *
     * @param roomName The name of the room to check.
     * @return true or false depending on the outcome.
     * @throws RoomNotExistsException Thrown if the given room name doesn't exist on the server.
     * @throws RemoteException        Thrown in case of a network error.
     */
    public boolean isRoomInGame(String roomName) throws RoomNotExistsException, RemoteException {
        return server.isInGame(roomName);
    }

    /**
     * LocalModel getter method.
     *
     * @return localmodel.
     */
    public StrippedModel getLocalModel() {
        return localModel;
    }

    /**
     * isMyTurn getter method
     *
     * @return isMyturn.
     */
    public boolean isMyTurn() {
        return isMyTurn;
    }

    /**
     * Method used to set player turn.
     *
     * @param myTurn set to true when "next-player" event's appropriate field equals nickname.
     */
    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    /**
     * Getter method for nickname
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Binds the ui to this client.
     *
     * @param ui Either GUI or CLI interface.
     */
    public void setUi(UI ui) {
        this.ui = ui;
    }

    /**
     * inGame field setter
     *
     * @param inGame value to set inGame to. Always false except for when the client is playing.
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * clientRoom getter
     *
     * @return the room the client is in.
     */
    public String getRoom() {
        return clientRoom;
    }

    /**
     * returns the local playerlist.
     *
     * @return local Players list.
     */
    public ArrayList<String> getLocalPlayerList() {
        return playersList;
    }

    /**
     * Asks the server whether the room is in Expert or Standard play mode.
     *
     * @return true if expertmode, false if not
     * @throws RemoteException        Thrown in case of a network error.
     * @throws RoomNotExistsException Thrown if the given room name doesn't exist on the server.
     */
    public boolean getExpertMode() throws RemoteException, RoomNotExistsException {
        return Boolean.parseBoolean(server.getLobbyInfo(clientRoom).get(2));
    }
}

