package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.stripped.StrippedModel;
import it.polimi.ingsw.server.SourceEvent;
import it.polimi.ingsw.server.commands.Command;
import it.polimi.ingsw.server.commands.PlayCharacterCardA;
import it.polimi.ingsw.server.serverStub;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Client implements Runnable {
    final private String ip;
    final private int port;
    private String nickname;
    private String clientRoom = null;
    private ArrayList<String> roomList;
    private serverStub server;
    private boolean inGame;
    private StrippedModel localModel;
    private boolean localModelLoaded;
    private boolean isMyTurn;


    private boolean userRegistered;
    private GUI gui;

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
        if (gui!=null)
        gui.roomsAvailable(roomList);
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
    }

    public void requestRoomJoin(String roomName) throws RemoteException, RoomNotExistsException, UserNotRegisteredException {
        server.joinRoom(nickname, roomName);
        clientRoom = roomName;

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
        }
    }

    public boolean isLeader() throws RemoteException, RoomNotExistsException {
        return getNicknamesInRoom(clientRoom).get(0).equals(nickname);
    }

    public void startGame() throws RemoteException, NotLeaderRoomException, UserNotInRoomException, RoomNotExistsException, UserNotRegisteredException {
        server.startGame(nickname);
    }

    @Override
    public void run() {
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
                    } catch (UserNotRegisteredException notRegisteredException) {
                        userRegistered = false;
                        break;
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
                case "init":
                    localModel = (StrippedModel) evt.getNewValue();
                    System.out.println("Game ready! Press any key to continue.\n");
                    break;
                case "message":
                    System.out.println("New message received!\n");
                    Object in = evt.getOldValue();
                    SourceEvent source= (SourceEvent) in;
                    switch (source.getWhat())
                    {
                        case "start turn":
                        {
                                //We're in CLI
                                if (gui==null)
                                {
                                    System.out.println("The current player is " + source.getWho());
                                    if(nickname.equals(source.getWho()))
                                    {
                                        System.out.println("It's your turn to play an assistant card!\n");
                                        isMyTurn=true;

                                    }

                                }
                            //We're in GUI
                            else
                                {

                                }
                            break;
                        }
                        case "played assistant card":
                        {
                            if (gui==null)
                            {
                                AssistantCard assistantCard= (AssistantCard) evt.getNewValue();
                                System.out.println("Assistant card played by "+source.getWho()+ ": " + assistantCard.getImageName());

                            }
                            //We're in GUI
                            else
                            {

                            }
                            break;
                        }
                        case "gameOver":
                        {
                            if (gui==null)
                            {
                                System.out.println(evt.getNewValue().toString() +" won! Congratulations and thanks for playing!\n");

                            }
                            //We're in GUI
                            else
                            {

                            }

                        }

                    }

                    //notify message to view TODO
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

    public void setGUI(GUI gui) {
        this.gui = gui;
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
}

