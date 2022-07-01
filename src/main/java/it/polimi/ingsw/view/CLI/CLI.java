package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.StringNames;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.stripped.StrippedBoard;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.network.server.stripped.StrippedIsland;
import it.polimi.ingsw.network.server.commands.*;
import it.polimi.ingsw.view.UI;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CLI implements UI {
    public static String view;
    Client client;
    String nickName;
    ArrayList<Integer> playedThisTurn;
    int playerNumber;
    String clientRoom = null;
    int action;
    int numOfPlayers;
    int movedStudents;
    boolean endTurn = false;
    MoveMotherNature moveMotherNatureOrder;
    MoveStudents moveStudentsOrder;
    PickCloud pickCloudOrder;
    PlayAssistantCard playAssistantCardOrder;
    PlayCharacterCardA playCharacterCardAOrder;
    PlayCharacterCardB playCharacterCardBOrder;
    PlayCharacterCardC playCharacterCardCOrder;
    PlayCharacterCardD playCharacterCardDOrder;
    DrawFromBagCommand drawFromBagOrder;
    HashMap<String, ArrayList<Colors>> professorsTables = new HashMap<>();
    private final Scanner in = new Scanner(System.in);

    /**
     * CLI constructor.
     *
     * @param client the client this view is connected to.
     */
    public CLI(Client client) {
        this.client = client;
        this.client.setUi(this);
        this.client.view = StringNames.LAUNCHER;
    }

    /**
     * The main view method, in which the game loop remains for all the uptime of the program.
     */
    public void Start() {

        AnsiConsole.systemInstall();

        System.out.println("Welcome to...");

        System.out.println(ansi().fg(BLUE).bg(BLACK).a("\n" +
                "* _____      _             _       *     \n" +
                " | ____|_ __(_) __ _ _*__ | |_ _*  _ ___ \n" +
                " |  _| | '__| |/ _` | '_ \\| __| | | / __|\n" +
                " | |___| |  | | (_| | | | | |_| |_| \\__ \\\n" +
                " |_____|_|  |_|\\__,_|_| |_|\\__|\\__, |___/\n" +
                "          *                    |___/  *  \n").reset());
                System.out.println("Rule the islands and compete with your friends!");
        while (true) {
            System.out.println("Welcome to the lobby!\nWhat's your name?");
            try {
                nickName = in.nextLine();
                client.registerClient(nickName);
                client.view = StringNames.LOBBY;
                break;
            } catch (UserAlreadyExistsException e) {
                System.out.println(StringNames.USER_ALREADY_EXISTS);
            } catch (RemoteException | NotBoundException e) {
                System.out.println(StringNames.REMOTE);
            } catch (NameFieldException e) {
                System.out.println(StringNames.NAME_FIELD_NULL);
            }
        }
        while (true) {
            System.out.println("O----------------------------------------------------------------------------------------O\n" +
                    "|Possible options: JOIN to join a room; CREATE to create a new room; ROOMS to list rooms;|\n" +
                    "|PLAYERS to list players in current lobby; INFO to view your current room's information; |\n" +
                    "|CHANGE to toggle expert mode for the current lobby; LEAVE to leave current lobby;       |\n" +
                    "|HELP to see this message again.                                                         |\n" +
                    "|When you're ready to go and everyone is in the lobby type START to start the game!      |\n" +
                    "O----------------------------------------------------------------------------------------O");
            //Main room loop
            while (!client.isInGame()) {
                //codice della lobby
                String command = in.nextLine().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
                if (!client.isInGame())
                    switch (command) {
                        case "join":
                            requestRoomJoin();
                            break;
                        case "create":
                            requestRoomCreation();
                            break;
                        case "players":
                            getPlayersInRoom();
                            break;
                        case "rooms":
                            getRooms();
                            break;
                        case "info":
                            getLobbyInfo();
                            break;
                        case "change":
                            setExpertMode();
                            break;
                        case "leave":
                            leaveRoom();
                            break;
                        case "start":
                            startGame();
                            break;
                        case "help":
                            System.out.println("O----------------------------------------------------------------------------------------O\n" +
                                    "|Possible options: JOIN to join a room; CREATE to create a new room; ROOMS to list rooms;|\n" +
                                    "|PLAYERS to list players in current lobby; INFO to view your current room's information; |\n" +
                                    "|CHANGE to toggle expert mode for the current lobby; LEAVE to leave current lobby;       |\n" +
                                    "|HELP to see this message again.                                                         |\n" +
                                    "|When you're ready to go and everyone is in the lobby type START to start the game!      |\n" +
                                    "O----------------------------------------------------------------------------------------O");
                            break;
                        case "\n":
                            System.out.println(command);
                            break;
                        default:
                            System.out.println("Command not recognized");
                            break;
                    }
            }
            System.out.println("Loading...");

            //Main game loop
            //Initializing local professors board
            boolean wait=true;
            while (client.getLocalModel() == null) {
                System.out.print("");
                if (wait) {
                    System.out.println("Waiting for local model");
                    wait=false;
                }
                if (client.getLocalModel()!=null)
                    System.out.println("Local model loaded!");
            }
            for (StrippedBoard s : client.getLocalModel().getBoards()) {
                professorsTables.put(s.getOwner(), s.getProfessorsTable());
            }
            numOfPlayers = client.getLocalModel().getBoards().size();
            while (!client.isInGame()) {
                System.out.print("a");
            }
            while (client.isInGame()) {

                endTurn = false;
                if (playedThisTurn == null)
                    playedThisTurn = new ArrayList<>();
                //Assistant Card play phase
                while (!client.isMyTurn() && client.isInGame()) {
                    //Wait for the other players to be done with their turn while I still output their moves...
                    waitForTurn();
                }
                //Means the game finished while we were waiting for turn
                if (!client.isInGame())
                    break;
                if (client.isMyTurn() && client.getLocalModel().getFirstPlayer().equals(client.getNickname())) {
                    System.out.println("Drawing from bag...");
                    drawFromBag();
                }
                if (client.isMyTurn() && client.getLocalModel().getState().equals(State.PLANNINGPHASE))
                    while (true) {
                        playAssistantCard();
                        break;
                    }

                System.out.println("Waiting for everyone to play an assistant card");
                int i = 0;
                while (numOfPlayers > 1 && !client.isInGame()) {
                    if ((i < 50)) {
                        i++;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(StringNames.INTERRUPTED);
                }
                //Turn phase
                try {
                    if (client.getExpertMode() && client.isInGame()) {
                        System.out.println();
                        System.out.print("\n");
                        while (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3) && !endTurn) {
                            System.out.print("");
                            while (!client.isMyTurn() && client.isInGame()) {
                                waitForTurn();
                                if (!client.isInGame())
                                    break;
                            }
                            System.out.print("");
                            if (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3) && !endTurn && client.isInGame()) {
                                System.out.print("");
                                expertPrintCommandHelp();
                                performActionInTurnExpert();
                                if (!client.isInGame())
                                    break;
                            }
                            if (!client.isInGame())
                                break;
                        }
                        if (!client.isInGame())
                            break;
                        pickCloud();

                    } else if (client.isInGame()) {
                        System.out.println();
                        System.out.print("\n");
                        while (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3) && !endTurn) {
                            System.out.print("");
                            while (!client.isMyTurn() && client.isInGame()) {
                                waitForTurn();
                                if (!client.isInGame())
                                    break;
                            }
                            System.out.print("");
                            if (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3) && !endTurn && client.isInGame()) {
                                System.out.print("");
                                printCommandHelp();
                                performActionInTurn();
                                if (!client.isInGame())
                                    break;
                            }
                            if (!client.isInGame())
                                break;
                        }
                        if (!client.isInGame())
                            break;
                        pickCloud();
                    }
                } catch (RemoteException e) {
                    System.out.println(StringNames.REMOTE);
                } catch (RoomNotExistsException e) {
                    System.out.println(StringNames.ROOM_NOT_EXISTS);
                }
            }

        }
    }

    /**
     * Display method for events. Check UI file for details.
     */
    @Override
    public void professorChanged() {
        ArrayList<StrippedBoard> tmp = new ArrayList<>(client.getLocalModel().getBoards());
        for (StrippedBoard s : tmp) {
            if (!professorsTables.get(s.getOwner()).equals(s.getProfessorsTable())) {
                System.out.println(s.getOwner() + "'s professors changed from ");
                System.out.println(professorsTables.get(s.getOwner()));
                System.out.println(" to ");
                System.out.println(s.getProfessorsTable());
                professorsTables.replace(s.getOwner(), s.getProfessorsTable());
            }
        }
    }

    @Override
    public void characterChanged(PropertyChangeEvent evt) {

    }

    /**
     * Display method for events. Check UI file for details.
     */
    @Override
    public void startGame() {
        if (!client.isInGame()) {
            try {
                client.startGame();
                client.view = StringNames.INGAME;
                client.setInGame(true);
            } catch (NotEnoughPlayersException e) {
                System.out.println(StringNames.NOT_ENOUGH_PLAYERS);
            } catch (UserNotInRoomException e) {
                System.out.println(StringNames.USER_NOT_IN_ROOM);
            } catch (NotLeaderRoomException e) {
                System.out.println(StringNames.NOT_LEADER);
            } catch (RoomNotExistsException e) {
                System.out.println(StringNames.ROOM_NOT_EXISTS);
            } catch (RuntimeException e) {
                System.out.println(StringNames.RUNTIME);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            } catch (UserNotRegisteredException e) {
                System.out.println(StringNames.USER_NOT_REGISTERED);
            }catch (InterruptedException e)
            {System.out.println(StringNames.INTERRUPTED);
            } catch (NegativeValueException e) {
                System.out.println(StringNames.NEGATIVE_VALUE);
            } catch (IncorrectArgumentException e) {
                System.out.println(StringNames.INCORRECT_ARGUMENT);
            }
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param s current player name.
     */
    @Override
    public void currentPlayer(String s) {
        if (!s.equals(client.getNickname())) {
            System.out.println("It's now " + s + "'s turn!");
            client.setMyTurn(false);
        } else {
            client.setMyTurn(true);
            System.out.println("It's your turn! Press any key to continue.");
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e the event that changed the clouds.
     */
    @Override
    public void notifyCloud(PropertyChangeEvent e) {

    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param assistantCard card played by one of the players.
     */
    @Override
    public void deckChange(String assistantCard) {
        String tmp;
        if (!client.getLocalModel().getCurrentPlayer().equals(client.getNickname()))
            System.out.println(client.getLocalModel().getCurrentPlayer() + " has played Assistant card " + assistantCard + "");
        tmp = assistantCard.replaceAll("[^\\d.]", "");
        if (playedThisTurn == null)
            playedThisTurn = new ArrayList<>();
        playedThisTurn.add(Integer.parseInt(tmp));
        numOfPlayers--;
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e island change event
     */
    @Override
    public void islandChange(PropertyChangeEvent e) {
        if (!client.getLocalModel().getCurrentPlayer().equals(client.getNickname())) {
            StrippedIsland newIsland;
            newIsland = (StrippedIsland) e.getNewValue();
            System.out.println(newIsland.getName() + " changed to");
            if(!newIsland.hasNoEnterTile())
                printIsland(newIsland, true);
            else
                printExpertIsland(newIsland, true);
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e island merge event
     */
    @Override
    public void islandMerged(PropertyChangeEvent e) {
        StrippedIsland oldIsland, newIsland;
        oldIsland = (StrippedIsland) e.getOldValue();
        System.out.println("Island " + oldIsland.getName() + " merged!\n");
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e island conquest event
     */
    @Override
    public void islandConquest(PropertyChangeEvent e) {
        StrippedIsland island = (StrippedIsland) e.getNewValue();
        System.out.println(client.getLocalModel().getCurrentPlayer() + " conquered " + island.getName() + "!\n");
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e dining change event.
     */
    @Override
    public void diningChange(PropertyChangeEvent e) {
        String player = (String) e.getOldValue();
        if (!player.equals(client.getNickname())) {
            EnumMap<Colors, Integer> newDining;
            newDining = (EnumMap<Colors, Integer>) e.getNewValue();
            System.out.println("O----------------------------------O");
            System.out.println(e.getOldValue() + " modified their dining room! Here's the new configuration...");
            printDining(newDining);
            System.out.println("O----------------------------------O");
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e entrance changed event.
     */
    @Override
    public void entranceChanged(PropertyChangeEvent e) {
        EnumMap<Colors, Integer> newBoard;
        String player;
        player = (String) e.getOldValue();
        if (!player.equals(client.getNickname())) {
            newBoard = (EnumMap<Colors, Integer>) e.getNewValue();
            System.out.println();
            System.out.println("O----------------------------------O");
            System.out.println(player + "'s entrance changed to");
            printEntrance(newBoard);
            System.out.println("O----------------------------------O");
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e tower change event.
     */
    @Override
    public void towersEvent(PropertyChangeEvent e) {

        String player = (String) e.getOldValue();
        int i = (int) e.getNewValue();
        if (!player.equals(client.getNickname())) {
            System.out.println("O--------------------------------------------------------------O");
            System.out.println("Player " + player + " now has " + i + " towers in their board!\n");
            System.out.println("O--------------------------------------------------------------O");
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param leavingPlayer The leaving player in case of quit-out and remaking of game
     * @param winner        Winner team if the game reached a valid end.
     */
    @Override
    public void gameOver(String leavingPlayer, String winner) {
        if (winner != null) {
            System.out.println("Game over! Team " + winner + "won! Congratulations!\n");
        } else {
            System.out.println("Player " + leavingPlayer + " left the game. Everyone will be put back into the lobby.");
        }
        client.setMyTurn(false);
        client.setInGame(false);
        clientRoom = null;
        client.view = StringNames.LOBBY;
        try {
            in.nextLine();
        }catch(IndexOutOfBoundsException e)
        {
            System.out.println();
        }
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param e coins changed event.
     */
    @Override
    public void coinsChanged(PropertyChangeEvent e) {

    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param rooms the rooms to display on the server.
     */
    @Override
    public void roomsAvailable(ArrayList<String> rooms) {
        getRooms();
    }

    /**
     * Display method for events. Check UI file for details.
     *
     * @param players players in the room
     */
    @Override
    public void roomJoin(ArrayList<String> players) {
        getPlayersInRoom();
    }

    /**
     * Method that returns all the players in a room.
     */
    private void getPlayersInRoom() {
        if (clientRoom != null) {
            ArrayList<String> response = new ArrayList<>();
            try {
                response = client.getNicknamesInRoom();
            } catch (RoomNotExistsException e) {
                System.out.println(StringNames.ROOM_NOT_EXISTS);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            }catch(UserNotInRoomException e)
            {System.out.println(StringNames.USER_NOT_IN_ROOM);
            }
            sendArrayString(response);
        }
        // System.out.println("You're not in a room, so there are no players to show\n"); //TODO StringName.USER_NOT_..
    }

    /**
     * Returns the lobby name, leader and whether you're in expert mode or not.
     */
    private void getLobbyInfo() {
        if (clientRoom != null) {
            ArrayList<String> result = new ArrayList<>();
            try {
                result = client.requestLobbyInfo(clientRoom);
            } catch (RoomNotExistsException e) {
               System.out.println(StringNames.ROOM_NOT_EXISTS);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            }
            System.out.println("Lobby name: " + result.get(0));
            System.out.println("Leader: " + result.get(1));
            System.out.println("Expert mode: " + result.get(2));
        } else
            System.out.println("You're not in a room yet\n");
    }

    /**
     * Method used to leave the room.
     */
    private void leaveRoom() {
        try {
            client.leaveRoom();
            clientRoom=null;
        } catch (UserNotInRoomException e) {
            System.out.println("You're not in a room yet\n");
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        }
    }

    /**
     * Method used to return all the rooms on the server.
     */
    public void getRooms() {
        client.view = StringNames.LOBBY;
        client.setInGame(false);
        System.out.println("Rooms on the server: ");
        ArrayList<String> response = null;
        try {
            response = client.getRooms();
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        }

        if (response != null) {
            if (response.isEmpty()) System.out.println("There are no rooms yet\n");
            else sendArrayString(response);
        }
    }

    /**
     * Method used to switch game mode in the room.
     */
    public void setExpertMode() {
        boolean result = false;
        System.out.println("Do you want to play in expert mode? Y/N");
        String answer;
        answer = in.nextLine().toLowerCase(Locale.ROOT);
        switch (answer) {
            case "y": {
                result = true;
                break;
            }
            case "n": {
                break;
            }
            default:
                System.out.println("Command not recognized\n");
        }
        try {
            client.setExpertMode(result);
            if (result)
                System.out.println("Expert mode enabled!\n");
            else
                System.out.println("Expert mode disabled|\n");
        } catch (UserNotInRoomException e) {
            System.out.println(StringNames.USER_NOT_IN_ROOM);
        } catch (NotLeaderRoomException e) {
            System.out.println(StringNames.NOT_LEADER);
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        }
    }

    /**
     * Method used to call for a room creation from Client.
     */
    public synchronized void requestRoomCreation() {
        System.out.println("Insert room name: \n");
        String nameRoom;
        nameRoom = in.nextLine();
        while (true) {
            try {
                if (!client.getRooms().contains(nameRoom)) break;
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            }
            System.out.println("Ops, there is another room with the same name! Choose another one please. \n");
            nameRoom = in.nextLine();
        }
        try {
            client.createRoom(nameRoom);
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        } catch (NameFieldException e) {
            System.out.println(StringNames.NAME_FIELD_NULL);
        }catch (RoomAlreadyExistsException e)
        {
            System.out.println(StringNames.ROOM_ALREADY_EXISTS);
        }
        if (clientRoom!=null)
            leaveRoom();
        clientRoom = nameRoom;
    }

    /**
     * Method used to request room join from Client.
     */
    public synchronized void requestRoomJoin() {
        String requestedRoom;
        System.out.println("Select the room: \n");
        try {
            if (client.getRooms().isEmpty()) System.out.println("There are no rooms, you can only create a new one");
            else {
                sendArrayString(client.getRooms());
                requestedRoom = in.nextLine().trim();
                while (!client.getRooms().contains(requestedRoom)) {
                    System.out.println("Ops, there are no rooms with that name: try again. If you want to exit instead type EXIT.\n");
                    requestedRoom = in.nextLine();
                    if (requestedRoom.toLowerCase(Locale.ROOT).trim().equals("exit")) {
                        System.out.println("Gotcha, leaving room join!\n");
                        return;
                    }
                }
                if (requestedRoom.equals(client.getRoom())) {
                    System.out.println("You're already in that room!\n");
                } else {
                    try {
                        if (clientRoom != null)
                            leaveRoom();
                        client.requestRoomJoin(requestedRoom);
                        client.view = StringNames.ROOM;
                        clientRoom = requestedRoom;
                        System.out.println("You entered room " + clientRoom + " successfully \n");
                        System.out.println("Players in this room:");
                        try {
                            if (!client.getExpertMode())
                            sendArrayString(client.getNicknamesInRoom());
                        } catch (RoomNotExistsException e) {
                            System.out.println(StringNames.ROOM_NOT_EXISTS + "1");
                        } catch (UserNotInRoomException e) {
                            System.out.println(StringNames.USER_NOT_IN_ROOM);
                        }
                    } catch (RoomNotExistsException e) {
                        System.out.println(StringNames.ROOM_NOT_EXISTS + "2");
                    } catch (RoomFullException e) {
                        System.out.println(StringNames.ROOM_FULL);
                    } catch (UserInRoomException e) {
                        System.out.println(StringNames.USER_IN_ROOM);
                    } catch (UserNotRegisteredException e) {
                        System.out.println(StringNames.USER_NOT_REGISTERED);
                    } catch (RoomInGameException e) {
                        System.out.println(StringNames.ROOM_IN_GAME);
                    }
                }
            }
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        }
    }

    // **********************************   Gameplay methods    ***************************************************************

    /**
     * Method used to leave the game.
     */
    public void leaveGame() {
        boolean isValidInputYN = false;
        String answer;
        System.out.println("Are you sure you want to do this? Y/N");
        in.nextLine();
        do {
            answer = in.nextLine();
            answer = answer.toLowerCase(Locale.ROOT);
            if (answer.equals("y") || answer.equals("n"))
                isValidInputYN = true;
            else if (answer.equals("\n")) {
            } else
                System.out.println("Whoops! That's not right. Try again: \n");
        } while (!isValidInputYN);
        if (answer.equals("y")) {
           /* client.setMyTurn(false);
            client.setInGame(false);
            client.view = StringNames.LOBBY;
            */
            clientRoom = null;
            try {
                client.leaveGame();
                Thread.sleep(500);
            } catch (UserNotInRoomException e) {
                System.out.println(StringNames.USER_NOT_IN_ROOM);
            } catch (UserNotRegisteredException e) {
                System.out.println(StringNames.USER_NOT_REGISTERED);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
        } else {
            System.out.println("Gotcha! Returning to the game.");
        }
    }

    /**
     * Method used to draw
     */
    public void drawFromBag() {
        drawFromBagOrder = new DrawFromBagCommand(client.getNickname());
        System.out.println("Drawing from bag...\n");
        try {
            client.performGameAction(drawFromBagOrder);
        } catch (NotEnoughCoinsException e) {
            System.out.println(StringNames.NOT_ENOUGH_COINS);
        } catch (AssistantCardNotFoundException e) {
            System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
        } catch (NegativeValueException e) {
            System.out.println(StringNames.NEGATIVE_VALUE);
        } catch (IncorrectStateException e) {
            System.out.println(StringNames.INCORRECT_STATE);
        } catch (MotherNatureLostException e) {
            System.out.println(StringNames.MOTHER_NATURE_LOST);
        } catch (ProfessorNotFoundException e) {
            System.out.println(StringNames.PROFESSOR_NOT_FOUND);
        } catch (IncorrectPlayerException e) {
            System.out.println(StringNames.INCORRECT_PLAYER);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        } catch (IncorrectArgumentException e) {
            System.out.println(StringNames.INCORRECT_ARGUMENT);
        } catch (UserNotInRoomException e) {
            System.out.println(StringNames.USER_NOT_IN_ROOM);
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        } catch (FullDiningException e) {
            System.out.println(StringNames.FULL_DINING);
        } catch (CardPlayedInTurnException e) {
            System.out.println(StringNames.CARD_PLAYED_IN_TURN);
        } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
            System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
        }
    }

    /**
     * Method used to play assistant cards.
     */
    public synchronized void playAssistantCard()  {
        System.out.println("It's your turn! Pick an assistant card to play. \n");
        printAssistantCards();
        int i;
        String input;
        while (true) {
            input = in.next();
            try {
                i = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(StringNames.NUMBER_FORMAT);
            }
        }
        while (true) {
            try {
                if (!(i < 1 || i > client.getLocalModel().getBoardOf(client.getNickname()).getDeck().getDeck().size()))
                    break;
            } catch (LocalModelNotLoadedException e) {
                System.out.println(StringNames.LOCAL_MODEL_ERROR);
            }
            System.out.println("Invalid number, try again\n");
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    if(i>0||i<client.getLocalModel().getBoardOf(client.getNickname()).getDeck().getDeck().size())
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(StringNames.NUMBER_FORMAT);
                } catch (LocalModelNotLoadedException e) {
                    System.out.println(StringNames.LOCAL_MODEL_ERROR);
                }
            }
        }

        //I now have a valid assistant card
        System.out.println("Card played: " + i);
        try {
            input=client.getLocalModel().getBoardOf(client.getNickname()).getDeck().getDeck().get(i-1).getImageName();
        } catch (LocalModelNotLoadedException e) {
            System.out.println("Critical local model error");
        }
        playAssistantCardOrder = new PlayAssistantCard(client.getNickname(), input);
        try {
            client.performGameAction(playAssistantCardOrder);
            client.getLocalModel().getBoardOf(client.getNickname()).setMoves(client.getLocalModel().getBoardOf(client.getNickname()).getDeck().get(input).getMove());
            movedStudents=0;
            //Resets number of students moved at the beginning of the turn
        } catch (UserNotInRoomException e) {
            System.out.println(StringNames.ROOM_ALREADY_EXISTS);
        } catch (AssistantCardNotFoundException e) {
            System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
        } catch (FullDiningException e) {
            System.out.println(StringNames.FULL_DINING);
        } catch (NegativeValueException e) {
            System.out.println(StringNames.NEGATIVE_VALUE);
        } catch (IncorrectStateException e) {
            System.out.println(StringNames.INCORRECT_STATE);
        } catch (MotherNatureLostException e) {
            System.out.println(StringNames.MOTHER_NATURE_LOST);
        } catch (ProfessorNotFoundException e) {
            System.out.println(StringNames.PROFESSOR_NOT_FOUND);
        } catch (IncorrectPlayerException e) {
            System.out.println(StringNames.INCORRECT_PLAYER);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        } catch (IncorrectArgumentException e) {
            System.out.println(StringNames.INCORRECT_ARGUMENT);
        } catch (NotEnoughCoinsException e) {
            System.out.println(StringNames.NOT_ENOUGH_COINS);
        } catch (CardPlayedInTurnException e) {
            System.out.println(StringNames.CARD_PLAYED_IN_TURN);
        } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
            System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
            playAssistantCard();
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        } catch (LocalModelNotLoadedException e) {
           System.out.println(StringNames.LOCAL_MODEL_ERROR);
        }
        client.setMyTurn(false);
    }

    /**
     * Method used to choose the actions the player wants to perform in their turn and also print game info such as clouds, islands and player boards.
     */
    public void performActionInTurn(){
        do {
            System.out.println("Select an action: ");
            String input;
            while (true) {
                input = in.next();
                try {
                    action = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(StringNames.NUMBER_FORMAT);
                }
            }
        } while (action < 1 || action > 8);
        switch (action) {
            case 1:
                printPlayerBoards();
                break;
            case 2:
                printPlayerNames();
                break;
            case 3:
                printIslands();
                break;
            case 4:
                printClouds();
                break;
            case 5:
                moveStudents();
                break;
            case 6:
                moveMN();
                break;
            case 7:
                printCommandHelp();
                break;
            case 8:
                    leaveGame();
                break;
            default:
                System.out.println("Invalid input, try again\n");
        }
    }

    /**
     * Basically the same method as performActionInTurn, but also contains character cards related methods.
     */
    public void performActionInTurnExpert() {
        do {
            System.out.println("Select an action: ");
            String input;
            while (true) {
                input = in.next();
                try {
                    action = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(StringNames.NUMBER_FORMAT);
                }
            }
        } while (action < 1 || action > 10);
        switch (action) {
            case 1:
                printPlayerBoards();
                break;
            case 2:
                printPlayerNames();
                break;
            case 3:
                printExpertIslands();
                break;
            case 4:
                printClouds();
                break;
            case 5:
                moveStudents();
                break;
            case 6:
                moveMN();
                break;
            case 7:
                printCharacterCards();
                break;
            case 8:
                playCharacterCard();
                break;
            case 9:
                expertPrintCommandHelp();
                break;
            case 10:
                    leaveGame();
                break;
            default:
        }

    }

    /**
     * Method used to pick a cloud tile at the end of the player turn.
     */
    public void pickCloud() {
        System.out.println("Almost at the end of your turn! Pick a cloud to refill your entrance.\n");
        printClouds();
        int i;
        String input;
        EnumMap<Colors, Integer> emptyEnum = new EnumMap<>(Colors.class);
        emptyEnum = initializeMove(emptyEnum);
        boolean invalidCloud = true;
        do {
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(StringNames.NUMBER_FORMAT);
                }
            }
            while (i < 1 || i > client.getLocalModel().getClouds().size() + 1) {
                System.out.println("Invalid cloud number! Try again.\n");
                while (true) {
                    input = in.next();
                    try {
                        i = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println(StringNames.NUMBER_FORMAT);
                    }
                }
            }
            for (StrippedCloud c : client.getLocalModel().getClouds()) {
                if (c.getName().equals("cloud" + i)) {
                    if (c.getStudents().equals(emptyEnum))
                        System.out.println("Someone already picked that cloud! Try another.");
                    else
                        invalidCloud = false;
                }
            }

        } while (invalidCloud);

        //Reached a valid cloud color
        pickCloudOrder = new PickCloud(client.getNickname(), "cloud" + i);
        try {
            client.performGameAction(pickCloudOrder);
        } catch (AssistantCardNotFoundException e) {
            System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
        } catch (NegativeValueException e) {
            System.out.println(StringNames.NEGATIVE_VALUE);
        } catch (IncorrectStateException e) {
            System.out.println(StringNames.INCORRECT_STATE);
        } catch (MotherNatureLostException e) {
            System.out.println(StringNames.MOTHER_NATURE_LOST);
        } catch (ProfessorNotFoundException e) {
            System.out.println(StringNames.PROFESSOR_NOT_FOUND);
        } catch (IncorrectPlayerException e) {
            System.out.println(StringNames.INCORRECT_PLAYER);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        } catch (IncorrectArgumentException e) {
            System.out.println(StringNames.INCORRECT_ARGUMENT);
        } catch (UserNotInRoomException e) {
            System.out.println(StringNames.USER_IN_ROOM);
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        } catch (NotEnoughCoinsException e) {
            System.out.println(StringNames.NOT_ENOUGH_COINS);
        } catch (FullDiningException e) {
            System.out.println(StringNames.FULL_DINING);
        } catch (CardPlayedInTurnException e) {
            System.out.println(StringNames.CARD_PLAYED_IN_TURN);
        } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
            System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
        }
        client.setMyTurn(false);
    }

    /**
     * Method used to move Mother Nature after moving the students.
     */
    public void moveMN()  {
        if (client.getLocalModel().isCanPlayMN()) {
            try {
                System.out.println("Input the number of steps you want Mother Nature to move! The maximum steps according to the card you played are \n " + client.getLocalModel().getBoardOf(client.getNickname()).getMoves());
            } catch (LocalModelNotLoadedException e) {
               System.out.println(StringNames.LOCAL_MODEL_ERROR);
            }
            String input;
            int i;
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(StringNames.NUMBER_FORMAT);
                }
            }
            while (true) {
                try {
                    if (!(i <= 0 || i > client.getLocalModel().getBoardOf(client.getNickname()).getMoves())) break;
                } catch (LocalModelNotLoadedException e) {
                    System.out.println(StringNames.LOCAL_MODEL_ERROR);
                }
                System.out.println("That number is not right! Try again.\n");
                while (true) {
                    input = in.next();
                    try {
                        i = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println(StringNames.NUMBER_FORMAT);
                    }
                }
            }
            moveMotherNatureOrder = new MoveMotherNature(client.getNickname(), i);
            try {
                client.performGameAction(moveMotherNatureOrder);

            } catch (IncorrectStateException e) {
                System.out.println(StringNames.INCORRECT_STATE);
            } catch (IncorrectArgumentException e) {
                System.out.println(StringNames.INCORRECT_ARGUMENT);
                moveMN();
            } catch (FullDiningException e) {
                System.out.println(StringNames.FULL_DINING);
            } catch (CardPlayedInTurnException e) {
                System.out.println(StringNames.CARD_PLAYED_IN_TURN);
            } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
            } catch (AssistantCardNotFoundException e) {
               System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
            } catch (UserNotInRoomException e) {
                System.out.println(StringNames.USER_NOT_IN_ROOM);
            } catch (NegativeValueException e) {
                System.out.println(StringNames.NEGATIVE_VALUE);
            } catch (MotherNatureLostException e) {
                System.out.println(StringNames.MOTHER_NATURE_LOST);
            } catch (ProfessorNotFoundException e) {
                System.out.println(StringNames.PROFESSOR_NOT_FOUND);
            } catch (UserNotRegisteredException e) {
                System.out.println(StringNames.USER_NOT_REGISTERED);
            } catch (IncorrectPlayerException e) {
                System.out.println(StringNames.INCORRECT_PLAYER);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            }
            //After my turn is over I set the Mother Nature flag to false for the next turn
            client.getLocalModel().setCanPlayMN(false);
            endTurn = true;
        } else System.out.println("You can't move Mother Nature yet! First things first: move three students!");
    }

    /**
     * Method used to move students in the mandatory dining/island phase of the turn.
     */
    public void moveStudents() {
        if (!client.getLocalModel().isCanPlayMN()) {
            StrippedBoard myBoard = null;
            try {
                myBoard = client.getLocalModel().getBoardOf(client.getNickname());
            } catch (LocalModelNotLoadedException e) {
                System.out.println(StringNames.LOCAL_MODEL_ERROR);
            }
            int maxStudents;
            if (client.getLocalModel().getBoards().size() == 3)
                maxStudents = 4;
            else
                maxStudents = 3;
            System.out.println("These are the available islands: ");
            try {
                if (client.getExpertMode())
                    printExpertIslands();
                else
                    printIslands();
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            } catch (RoomNotExistsException e) {
                System.out.println(StringNames.ROOM_NOT_EXISTS);
            }
            System.out.println("These are the students in your entrance: \n");
            if (myBoard != null) {
                printEntrance(myBoard);
            }
            int islandChosen;
            String tmp;
            String answer;
            String[] parts;
            Colors color = null;
            boolean isValidInputYN = false;
            boolean doItAgain;
            System.out.println("Do you want to move students to the dining room? Y\\N\n");
            in.nextLine();
            do {
                answer = in.nextLine();
                answer = answer.toLowerCase(Locale.ROOT);
                if (answer.equals("y") || answer.equals("n"))
                    isValidInputYN = true;
                else if (answer.equals("\n")) {
                } else
                    System.out.println("Whoops! That's not right. Try again: \n");
            } while (!isValidInputYN);
            //Move students to the dining room
            doItAgain = true;
            isValidInputYN = false;

            if (answer.equals("y") && client.isInGame()) {
                do {
                    while (true) {
                        System.out.println("Choose the color of the student you want to move to the dining room! ");
                        answer = in.nextLine();
                        answer = answer.trim().toLowerCase(Locale.ROOT);
                        if (isValidColor(answer) && movedStudents < maxStudents) {
                            color = stringToColor(answer);
                            movedStudents++;
                            break;
                        } else
                            System.out.println("Invalid color! Try again.");
                    }
                    if (myBoard.getEntrance().get(color) >= 1 && client.isInGame()) {
                        moveStudentsOrder = new MoveStudents(client.getNickname(), color, "dining");
                        try {
                            client.performGameAction(moveStudentsOrder);
                        } catch (NotEnoughCoinsException e) {
                            System.out.println(StringNames.NOT_ENOUGH_COINS);
                        } catch (FullDiningException e) {
                            System.out.println(StringNames.FULL_DINING);
                        } catch (AssistantCardNotFoundException e) {
                            System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
                        } catch (NegativeValueException e) {
                            System.out.println(StringNames.NEGATIVE_VALUE);
                        } catch (IncorrectStateException e) {
                            System.out.println(StringNames.INCORRECT_STATE);
                        } catch (MotherNatureLostException e) {
                            System.out.println(StringNames.MOTHER_NATURE_LOST);
                        } catch (ProfessorNotFoundException e) {
                            System.out.println(StringNames.PROFESSOR_NOT_FOUND);
                        } catch (IncorrectPlayerException e) {
                            System.out.println(StringNames.INCORRECT_PLAYER);
                        } catch (RemoteException e) {
                            System.out.println(StringNames.REMOTE);
                        } catch (IncorrectArgumentException e) {
                            System.out.println(StringNames.INCORRECT_ARGUMENT);
                        } catch (UserNotInRoomException e) {
                            System.out.println(StringNames.USER_NOT_IN_ROOM);
                        } catch (UserNotRegisteredException e) {
                            System.out.println(StringNames.USER_NOT_REGISTERED);
                        } catch (CardPlayedInTurnException e) {
                            System.out.println(StringNames.CARD_PLAYED_IN_TURN);
                        } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                            System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                        }
                    } else {
                        System.out.println("You don't have enough students of that color in your entrance.");
                    }
                    if (movedStudents < maxStudents && client.isInGame()) {
                        System.out.println("Do you want to move other students in the dining room?");
                        do {
                            answer = in.nextLine();
                            answer = answer.toLowerCase(Locale.ROOT);
                            if (answer.equals("y") || answer.equals("n"))
                                isValidInputYN = true;
                            else if (answer.equals("\n")) {
                            } else
                                System.out.println("Whoops! That's not right. Try again: \n");
                        } while (!isValidInputYN);
                        if (answer.equals("y") && movedStudents < maxStudents && client.isInGame()) {
                            System.out.println("Alright, ready to move another student!");
                            printEntrance(myBoard);
                        } else if (movedStudents == maxStudents) {
                            System.out.println("You can't move any more students this turn!");
                            doItAgain = false;
                        } else
                            doItAgain = false;
                    } else
                        doItAgain = false;
                } while (doItAgain);

                //End of dining room move segment
                //If the player has already moved the maximum number of students he can move mother nature
                if (movedStudents == maxStudents)
                    client.getLocalModel().setCanPlayMN(true);
            }
            isValidInputYN = false;
            if (movedStudents < maxStudents && client.isInGame())
            {
                System.out.println("Do you want to move students to the islands?");
            do {
                answer = in.nextLine();
                answer = answer.toLowerCase(Locale.ROOT);
                if (answer.equals("y") || answer.equals("n"))
                    isValidInputYN = true;
                else if (answer.equals("\n")) {
                } else
                    System.out.println("Whoops! That's not right. Try again: \n");
            } while (!isValidInputYN);

            //Move students to the islands if the player has moved less than 3 students already
            //Resetting destinations array for students to island part
            if (answer.equals("y") && (movedStudents < maxStudents) && client.isInGame()) {
                do {
                    while (true) {
                        try {
                            if (client.getExpertMode())
                                printExpertIslands();
                            else
                                printIslands();
                            printEntrance(myBoard);
                            System.out.println("Type the students you want to move to the island as \"color, island number\"!");
                        } catch (RemoteException e) {
                            System.out.println(StringNames.REMOTE);
                        } catch (RoomNotExistsException e) {
                            System.out.println(StringNames.ROOM_NOT_EXISTS);
                        }
                        answer = in.nextLine();
                        // System.out.println("answer "+ answer);
                        parts = answer.split(",|, | ,");
                        tmp = parts[0];
                        tmp = tmp.replaceAll("[^a-zA Z0-9]", "");
                        try {
                            parts[1] = parts[1].trim();
                            try {
                                islandChosen = Integer.parseInt(parts[1]);
                                tmp = tmp.toUpperCase(Locale.ROOT);
                                break;
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                System.out.println("Something went wrong with your input, try again!");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                        }
                    }
                    //Now I should have a number and a color
                    doItAgain = true;
                    String islandName=null;
                    if (isValidColor(tmp)) {
                        if (myBoard.getEntrance().get(stringToColor(tmp)) >= 1) {
                            color = stringToColor(tmp);
                            movedStudents++;
                            int count = 0;
                            for (StrippedIsland trueIsland : client.getLocalModel().getIslands()) {
                                if (!trueIsland.getName().equals("EMPTY")) {
                                    count++;
                                    if(count==islandChosen)
                                        islandName=trueIsland.getName();

                                }
                            }
                            if (islandChosen > 0 && islandChosen <= count) {
                                if (movedStudents > maxStudents)
                                    doItAgain = false;
                                int counter = 0;
                                for (StrippedIsland island : client.getLocalModel().getIslands()) {
                                    if (!island.getName().equals("EMPTY")) {
                                        if (counter+1>=islandChosen)
                                            break;
                                        counter++;
                                    }
                                }
                                moveStudentsOrder = new MoveStudents(client.getNickname(), color, islandName);
                                try {
                                    client.performGameAction(moveStudentsOrder);
                                } catch (NotEnoughCoinsException e) {
                                    System.out.println(StringNames.NOT_ENOUGH_COINS);
                                } catch (FullDiningException e) {
                                    System.out.println(StringNames.FULL_DINING);
                                } catch (AssistantCardNotFoundException e) {
                                    System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
                                } catch (NegativeValueException e) {
                                    System.out.println(StringNames.NEGATIVE_VALUE);
                                } catch (IncorrectStateException e) {
                                    System.out.println(StringNames.INCORRECT_STATE);
                                } catch (MotherNatureLostException e) {
                                    System.out.println(StringNames.MOTHER_NATURE_LOST);
                                } catch (ProfessorNotFoundException e) {
                                    System.out.println(StringNames.PROFESSOR_NOT_FOUND);
                                } catch (IncorrectPlayerException e) {
                                    System.out.println(StringNames.INCORRECT_PLAYER);
                                } catch (RemoteException e) {
                                    System.out.println(StringNames.REMOTE);
                                } catch (IncorrectArgumentException e) {
                                    System.out.println(StringNames.INCORRECT_ARGUMENT);
                                } catch (UserNotInRoomException e) {
                                    System.out.println(StringNames.USER_NOT_IN_ROOM);
                                } catch (UserNotRegisteredException e) {
                                    System.out.println(StringNames.USER_NOT_REGISTERED);
                                } catch (CardPlayedInTurnException e) {
                                    System.out.println(StringNames.CARD_PLAYED_IN_TURN);
                                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                                    System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                                }
                                if (movedStudents < maxStudents) {
                                    System.out.println("You still have " + (maxStudents - movedStudents) + " students to move!\n");
                                    System.out.println("Do you want to move them now?");
                                    isValidInputYN = false;
                                    do {
                                        answer = in.next();
                                        answer = answer.toLowerCase(Locale.ROOT);
                                        if (answer.equals("y") || answer.equals("n"))
                                            isValidInputYN = true;
                                        else if (answer.equals("\n")) {
                                        } else
                                            System.out.println("Whoops! That's not right. Try again: \n");
                                    } while (!isValidInputYN);
                                    if (answer.equals("y"))
                                        in.nextLine();
                                    else {
                                        System.out.println("Ok, returning to the menu!");
                                        break;
                                    }
                                } else {
                                    client.getLocalModel().setCanPlayMN(true);
                                    doItAgain = false;
                                }
                            } else {
                                System.out.println("Invalid island number! Try again.\n");
                            }
                        } else {
                            System.out.println("You don't have enough students of that color! Try again.\n");
                        }
                    } else {
                        System.out.println("There is no such color as " + color + "! Try again. \n");
                    }
                } while (doItAgain);

            } else {
                if (maxStudents == movedStudents)
                    System.out.println("You already moved enough students this turn\n");
                else
                    System.out.println("Remember you have to move " + maxStudents + " before you can move Mother Nature!");
            }
        } else
            System.out.println("You already moved enough students this turn, you can't do that anymore!\n");
    }
        }
        


    //End of MoveStudents

    // **********************************   Character card methods    ***************************************************************

    /**
     * Method used to play a character card. Splits off in several children methods based on parameters needed.
     */
    public void playCharacterCard() {
        StrippedCharacter tmp;
        System.out.println("Select the character you want to play! You currently have " + client.getLocalModel().getBoards().get(playerNumber).getCoins() + " coins \n");
        printCharacterCards();
        int i;
        String input;
        while (true) {
            input = in.next();
            try {
                i = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(StringNames.NUMBER_FORMAT);
            }
        }
        i--;
        while (i < 0 || i > 2) {
            System.out.println("That's not right! Try again\n");
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println(StringNames.NUMBER_FORMAT);
                }
            }
        }
        tmp = client.getLocalModel().getCharacters().get(i);
        // System.out.println(tmp.getRequirements().getRequirements().toLowerCase(Locale.ROOT));
        try {
            if (client.getLocalModel().getBoardOf(client.getNickname()).getCoins()>=tmp.getPrice())
            switch (tmp.getDescription()) {
                //Automatic action card
                case "If somebody tries to Conquer an island, ignore the towers for this turn!":
                case "Add 2 to your Influence this turn!":
                case "Snag an opponent's professor if you have the same number of their students!":
                    playCharacterA(i);
                    break;
                //MN movement
                case "You may move Mother Nature up to 2 additional spaces!":
                    //Updating localmodel for +2 moves
                    try {
                        client.getLocalModel().getBoardOf(client.getNickname()).setMoves(client.getLocalModel().getBoardOf(client.getNickname()).getMoves() + 2);
                    } catch (LocalModelNotLoadedException e) {
                        System.out.println(StringNames.LOCAL_MODEL_ERROR);
                    }
                    playCharacterA(i);
                    break;
                //Island and student card
                case "Move students on the character card to any isle you wish!":
                    playCharacterB(i, tmp);
                    break;
                //Students on card card
                case "Swap 3 of the students on this card with 3 from your Entrance!":
                case "Switch up to two students between your Entrance and your Dining Room!":
                    playCharacterC(i, tmp);
                    break;
                //No STUDENTS used but input needed card (no entry tiles are also here since you have to pick the island)
                case "Calculate the influence on any Island and reap the rewards!":
                case "Place a No Entry card on an isle and block Mother Nature's power once!":
                case "Choose a student color to ignore when calculating Influence this turn!":
                case "Choose a color: EVERYONE must return 3 students of that color to the bag!":
                case "Take one student from this card and put them in the dining room, then draw another from the bag and replace them!":
                    playCharacterD(i);

            }
            else
                System.out.println(StringNames.NOT_ENOUGH_COINS);
        } catch (LocalModelNotLoadedException e) {
            System.out.println(StringNames.LOCAL_MODEL_ERROR);
        }


    }

    /**
     * Character card method that is used to play automatic action cards.
     *
     * @param id The ID of the character card played.
     */
    public void playCharacterA(int id) {
        System.out.println("You have chosen a no parameter character! Buckle up, the effects are on the way!\n");
        playCharacterCardAOrder = new PlayCharacterCardA(client.getNickname(), id);
        try {
            client.performGameAction(playCharacterCardAOrder);
        } catch (UserNotInRoomException e) {
            System.out.println(StringNames.USER_NOT_IN_ROOM);
        } catch (NotEnoughCoinsException e) {
            System.out.println(StringNames.NOT_ENOUGH_COINS);
        } catch (FullDiningException e) {
            System.out.println(StringNames.FULL_DINING);
        } catch (AssistantCardNotFoundException e) {
            System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
        } catch (NegativeValueException e) {
            System.out.println(StringNames.NEGATIVE_VALUE);
        } catch (IncorrectStateException e) {
            System.out.println(StringNames.INCORRECT_STATE);
        } catch (MotherNatureLostException e) {
            System.out.println(StringNames.MOTHER_NATURE_LOST);
        } catch (ProfessorNotFoundException e) {
            System.out.println(StringNames.PROFESSOR_NOT_FOUND);
        } catch (IncorrectPlayerException e) {
            System.out.println(StringNames.INCORRECT_PLAYER);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        } catch (IncorrectArgumentException e) {
            System.out.println(StringNames.INCORRECT_ARGUMENT);
        } catch (CardPlayedInTurnException e) {
            System.out.println(StringNames.CARD_PLAYED_IN_TURN);
        } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
            System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
        }catch (UserNotRegisteredException e)
        {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        }

    }

    //This is only one card but it's unique since it calls for a color AND an island.

    /**
     * Method to call Character Card B. This type of card requires an island and a set of students on the card.
     *
     * @param id        the ID of the character card.
     * @param character The character card itself, since we need to have the students present on it.
     */
    public void playCharacterB(int id, StrippedCharacter character) {
        //System.out.println("You have chosen a student island card\n");
        int student, island;
        System.out.println(character.getDescription());
        printStudentsOnCard(character);
        System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
        System.out.println();
        String input;
        boolean hasStudents=false;
        do{
        while (true) {
            input = in.next();
            try {
                student = Integer.parseInt(input);
                if (student >= 0 && student <= 5)
                    break;
                else
                    System.out.println("Invalid number! Try again.");
            } catch (NumberFormatException e) {
                System.out.println(StringNames.NUMBER_FORMAT);
            }
        }
            if (character.getStudents().get(Colors.values()[student])!=0)
                hasStudents=true;
            else
                System.out.println("There are no students of that color on the card! Try again.");
        }while(!hasStudents);
        printExpertIslands();
        int count=0;
        for (StrippedIsland trueIsland: client.getLocalModel().getIslands())
        {
            if (!trueIsland.getName().equals("EMPTY"))
                count++;
        }

        while (true) {
            input = in.next();
            try {
                island = Integer.parseInt(input);
                if (island > 0 && island < count+1)
                    break;
                else
                    System.out.println("Invalid island! Try again.");
            } catch (NumberFormatException e) {
                System.out.println(StringNames.NUMBER_FORMAT);
            }
        }
        int counter=0;
        while(counter<client.getLocalModel().getIslands().size()&&counter<island)
        {
            if (client.getLocalModel().getIslands().get(counter).getName().equals("EMPTY"))
            {
                counter++;
            }
            counter++;
        }
        counter--;
        //Now I have a valid non merged island and color choice.
        playCharacterCardBOrder = new PlayCharacterCardB(client.getNickname(), id, student, counter);
        try {
            client.performGameAction(playCharacterCardBOrder);
        } catch (UserNotInRoomException e) {
            System.out.println(StringNames.USER_NOT_IN_ROOM);
        } catch (NotEnoughCoinsException e) {
            System.out.println(StringNames.NOT_ENOUGH_COINS);
        } catch (FullDiningException e) {
            System.out.println(StringNames.FULL_DINING);
        } catch (AssistantCardNotFoundException e) {
            System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
        } catch (NegativeValueException e) {
            System.out.println(StringNames.NEGATIVE_VALUE);
        } catch (IncorrectStateException e) {
            System.out.println(StringNames.INCORRECT_STATE);
        } catch (MotherNatureLostException e) {
            System.out.println(StringNames.MOTHER_NATURE_LOST);
        } catch (ProfessorNotFoundException e) {
            System.out.println(StringNames.PROFESSOR_NOT_FOUND);
        } catch (IncorrectPlayerException e) {
            System.out.println(StringNames.INCORRECT_PLAYER);
        } catch (RemoteException e) {
            System.out.println(StringNames.REMOTE);
        } catch (IncorrectArgumentException e) {
            System.out.println(StringNames.INCORRECT_ARGUMENT);
        } catch (CardPlayedInTurnException e) {
            System.out.println(StringNames.CARD_PLAYED_IN_TURN);
        } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
            System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
        } catch (UserNotRegisteredException e) {
            System.out.println(StringNames.USER_NOT_REGISTERED);
        }
    }

    /**
     * Method used to call Character card type C. This type of card requires two sets of students.
     *
     * @param id   the ID of the character card.
     * @param card The character card itself.
     */
    public void playCharacterC(int id, StrippedCharacter card) {
        System.out.println("You have chosen a card that requires two sets of students\n");
        String answer;
        String[] parts;
        String color = null;
        int value = 0;
        int movedStudents = 0;
        boolean validInput = false;
        System.out.println("These are the students on the card: \n");
        EnumMap<Colors, Integer> students1 = new EnumMap<>(Colors.class), students2 = new EnumMap<>(Colors.class);
        //There's gonna be two cases here
        StrippedBoard myBoard = null;
        try {
            myBoard = client.getLocalModel().getBoardOf(client.getNickname());
        } catch (LocalModelNotLoadedException e) {
            System.out.println(StringNames.LOCAL_MODEL_ERROR);
        }
        if (card.getDescription().equals("Swap 3 of the students on this card with 3 from your Entrance!")) {
            System.out.println(card.getDescription());
            //Students input code
            //Students from entrance
            do {
                while (true) {
                    if (myBoard != null) {
                        printEntrance(myBoard);
                    }
                    printStudentsOnCard(card);
                    System.out.println("Type the students you want to move from your entrance as \"color, number\"");
                    answer = in.nextLine();
                    parts = answer.split(",|, | ,");
                    color = parts[0];
                    color = color.replaceAll("[^a-zA Z0-9]", "");
                    try {
                        parts[1] = parts[1].trim();
                        try {
                            value = Integer.parseInt(parts[1]);
                            color = color.toUpperCase(Locale.ROOT);
                            if (value <= 3)
                                break;
                            else
                                System.out.println("You can't move more than 3 students in a single turn! Try again.");
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Something went wrong with your input, try again!");
                        //in.nextLine();
                    }
                }
                if (isValidColor(color)) {
                    if (myBoard != null) {
                        if (value <= myBoard.getEntrance().get(stringToColor(color))) {
                            students1 = initializeMove(students1);
                            students1.put(stringToColor(color), students1.get(stringToColor(color)) + value);
                            movedStudents += value;
                            if (movedStudents < 3) {
                                System.out.println("You still have to pick " + (3 - movedStudents) + " students to move!");
                            }
                            //Since a player can only move 3 students in a turn there needs to be a check here too
                        } else
                            System.out.println("You don't have enough students of that color! Try again.\n");
                    }
                } else
                    System.out.println("There is no such color as " + color + "! Try again. \n");
            } while (movedStudents < 3);

            //**************************** Students from entrance taken **************************************
            //Resetting the variable for later use
            movedStudents = 0;
            printStudentsOnCard(card);
            //Students from card
            do {
                while (true) {
                    System.out.println("Type the students you want to take from the card as \"color, number\"");
                    answer = in.nextLine();
                    // System.out.println("answer "+ answer);
                    parts = answer.split(",|, | ,");
                    color = parts[0];
                    color = color.replaceAll("[^a-zA Z0-9]", "");
                    try {
                        parts[1] = parts[1].trim();
                        try {
                            value = Integer.parseInt(parts[1]);
                            color = color.toUpperCase(Locale.ROOT);
                            if (value <= 3)
                                break;
                            else
                                System.out.println("You can't move more than 3 students in a single turn! Try again.");
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Something went wrong with your input, try again!");
                    }
                }
                if (isValidColor(color)) {
                    if (value <= card.getStudents().get(stringToColor(color))) {
                        students2 = initializeMove(students2);
                        students2.put(stringToColor(color), students2.get(stringToColor(color)) + value);
                        movedStudents += value;
                        if (movedStudents < 3) {
                            System.out.println("You still have to pick " + (3 - movedStudents) + " students to move!");
                        }
                        //Since a player can only move 3 students in a turn there needs to be a check here too
                    } else
                        System.out.println("You don't have enough students of that color! Try again.\n");
                } else
                    System.out.println("There is no such color as " + color + "! Try again. \n");
            } while (movedStudents < 3);

            //**************************** Students from card taken **************************************
            validInput = true;
        }

        //**************************** OTHER CARD **************************************
        else {   //First a check to make sure there are at least 2 students in the dining room
            int diningStudents = 0;

            if (myBoard != null) {
                for (Colors c : myBoard.getDining().keySet()) {
                    diningStudents += myBoard.getDining().get(c);
                }
            }
            if (diningStudents >= 2) {
                do {
                    while ( client.isInGame()) {
                        printEntrance(myBoard);
                        printDining(myBoard);
                        System.out.println("Type the students you want to move from your entrance as \"color, number\"");
                        answer = in.nextLine();
                        parts = answer.split(",|, | ,");
                        color = parts[0];
                        color = color.replaceAll("[^a-zA Z0-9]", "");
                        try {
                            parts[1] = parts[1].trim();
                            try {
                                value = Integer.parseInt(parts[1]);
                                color = color.toUpperCase(Locale.ROOT);
                                if (value <= 2)
                                    break;
                                else
                                    System.out.println("You can't move more than 2 students with this card! Try again.");
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                System.out.println("Something went wrong with your input, try again!");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                        }
                    }
                    if (isValidColor(color)) {
                        if (value <= myBoard.getEntrance().get(stringToColor(color))) {
                            students1 = initializeMove(students1);
                            students1.put(stringToColor(color), students1.get(stringToColor(color)) + value);
                            movedStudents += value;
                            if (movedStudents < 2) {
                                System.out.println("You still have to pick " + (2 - movedStudents) + " students to move!");
                            }
                            //Since a player can only move 2 students in a turn there needs to be a check here too
                        } else
                            System.out.println("You don't have enough students of that color! Try again.\n");
                    } else
                        System.out.println("There is no such color as " + color + "! Try again. \n");
                } while (movedStudents < 2);

                //**************************** Students from entrance taken **************************************
                movedStudents = 0;
                do {
                    while (client.isInGame()) {
                        printDining(myBoard);
                        System.out.println("Type the students you want to move from your dining room as \"color, number\"");
                        answer = in.nextLine();
                        parts = answer.split(",|, | ,");
                        color = parts[0];
                        color = color.replaceAll("[^a-zA Z0-9]", "");
                        try {
                            parts[1] = parts[1].trim();
                            try {
                                value = Integer.parseInt(parts[1]);
                                color = color.toUpperCase(Locale.ROOT);
                                if (value <= 2)
                                    break;
                                else
                                    System.out.println("You can't move more than 2 students with this card! Try again.");
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                System.out.println("Something went wrong with your input, try again!");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                        }
                    }
                    if (isValidColor(color)) {
                        if (value <= myBoard.getDining().get(stringToColor(color))) {
                            students2 = initializeMove(students2);
                            students2.put(stringToColor(color), students2.get(stringToColor(color)) + value);
                            movedStudents += value;
                            if (movedStudents < 2) {
                                System.out.println("You still have to pick " + (2 - movedStudents) + " students to move!");
                            }
                            //Since a player can only move 2 students in a turn there needs to be a check here too
                        } else
                            System.out.println("You don't have enough students of that color! Try again.\n");
                    } else
                        System.out.println("There is no such color as " + color + "! Try again. \n");
                } while (movedStudents < 2);
                validInput = true;
            } else {
                System.out.println("Whoops! You don't have enough students in your dining room to play this card!");
            }
            //**************************** Students from dining room taken **************************************

        }
            for (Colors c: students1.keySet())
            {
                System.out.println(c + " students 1: "+ students1.get(c));
            }
        for (Colors c: students2.keySet())
        {
            System.out.println(c + " students 2: "+ students2.get(c));
        }
        if (validInput) {
            playCharacterCardCOrder = new PlayCharacterCardC(client.getNickname(), id, students1, students2);
            try {
                client.performGameAction(playCharacterCardCOrder);
            } catch (UserNotInRoomException e) {
                System.out.println(StringNames.USER_NOT_IN_ROOM);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
            } catch (FullDiningException e) {
                System.out.println(StringNames.FULL_DINING);
            } catch (AssistantCardNotFoundException e) {
                System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
            } catch (NegativeValueException e) {
                System.out.println(StringNames.NEGATIVE_VALUE);
            } catch (IncorrectStateException e) {
                System.out.println(StringNames.INCORRECT_STATE);
            } catch (MotherNatureLostException e) {
                System.out.println(StringNames.MOTHER_NATURE_LOST);
            } catch (ProfessorNotFoundException e) {
                System.out.println(StringNames.PROFESSOR_NOT_FOUND);
            } catch (IncorrectPlayerException e) {
                System.out.println(StringNames.INCORRECT_PLAYER);
            } catch (RemoteException e) {
                System.out.println(StringNames.REMOTE);
            } catch (IncorrectArgumentException e) {
                System.out.println(StringNames.INCORRECT_ARGUMENT);
            } catch (CardPlayedInTurnException e) {
                System.out.println(StringNames.CARD_PLAYED_IN_TURN);
            } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
            } catch (UserNotRegisteredException e) {
                System.out.println(StringNames.USER_NOT_REGISTERED);
            }
        }
    }

    /**
     * Method used to call Character card type D. They require a choice from the player, but after that the action is automatic.
     *
     * @param id the ID of the character card.
     */
    public void playCharacterD(int id) {
        //There are THREE cards that ask for colors (the professors one just takes them all. One of them has students on it, so I have to differentiate.
        //And the No Entry card that wants the island number so it ends up here
        System.out.println(client.getLocalModel().getCharacters().get(id).getDescription());
        switch (client.getLocalModel().getCharacters().get(id).getDescription()) {
            case "Choose a student color to ignore when calculating Influence this turn!":
            case "Choose a color: EVERYONE must return 3 students of that color to the bag!": {
                System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
                int choice;
                String input;
                boolean invalidChoice = true;
                do {
                    while (true) {
                        input = in.next();
                        try {
                            choice = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("That's not a number! Try again.\n");
                        }
                    }
                    if (choice < 0 || choice > 4) {
                        System.out.println("Invalid color! Try again.");
                        System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
                    } else
                        invalidChoice = false;
                } while (invalidChoice);

                playCharacterCardDOrder = new PlayCharacterCardD(client.getNickname(), id, choice);
                try {
                    client.performGameAction(playCharacterCardDOrder);
                } catch (UserNotInRoomException e) {
                    System.out.println(StringNames.USER_NOT_IN_ROOM);
                } catch (NotEnoughCoinsException e) {
                    System.out.println(StringNames.NOT_ENOUGH_COINS);
                } catch (FullDiningException e) {
                    System.out.println(StringNames.FULL_DINING);
                } catch (AssistantCardNotFoundException e) {
                    System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
                } catch (NegativeValueException e) {
                    System.out.println(StringNames.NEGATIVE_VALUE);
                } catch (IncorrectStateException e) {
                    System.out.println(StringNames.INCORRECT_STATE);
                } catch (MotherNatureLostException e) {
                    System.out.println(StringNames.MOTHER_NATURE_LOST);
                } catch (ProfessorNotFoundException e) {
                    System.out.println(StringNames.PROFESSOR_NOT_FOUND);
                } catch (IncorrectPlayerException e) {
                    System.out.println(StringNames.INCORRECT_PLAYER);
                } catch (RemoteException e) {
                    System.out.println(StringNames.REMOTE);
                } catch (IncorrectArgumentException e) {
                    System.out.println(StringNames.INCORRECT_ARGUMENT);
                } catch (CardPlayedInTurnException e) {
                    System.out.println(StringNames.CARD_PLAYED_IN_TURN);
                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                    System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                } catch (UserNotRegisteredException e) {
                    System.out.println(StringNames.USER_NOT_REGISTERED);
                }
                break;
            }
            //It's basically the same but I have to show the player the students on the card first
            case "Take one student from this card and put them in the dining room, then draw another from the bag and replace them!": {
                printStudentsOnCard(client.getLocalModel().getCharacters().get(id));
                System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
                int choice;
                String input;
                boolean invalidChoice = true;
                do {
                    while (true) {
                        input = in.next();
                        try {
                            choice = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(StringNames.NUMBER_FORMAT);
                        }
                    }
                    if (choice < 0 || choice > 4) {
                        System.out.println("Invalid color! Try again.");
                        System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
                    } else
                        invalidChoice = false;
                } while (invalidChoice);

                playCharacterCardDOrder = new PlayCharacterCardD(client.getNickname(), id, choice);
                try {
                    client.performGameAction(playCharacterCardDOrder);
                } catch (UserNotInRoomException e) {
                    System.out.println(StringNames.USER_NOT_IN_ROOM);
                } catch (NotEnoughCoinsException e) {
                    System.out.println(StringNames.NOT_ENOUGH_COINS);
                } catch (FullDiningException e) {
                    System.out.println(StringNames.FULL_DINING);
                } catch (AssistantCardNotFoundException e) {
                    System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
                } catch (NegativeValueException e) {
                    System.out.println(StringNames.NEGATIVE_VALUE);
                } catch (IncorrectStateException e) {
                    System.out.println(StringNames.INCORRECT_STATE);
                } catch (MotherNatureLostException e) {
                    System.out.println(StringNames.MOTHER_NATURE_LOST);
                } catch (ProfessorNotFoundException e) {
                    System.out.println(StringNames.PROFESSOR_NOT_FOUND);
                } catch (IncorrectPlayerException e) {
                    System.out.println(StringNames.INCORRECT_PLAYER);
                } catch (RemoteException e) {
                    System.out.println(StringNames.REMOTE);
                } catch (IncorrectArgumentException e) {
                    System.out.println(StringNames.INCORRECT_ARGUMENT);
                } catch (CardPlayedInTurnException e) {
                    System.out.println(StringNames.CARD_PLAYED_IN_TURN);
                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                    System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                } catch (UserNotRegisteredException e) {
                    System.out.println(StringNames.USER_NOT_REGISTERED);
                }

                break;
            }
            case "Calculate the influence on any Island and reap the rewards!": {
                printExpertIslands();
                System.out.println("Choose an island to resolve and reap the rewards!");
                String input;
                int island;
                while (true) {
                    input = in.next();
                    try {
                        island = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println(StringNames.NUMBER_FORMAT);
                    }
                }
                int count=0;
                for (StrippedIsland trueIsland: client.getLocalModel().getIslands())
                {
                    if (!trueIsland.getName().equals("EMPTY"))
                        count++;
                }

                island--;
                while (island < 0 || island > count) {
                    System.out.println("That number is not right! Try again.\n");
                    while (true) {
                        input = in.next();
                        try {
                            island = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(StringNames.NUMBER_FORMAT);
                        }
                    }
                }

                int counter=0;
                while(counter<client.getLocalModel().getIslands().size()&&counter<island)
                {
                    if (client.getLocalModel().getIslands().get(counter).getName().equals("EMPTY"))
                    {
                        counter++;
                    }
                    counter++;
                }
                System.out.println("Island chosen: "+ counter);
                playCharacterCardDOrder = new PlayCharacterCardD(client.getNickname(), id, counter);
                try {
                    client.performGameAction(playCharacterCardDOrder);
                } catch (UserNotInRoomException e) {
                    System.out.println(StringNames.USER_NOT_IN_ROOM);
                } catch (NotEnoughCoinsException e) {
                    System.out.println(StringNames.NOT_ENOUGH_COINS);
                } catch (FullDiningException e) {
                    System.out.println(StringNames.FULL_DINING);
                } catch (AssistantCardNotFoundException e) {
                    System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
                } catch (NegativeValueException e) {
                    System.out.println(StringNames.NEGATIVE_VALUE);
                } catch (IncorrectStateException e) {
                    System.out.println(StringNames.INCORRECT_STATE);
                } catch (MotherNatureLostException e) {
                    System.out.println(StringNames.MOTHER_NATURE_LOST);
                } catch (ProfessorNotFoundException e) {
                    System.out.println(StringNames.PROFESSOR_NOT_FOUND);
                } catch (IncorrectPlayerException e) {
                    System.out.println(StringNames.INCORRECT_PLAYER);
                } catch (RemoteException e) {
                    System.out.println(StringNames.REMOTE);
                } catch (IncorrectArgumentException e) {
                    System.out.println(StringNames.INCORRECT_ARGUMENT);
                } catch (CardPlayedInTurnException e) {
                    System.out.println(StringNames.CARD_PLAYED_IN_TURN);
                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                    System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                } catch (UserNotRegisteredException e) {
                    System.out.println(StringNames.USER_NOT_REGISTERED);
                }
                break;
            }
            default: {
                //This is the entry Tile one so we have to do some more stuff
                printExpertIslands();
                System.out.println("Choose an island to put a No Entry Tile on!");
                String input;
                int choice;
                while (true) {
                    input = in.next();
                    try {
                        choice = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println(StringNames.NUMBER_FORMAT);
                    }
                }
                choice--;
                int count=0;
                for (StrippedIsland trueIsland: client.getLocalModel().getIslands())
                {
                    if (!trueIsland.getName().equals("EMPTY"))
                        count++;
                }

                while (choice < 0 || choice > count) {
                    System.out.println("That number is not right! Try again.\n");
                    while (true) {
                        input = in.next();
                        try {
                            choice = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(StringNames.NUMBER_FORMAT);
                        }
                    }
                }
                int counter=0;
                while(counter<client.getLocalModel().getIslands().size()&&counter<choice)
                {
                    if (client.getLocalModel().getIslands().get(counter).getName().equals("EMPTY"))
                    {
                        counter++;
                    }
                    counter++;
                }
                playCharacterCardDOrder = new PlayCharacterCardD(client.getNickname(), id, counter);
                try {
                    client.performGameAction(playCharacterCardDOrder);
                } catch (UserNotInRoomException e) {
                    System.out.println(StringNames.USER_NOT_IN_ROOM);
                } catch (NotEnoughCoinsException e) {
                    System.out.println(StringNames.NOT_ENOUGH_COINS);
                } catch (FullDiningException e) {
                    System.out.println(StringNames.FULL_DINING);
                } catch (AssistantCardNotFoundException e) {
                    System.out.println(StringNames.ASSISTANT_CARD_NOT_FOUND);
                } catch (NegativeValueException e) {
                    System.out.println(StringNames.NEGATIVE_VALUE);
                } catch (IncorrectStateException e) {
                    System.out.println(StringNames.INCORRECT_STATE);
                } catch (MotherNatureLostException e) {
                    System.out.println(StringNames.MOTHER_NATURE_LOST);
                } catch (ProfessorNotFoundException e) {
                    System.out.println(StringNames.PROFESSOR_NOT_FOUND);
                } catch (IncorrectPlayerException e) {
                    System.out.println(StringNames.INCORRECT_PLAYER);
                } catch (RemoteException e) {
                    System.out.println(StringNames.REMOTE);
                } catch (IncorrectArgumentException e) {
                    System.out.println(StringNames.INCORRECT_ARGUMENT);
                } catch (CardPlayedInTurnException e) {
                    System.out.println(StringNames.CARD_PLAYED_IN_TURN);
                } catch (AssistantCardAlreadyPlayed assistantCardAlreadyPlayed) {
                    System.out.println(StringNames.ASSISTANT_CARD_ALREADY_PLAYED);
                } catch (UserNotRegisteredException e) {
                    System.out.println(StringNames.USER_NOT_REGISTERED);
                }
                break;
            }
        }
    }

    // **********************************   Printer Methods    ***************************************************************

    /**
     * Method used to print available commands during the turn.
     */
    public void printCommandHelp() {
        System.out.println("O-----------------------------------------------------------------------O\n" +
                "|              The commands available are the following:               |\n" +
                "|\"Press 1 to view everyone's boards                                    |\n" +
                "|\"Press 2 to view every player's name                                  |\n" +
                "|\"Press 3 to view all the islands                                      |\n" +
                "|\"Press 4 to view all the clouds                                       |\n" +
                "|\"Press 5 to move students across the islands and the dining room      |\n" +
                "|\"Press 6 to move mother nature. This will end your turn               |\n" +
                "|\"Press 7 to view this message again                                   |\n" +
                "|\"Press 8 to leave the game                                            |\n" +
                "O-----------------------------------------------------------------------O");
    }

    /**
     * Same as above, but with added character cards.
     */
    public void expertPrintCommandHelp() {
        System.out.println("O-----------------------------------------------------------------------O\n" +
                "|              The commands available are the following:               |\n" +
                "|\"Press 1 to view everyone's boards                                    |\n" +
                "|\"Press 2 to view every player's name                                  |\n" +
                "|\"Press 3 to view all the islands                                      |\n" +
                "|\"Press 4 to view all the clouds                                       |\n" +
                "|\"Press 5 to move students across the islands and the dining room      |\n" +
                "|\"Press 6 to move mother nature. This will end your turn               |\n" +
                "|\"Press 7 to see the character cards in the game                       |\n" +
                "|\"Press 8 to play a character card                                     |\n" +
                "|\"Press 9 to view this message again                                   |\n" +
                "|\"Press 10 to leave the game                                           |\n" +
                "O-----------------------------------------------------------------------O");
    }

    /**
     * Method used to print every player board. Your own board is highlighted.
     */
    public void printPlayerBoards() {
        ArrayList<StrippedBoard> boards = client.getLocalModel().getBoards();
        int i=client.getLocalModel().getBoards().size();
        System.out.println("Player boards:\n");
        for (StrippedBoard s : boards) {
            if (s.getOwner().equals(client.getNickname()))
                System.out.println(ansi().fg(YELLOW).a("This is your board!").reset());
            printPlayerBoard(s);
            i--;
            if (i>0)
                System.out.println(ansi().bg(WHITE).fg(BLACK).a("      Next player!      ").reset());
        }
    }

    /**
     * Method that prints a given StrippedBoard.
     *
     * @param board The StrippedBoard to print.
     */
    public void printPlayerBoard(StrippedBoard board) {
        int rows=0;
        System.out.println(("O----------------------O"));
        System.out.println(board.getOwner() + "'s board: ");
        System.out.println("Tower color: " + board.getColorsTowers());
        if (board.getColorsTowers().toString().equals("BLACK"))
            while(rows< 8) {
                if (rows%4==0) {
                    if ((rows)< board.getNumberOfTowers())
                        System.out.print(ansi().bg(WHITE).fg(BLACK).a("|* "));
                    else
                        System.out.print(ansi().bg(WHITE).fg(BLACK).a("| "));
                    rows++;
                }
                else if (rows==3||rows==7) {
                    if ((rows)< board.getNumberOfTowers()) {
                        System.out.print(ansi().bg(WHITE).fg(BLACK).a(" *|").reset());
                        System.out.println();
                    }
                    else {
                        System.out.print(ansi().bg(WHITE).fg(BLACK).a("  |").reset());
                        System.out.println();
                    }
                    rows++;
                }
                else {
                    if ((rows+1)< board.getNumberOfTowers())
                        System.out.print(ansi().bg(WHITE).fg(BLACK).a(" * "));
                    else
                        System.out.print(ansi().bg(WHITE).fg(BLACK).a("   "));
                    rows++;
                }
            }

        else
            while(rows< 8) {
                if (rows%4==0) {
                    if ((rows)< board.getNumberOfTowers())
                        System.out.print(ansi().bg(BLACK).fg(WHITE).a("|* "));
                    else
                        System.out.print(ansi().bg(BLACK).fg(WHITE).a("| "));
                    rows++;
                }
                else if (rows==3||rows==7) {
                    if ((rows)< board.getNumberOfTowers()) {
                        System.out.print(ansi().bg(BLACK).fg(WHITE).a(" *|").reset());
                        System.out.println();
                    }
                    else {
                        System.out.print(ansi().bg(BLACK).fg(WHITE).a("  |").reset());
                        System.out.println();
                    }
                    rows++;
                }
                else {
                    if ((rows+1)< board.getNumberOfTowers())
                        System.out.print(ansi().bg(BLACK).fg(WHITE).a(" * "));
                    else
                        System.out.print(ansi().bg(BLACK).fg(WHITE).a("   "));
                    rows++;
                }
            }

        System.out.println("Coins: " + board.getCoins());
        System.out.println("\nDining room configuration: ");
        printDining(board);
        int len;
        int maxlen="YELLOW".length()+3;
        int oldlen;
        int counter=0;
        Color color;
        String coloration;
        boolean isTheFirst=true;
        for (Colors c : board.getDining().keySet()) {
            if (board.getDining().get(c)!=0) {
                color = colorsToColor(c);
                coloration = c.toString();
                len=coloration.length()+3;
                if (isTheFirst)
                {
                    System.out.print("0");
                    while (counter<maxlen) {
                        System.out.print("-");
                        counter++;
                    }
                    System.out.print("0");
                    isTheFirst=false;}
                System.out.println();
                System.out.print("|"+ansi().fg(color).a(coloration).reset() + " | " + board.getDining().get(c));
                oldlen=maxlen-len;
                while(oldlen>0)
                {
                    System.out.print(" ");
                    oldlen--;
                }
                System.out.println("|");
                counter=0;
                System.out.print("0");
                while (counter<maxlen) {
                    System.out.print("-");
                    counter++;
                }
                System.out.print("0");
            }
        }
        System.out.println("O----------------------O");
        printEntrance(board);
        counter=0;
        isTheFirst=true;
        for (Colors c : board.getDining().keySet()) {
            if (board.getDining().get(c)!=0) {
                color = colorsToColor(c);
                coloration = c.toString();
                len=coloration.length()+3;
                if (isTheFirst)
                {
                    System.out.print("0");
                    while (counter<maxlen) {
                        System.out.print("-");
                        counter++;
                    }
                    System.out.print("0");
                    isTheFirst=false;}
                System.out.println();
                System.out.print("|"+ansi().fg(color).a(coloration).reset() + " | " + board.getDining().get(c));
                oldlen=maxlen-len;
                while(oldlen>0)
                {
                    System.out.print(" ");
                    oldlen--;
                }
                System.out.println("|");
                counter=0;
                System.out.print("0");
                while (counter<maxlen) {
                    System.out.print("-");
                    counter++;
                }
                System.out.print("0");
            }
        }
        System.out.println("O----------------------O");
        System.out.println("\nNumber of towers: " + board.getNumberOfTowers());
        System.out.println("\nProfessors table: ");
        for (Colors c : board.getProfessorsTable()) {
            System.out.println(c + "\n");
        }
        System.out.println("O----------------------O");
    }

    /**
     * Method that prints all the player names in the game.
     */
    public void printPlayerNames() {
        for (StrippedBoard board : client.getLocalModel().getBoards()) {
            System.out.println(board.getOwner() + "\n");
        }
    }

    /**
     * Method that prints every island in standard mode.
     */
    public void printIslands() {
        ArrayList<StrippedIsland> islands = client.getLocalModel().getIslands();
        int i = 0, motherNature = 0;
        for (StrippedIsland island : islands) {
            if (!island.getName().equals("EMPTY")) {
                System.out.println("Island number "+ (i+1));
                printIsland(island, false);
                i++;
            }
            if (island.hasMotherNature()) {
                motherNature = i;
            }

        }

        System.out.println("Mother Nature is on isle number " + motherNature + "!");
    }

    /**
     * Method that prints every island in expert mode.
     */
    public void printExpertIslands() {
        ArrayList<StrippedIsland> islands = client.getLocalModel().getIslands();
        int i = 0, motherNature = 0;
        for (StrippedIsland island : islands) {
            if (!island.getName().equals("EMPTY")) {
                System.out.println("Island number "+ (i+1));
                printExpertIsland(island, false);
                i++;
            }
            if (island.hasMotherNature()) {
                motherNature = i;
            }

        }
        System.out.println("Mother Nature is on isle number " + (motherNature) + "!");
    }

    /**
     * Method that prints the dining room of a given board.
     *
     * @param board the board to print.
     */
    public void printDining(StrippedBoard board) {
        Integer i;
        int printColumns;
        Ansi.Color color;
        System.out.println("┌----------------------┐");
        for (Colors c : board.getDining().keySet()) {
            i = board.getDining().get(c);
            color = colorsToColor(c);
            printColumns = 0;
            while (i > 0) {
                if (printColumns == 0) {
                    System.out.print("|");
                }
                System.out.print(ansi().fg(color).a("*\t").reset());
                printColumns++;
                i--;
            }
            System.out.println();
        }
        System.out.println("┕----------------------┚");
    }

    /**
     * Method that prints the entrance of a given board.
     *
     * @param board the board to print.
     */
    public void printEntrance(StrippedBoard board) {
        Integer i;
        int numofstudents = 0;
        int rows = 0;
        int maxstudents;
        if (client.getLocalModel().getBoards().size()==3)
            maxstudents=9;
        else
            maxstudents=7;
        Ansi.Color color = null;
        if (maxstudents==7) {
            System.out.println("\nEntrance configuration: ");
            System.out.println("O-----O");
            for (Colors c : board.getEntrance().keySet())
                numofstudents += board.getEntrance().get(c);
            for (Colors c : board.getEntrance().keySet()) {
                i = board.getEntrance().get(c);
                color = colorsToColor(c);

                while (rows < 7 && i > 0) {
                    if (rows % 3 == 0 && rows != 6) {
                        if (rows < numofstudents) {
                            System.out.print("|");
                            System.out.print(ansi().fg(color).a("*").reset());
                        } else
                            System.out.print(ansi().a("|  ").reset());

                    } else if (rows % 3 == 1) {
                        if (rows < numofstudents) {
                            System.out.print(ansi().fg(color).a(" *").reset());
                        } else
                            System.out.print(ansi().fg(color).a("  ").reset());
                    } else if (rows % 3 == 2) {
                        if (rows < numofstudents) {
                            System.out.print(ansi().fg(color).a(" *").reset());
                            System.out.println("|");
                        } else
                            System.out.println(ansi().a("   |").reset());

                    } else {
                        if (rows < numofstudents) {
                            System.out.print("|");
                            System.out.print(ansi().fg(color).a("  *  ").reset());
                            System.out.print("|");
                        } else {
                            System.out.print("|");
                            System.out.print(ansi().fg(color).a("     ").reset());
                            System.out.print("|");
                        }

                    }
                    rows++;
                    i--;

                }


            }
            while (rows < 7) {
                if (rows % 3 == 0 && rows != 6)
                    System.out.print(ansi().a("|  ").reset());
                else if (rows % 3 == 1)
                    System.out.print(ansi().fg(color).a("  ").reset());
                else if (rows % 3 == 2)
                    System.out.println(ansi().a("  |").reset());
                else {
                    System.out.print("|");
                    System.out.print(ansi().fg(color).a("     ").reset());
                    System.out.print("|");
                }

                rows++;
            }
        }



        else {
            System.out.println("\nEntrance configuration: ");
            System.out.println("O-----O");
            for (Colors c : board.getEntrance().keySet())
                numofstudents += board.getEntrance().get(c);
            for (Colors c : board.getEntrance().keySet()) {
                i = board.getEntrance().get(c);
                color = colorsToColor(c);

                while (rows < maxstudents && i > 0) {
                    if (rows % 3 == 0) {
                        if (rows < numofstudents) {
                            System.out.print("|");
                            System.out.print(ansi().fg(color).a("*").reset());
                        } else
                            System.out.print(ansi().a("|  ").reset());

                    } else if (rows % 3 == 1) {
                        if (rows < numofstudents) {
                            System.out.print(ansi().fg(color).a(" *").reset());
                        } else
                            System.out.print(ansi().fg(color).a("  ").reset());
                    } else {
                        if (rows < numofstudents) {
                            System.out.print(ansi().fg(color).a(" *").reset());
                            if (rows != 8)
                                System.out.println("|");
                            else
                                System.out.print("|");
                        } else
                            System.out.print(ansi().a("   |").reset());


                    }
                    rows++;
                    i--;

                }


            }
            while (rows < maxstudents) {
                if (rows % 3 == 0)
                    System.out.print(ansi().a("|  ").reset());
                else if (rows % 3 == 1)
                    System.out.print(ansi().fg(color).a("  ").reset());
                else
                    System.out.print(ansi().a("  |").reset());

                rows++;
            }
        }

        System.out.println();
        System.out.println("O-----O");
}

    /**
     * Prints a single standard island (No "No entry" tiles).
     *
     * @param island the island to print.
     */
    public void printIsland(StrippedIsland island, boolean print) {
        int i;
        Color color;
        int rows = 0;
        if (print)
            System.out.println("Island: " + island.getName());
        System.out.println("Students on the island: ");
        if (island.hasMotherNature())
            System.out.println(ansi().fg(CYAN).a("* Mother Nature's here!  *").reset());
        System.out.println(" ____________________");
        System.out.println("/                    \\");
        for (Colors c : island.getStudents().keySet()) {
            i = island.getStudents().get(c);
            color = colorsToColor(c);
            while (i > 0) {
                if ((rows % 3) < 2) {
                    if (rows != 6) {
                        System.out.print(ansi().fg(color).a("\t* ").reset());
                        rows++;
                    } else
                        System.out.print(ansi().fg(color).a(" *\n").reset());
                } else {
                    System.out.print(ansi().fg(color).a("*\n").reset());
                    rows++;


                }
                i--;
            }
        }
        int k=0;
        while(k<island.getNumOfTowers())
        {
            if(k%4<3) {
                if ((island.getTowersColor().toString().equals("BLACK")))
                    System.out.print(ansi().fg(WHITE).bg(BLACK).a("||").reset()+" ");
                else
                    System.out.print(ansi().fg(BLACK).bg(WHITE).a("||").reset()+" ");
            }
            else
            {
                if ((island.getTowersColor().toString().equals("BLACK"))) {
                    System.out.print(ansi().fg(WHITE).bg(BLACK).a("||").reset()+" ");
                    System.out.println();
                }
                else {
                    System.out.print(ansi().fg(BLACK).bg(WHITE).a("||").reset()+" ");
                    System.out.println();
                }
            }

            k++;
        }
        System.out.println();
        System.out.println("\\                    /");
        System.out.println(" ____________________");
        int len;
        int maxlen="YELLOW".length()+3;
        int oldlen;
        int counter=0;
        String coloration;
        boolean isTheFirst=true;
        for (Colors c : island.getStudents().keySet()) {
            if (island.getStudents().get(c)!=0) {
                color = colorsToColor(c);
                coloration = c.toString();
                len=coloration.length()+3;
                if (isTheFirst)
                {
                System.out.print("0");
                    while (counter<maxlen) {
                        System.out.print("-");
                        counter++;
                    }
                    System.out.print("0");
                isTheFirst=false;}
                    System.out.println();
                    System.out.print("|"+ansi().fg(color).a(coloration).reset() + " | " + island.getStudents().get(c));
                    oldlen=maxlen-len;
                    while(oldlen>0)
                    {
                        System.out.print(" ");
                        oldlen--;
                    }
                    System.out.println("|");
                    counter=0;
                System.out.print("0");
                    while (counter<maxlen) {
                        System.out.print("-");
                        counter++;
                    }
                System.out.print("0");
            }
        }
System.out.println();
        if (island.getNumOfTowers() == 0)
            System.out.println("There are no towers yet on this island!");
        else
            System.out.println("Towers: " + island.getNumOfTowers() + " " + island.getTowersColor() + " towers \n");

    }

    /**
     * Prints a single expert island (with eventual "No entry" tiles).
     *
     * @param island the island to print.
     */
    public void printExpertIsland(StrippedIsland island, boolean print) {
        int i;
        Color color;
        int rows = 0;
        if (print)
            System.out.println("Island: " + island.getName());
        System.out.println("Students on the island: ");
        if (island.hasMotherNature())
            System.out.println(ansi().fg(CYAN).a("* Mother Nature's here!  *").reset());
        if (island.hasNoEnterTile())
            System.out.println(ansi().fg(RED).a("!!! There's a No Entry Tile on this island !!!").reset());
        System.out.println(" ____________________");
        System.out.println("/                    \\");
        for (Colors c : island.getStudents().keySet()) {
            i = island.getStudents().get(c);
            color = colorsToColor(c);
            while (i > 0) {
                if ((rows % 3) < 2) {
                    if (rows != 6) {
                        System.out.print(ansi().fg(color).a("\t* ").reset());
                        rows++;
                    } else
                        System.out.print(ansi().fg(color).a("  *\n").reset());
                } else {
                    System.out.print(ansi().fg(color).a("*\n").reset());
                    rows++;
                }
                i--;
            }

        }
        int k=0;
        while(k<island.getNumOfTowers())
        {
            if(k%4<3) {
                if ((island.getTowersColor().toString().equals("BLACK")))
                    System.out.print(ansi().fg(WHITE).bg(BLACK).a("||").reset()+" ");
                else
                    System.out.print(ansi().fg(BLACK).bg(WHITE).a("||").reset()+" ");
            }
            else
            {
                if ((island.getTowersColor().toString().equals("BLACK"))) {
                    System.out.print(ansi().fg(WHITE).bg(BLACK).a("||").reset()+" ");
                    System.out.println();
                }
                else {
                    System.out.print(ansi().fg(BLACK).bg(WHITE).a("||").reset()+" ");
                    System.out.println();
                }
            }

            k++;
        }
        System.out.println();
        System.out.println("\\                    /");
        System.out.println(" ____________________");
        int len;
        int maxlen="YELLOW".length()+3;
        int oldlen;
        int counter=0;
        String coloration;
        boolean isTheFirst=true;
        for (Colors c : island.getStudents().keySet()) {
            if (island.getStudents().get(c)!=0) {
                color = colorsToColor(c);
                coloration = c.toString();
                len=coloration.length()+3;
                if (isTheFirst)
                {
                    System.out.print("0");
                    while (counter<maxlen) {
                        System.out.print("-");
                        counter++;
                    }
                    System.out.print("0");
                    isTheFirst=false;}
                System.out.println();
                System.out.print("|"+ansi().fg(color).a(coloration).reset() + " | " + island.getStudents().get(c));
                oldlen=maxlen-len;
                while(oldlen>0)
                {
                    System.out.print(" ");
                    oldlen--;
                }
                System.out.println("|");
                counter=0;
                System.out.print("0");
                while (counter<maxlen) {
                    System.out.print("-");
                    counter++;
                }
                System.out.print("0");
            }
        }
        System.out.println();

        if (island.getNumOfTowers() == 0)
            System.out.println("There are no towers yet on this island!\n");
        else
            System.out.println("Towers: " + island.getNumOfTowers() + " " + island.getTowersColor() + " towers \n");


        if (island.hasMotherNature())
            System.out.println("Mother Nature is on this island!");

    }

    /**
     * Prints every cloud in the game.
     */
    public void printClouds() {
        EnumMap<Colors, Integer> students;
        int i;
        Color color;
        int rows;
        for (StrippedCloud cloud : client.getLocalModel().getClouds()) {
            rows = 0;
            students = cloud.getStudents();
            if (!students.isEmpty()) {
                System.out.println(("Cloud name:" + cloud.getName()));
                System.out.println(" 00000000000000000000");
                System.out.println("0                    0");
                for (Colors c : cloud.getStudents().keySet()) {
                    //System.out.println(c + " students: " + board.getEntrance().get(c));
                    i = cloud.getStudents().get(c);
                    // System.out.println("I secondo: "+i);
                    color = colorsToColor(c);
                    while (i > 0) {
                        if ((rows % 3) < 2) {
                            if (rows != 6) {
                                System.out.print(ansi().fg(color).a("\t* ").reset());
                                rows++;
                            } else
                                System.out.print(ansi().fg(color).a("  *\n").reset());
                        } else {
                            System.out.print(ansi().fg(color).a("*\n").reset());
                            rows++;

                        }
                        i--;
                    }
                }
                System.out.println();
                System.out.println("0                    0");
                System.out.println(" 00000000000000000000");
                System.out.println("Number of students: ");
                for (Colors c : Colors.values()) {
                    System.out.println(c + " " + students.get(c));
                }
            }
        }

    }

    /**
     * Prints every character card in the game.
     */
    public void printCharacterCards() {
        int i = 0;
        ArrayList<StrippedCharacter> temp = client.getLocalModel().getCharacters();
        for (StrippedCharacter c : temp) {
            System.out.println();
            int w=c.getDescription().length()+23;
            System.out.print("~");
            while(w>0)
            {
                System.out.print("-");
                w--;
            }
            System.out.println("~");
            System.out.print("Character " + (i + 1));
            w=11;
            while(w<(c.getDescription().length()+23))
            {
                System.out.print(" ");
                w++;
            }
            System.out.println(" |");
            System.out.println("Price: " + c.getPrice() + ", description:  " + c.getDescription()+ "|");
            w=c.getDescription().length()+23;
           System.out.print("~");
           while(w>0)
           {
               System.out.print("-");
               w--;
           }
            System.out.print("~");
            i++;
        }
        System.out.println();
        try {
            System.out.println("You currently have " + client.getLocalModel().getBoardOf(client.getNickname()).getCoins() + " coins!");
        } catch (LocalModelNotLoadedException e) {
            System.out.println(StringNames.LOCAL_MODEL_ERROR);
        }
    }

    /**
     * Prints every assistant card in the local personal deck.
     */
    public void printAssistantCards() {
        AssistantCardDeck myDeck = null;
        try {
            myDeck = client.getLocalModel().getBoardOf(client.getNickname()).getDeck();
        } catch (LocalModelNotLoadedException e) {
            System.out.println(StringNames.LOCAL_MODEL_ERROR);
        }
        int i=1;
        System.out.println("0--------------------------------------0");
        if (myDeck != null) {
            for (AssistantCard a : myDeck.getDeck()) {
                System.out.print("|");
                if (a.getImageName().equals("10"))
                System.out.print("Card number " + i + "| Value " + a.getImageName() + "| Moves: " + a.getMove());
                else
                    System.out.print("Card number " + i + " | Value " + a.getImageName() + "| Moves: " + a.getMove());
                if(i!=10&&!a.getImageName().equals("10"))
                {
                    System.out.println("     |");
                }
                else if (a.getImageName().equals("10")&&i==10)
                {
                    System.out.println("    |");
                }
                else
                    System.out.println("     |");
                i++;
            }
            System.out.println("0--------------------------------------0");
        }
    }

    /**
     * Prints the students present on a card.
     *
     * @param card the card to print the students of.
     */
    public void printStudentsOnCard(StrippedCharacter card) {
        Integer i;
        int po=0;
        int rows = 0;
        Ansi.Color color;
        System.out.println("*-----*");
        for (Colors c: card.getStudents().keySet())
        {
            i=card.getStudents().get(c);
            po+=i;
        }
        for (Colors c : card.getStudents().keySet()) {
            i = card.getStudents().get(c);
            color = colorsToColor(c);
            while (rows<6&&i>0) {
                if (rows%2==0) {
                    if (rows<po) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("* ").reset());
                    }
                    else
                        System.out.print(ansi().a("|  ").reset());

                } else if (rows%2==1) {
                    if (rows<po) {
                        System.out.print(ansi().fg(color).a("  *").reset());
                        System.out.println("|");
                    }
                    else
                        System.out.println(ansi().a("   |").reset());

                } else {
                    if (rows<po) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("*").reset());
                        System.out.print("|");
                    }
                }
                rows++;
                i--;

            }


        }
        while (rows<6)
        {
            if (rows%2==0)
                System.out.print(ansi().a("|  ").reset());
            else if (rows%2==1)
                System.out.println(ansi().a("   |").reset());
            rows++;
        }

        System.out.println("*-----*");
    }

    public void printDining (EnumMap<Colors, Integer> dining)
    {
        Integer i;
        int printColumns;
        Ansi.Color color;
        System.out.println("O----------------------O");
        for (Colors c : dining.keySet()) {
            i = dining.get(c);
            //  System.out.println("I: "+i);
            color = colorsToColor(c);
            printColumns = 0;
            while (i > 0) {
                if (printColumns == 0) {
                    System.out.print("|");
                    System.out.print(ansi().fg(color).a("*\t").reset());
                    printColumns++;
                } else {
                    System.out.print(ansi().fg(color).a("*\t").reset());
                    printColumns++;
                }
                i--;

            }
            System.out.println();
        }
        System.out.println("O----------------------O");
    }

    // **********************************   Utility Methods    ***************************************************************

    /**
     * Checks if the string input corresponds to a valid color
     *
     * @param input the string to check
     * @return whether it corresponds (true) or it doesn't(false)
     */
    public boolean isValidColor(String input) {
        input = input.toUpperCase(Locale.ROOT);
        for (Colors c : Colors.values()) {
            if (c.name().equals(input))
                return true;
        }
        return false;
    }

    /**
     * Takes a string and converts it to a Colors Enum value.
     *
     * @param input the string to convert
     * @return the corresponding Colors Enum value.
     */
    public Colors stringToColor(String input) {
        input = input.toLowerCase(Locale.ROOT);
        switch (input) {
            case "red":
                return Colors.RED;
            case "blue":
                return Colors.BLUE;
            case "green":
                return Colors.GREEN;
            case "pink":
                return Colors.PINK;
            case "yellow":
            default:
                return Colors.YELLOW;
        }
    }

    /**
     * Colors (our enum) to Color (Standard Enum) converter for color display purposes.
     *
     * @param color the color to convert
     * @return its equivalent color in ANSI notation.
     */
    public Color colorsToColor(Colors color) {
        Color colorToReturn = null;
        switch (color) {
            case RED:
                colorToReturn = Ansi.Color.RED;
                break;
            case YELLOW:
                colorToReturn = Ansi.Color.YELLOW;
                break;
            case BLUE:
                colorToReturn = Ansi.Color.CYAN;
                break;
            case GREEN:
                colorToReturn = Ansi.Color.GREEN;
                break;
            case PINK:
                colorToReturn = Ansi.Color.MAGENTA;
                break;
        }
        return colorToReturn;
    }
    public void printEntrance(EnumMap<Colors, Integer> entrance) {
        Integer i;
        int numofstudents = 0;
        int rows = 0;
        Ansi.Color color = null;
        System.out.println("\nEntrance configuration: ");
        System.out.println("O-----O");
        for (Colors c : entrance.keySet())
            numofstudents += entrance.get(c);
        for (Colors c : entrance.keySet()) {
            i = entrance.get(c);
            color = colorsToColor(c);

            while (rows < 7 && i > 0) {
                if (rows % 3 == 0&&rows!=6) {
                    if (rows < numofstudents) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("*").reset());
                    } else
                        System.out.print(ansi().a("|  ").reset());

                } else if (rows % 3 == 1) {
                    if (rows < numofstudents) {
                        System.out.print(ansi().fg(color).a(" *").reset());
                    } else
                        System.out.print(ansi().fg(color).a("  ").reset());
                } else if (rows % 3 == 2) {
                    if (rows < numofstudents) {
                        System.out.print(ansi().fg(color).a(" *").reset());
                        System.out.println("|");
                    } else
                        System.out.println(ansi().a("   |").reset());

                } else {
                    if (rows < numofstudents) {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("  *  ").reset());
                        System.out.print("|");
                    }
                    else {
                        System.out.print("|");
                        System.out.print(ansi().fg(color).a("     ").reset());
                        System.out.print("|");
                    }

                }
                rows++;
                i--;

            }


        }
        while (rows < 7) {
            if (rows % 3 == 0&&rows!=6)
                System.out.print(ansi().a("|  ").reset());
            else if (rows % 3 == 1)
                System.out.print(ansi().fg(color).a("  ").reset());
            else if (rows%3==2)
                System.out.println(ansi().a("  |").reset());
            else{
                System.out.print("|");
                System.out.print(ansi().fg(color).a("     ").reset());
                System.out.print("|");
            }

            rows++;
        }
        System.out.println();
        System.out.println("O-----O");
    }


    /**
     * Method that converts stripped students EnumMaps built in client in input appropriate for the moveStudents method in Game.
     *
     * @param students       the students to convert
     * @param returnStudents the game format EnumMap the students are to be added to
     * @param destination    the destination of this batch of students (either "dining" or "island"
     * @return returnStudents with the students and their relative destinations added.
     */
    public EnumMap<Colors, ArrayList<String>> strippedToGame(EnumMap<Colors, Integer> students, EnumMap<Colors, ArrayList<String>> returnStudents, String destination) {
        for (Colors c : students.keySet()) {
            while (students.get(c) > 0) {
                returnStudents.get(c).add(destination);
                students.put(c, students.get(c) - 1);
            }
        }

        return returnStudents;
    }

    /**
     * Printer method for arraylists
     *
     * @param messageArray the arraylist to print.
     */
    private synchronized void sendArrayString(ArrayList<String> messageArray) {
        for (String message : messageArray) System.out.println(message);
    }

    /**
     * Method used to initialize students EnumMap.
     *
     * @param move enumMap to initialize
     * @return initialized EnumMap.
     */
    public EnumMap<Colors, Integer> initializeMove(EnumMap<Colors, Integer> move) {
        for (Colors c : Colors.values()) {
            move.put(c, 0);
        }
        return move;
    }

    /**
     * Method that waits for turn.
     *
     */
    public synchronized void waitForTurn(){
        if (!client.isMyTurn()) {
            System.out.print("Waiting.  ");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
            System.out.print("\b\b\b\b\b\b\b\b\b\b\b");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
            System.out.print("Waiting.. ");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
            System.out.print("\b\b\b\b\b\b\b\b\b\b\b");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }

            System.out.print("Waiting... ");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
            System.out.print("\b\b\b\b\b\b\b\b\b\b\b");

            System.out.print("Waiting   ");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(StringNames.INTERRUPTED);
            }
            System.out.print("\b\b\b\b\b\b\b\b\b\b");

        }

    }
    // **********************************   End of Utility Methods    ***************************************************************
}