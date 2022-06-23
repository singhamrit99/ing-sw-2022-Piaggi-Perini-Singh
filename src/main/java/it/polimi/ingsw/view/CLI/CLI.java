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
    boolean endturn=false;
    MoveMotherNature moveMotherNatureOrder;
    MoveStudents moveStudentsOrder;
    PickCloud pickCloudOrder;
    PlayAssistantCard playAssistantCardOrder;
    PlayCharacterCardA playCharacterCardAOrder;
    PlayCharacterCardB playCharacterCardBOrder;
    PlayCharacterCardC playCharacterCardCOrder;
    PlayCharacterCardD playCharacterCardDOrder;
    DrawFromBagCommand drawFromBagOrder;
    HashMap<String, ArrayList<Colors>> professorsTables= new HashMap<>();
    private final Scanner in = new Scanner(System.in);

    public CLI(Client client) {
        this.client = client;
        this.client.setUi(this);
        this.client.view = StringNames.LAUNCHER;
    }

    @Override
    public void reloadRoomsFromGameView() {
        //TODO
        // In gui we use this method to exit from GameView and enter in rooms scenes when
        // the event to leave the game is triggered by another client
    }

    public void Start() throws RemoteException, UserNotInRoomException, NotLeaderRoomException, NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, UserNotRegisteredException, InterruptedException, RoomNotExistsException, LocalModelNotLoadedException {

        AnsiConsole.systemInstall();
        System.out.println("Welcome to...");

        System.out.println(("      ########## #########  ###########     ###     ####    ### ########### ###   ###  ######## \n") +
                "     #+#        #+#    #+#     #+#       #+# #+#   #+#+#   #+#     #+#     #+#   #+# #+#    #+# \n" +
                "    +#+        +#+    +#+     +#+      +#+   +#+  #+#+#+  +#+     +#+      +#+ +#+  +#+         \n" +
                "   +#++#++#   +#++#++##      +#+     +#++#++#++# +#+ +#+ +#+     +#+       +#++#   +#++#++#++   \n" +
                "  +#+        +#+    +#+     +#+     +#+     +#+ +#+  +#+#+#     +#+        +#+           +#+    \n" +
                " #+#        #+#    #+#     #+#     #+#     #+# #+#   #+#+#     #+#        #+#    #+#    #+#     \n" +
                "########## ###    ### ########### ###     ### ###    ####     ###        ###     ########     ");
        System.out.println("Welcome to the lobby!\nWhat's your name?");
        while (true) {
            try {
                nickName = in.nextLine();
                client.registerClient(nickName);
                client.view = StringNames.LOBBY;
                break;
            } catch (UserAlreadyExistsException e) {
                System.out.println("That username is already in the game! Try another.\n");
            }
        }
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
                        requestRoomJoin(); //fatto
                        break;
                    case "create":
                        requestRoomCreation(); //fatto
                        break;
                    case "players":
                        getPlayersInRoom();//fatto
                        break;
                    case "rooms":
                        getRooms();//fatto
                        break;
                    case "info":
                        getLobbyInfo();//fatto
                        break;
                    case "change":
                        setExpertMode();//fatto
                        break;
                    case "leave":
                        leaveRoom();//fatto
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
        while (client.getLocalModel()==null) {

        }
        for (StrippedBoard s: client.getLocalModel().getBoards())
        {
            professorsTables.put(s.getOwner(), s.getProfessorsTable());
        }





        while (client.isInGame()) {
            endturn=false;
            numOfPlayers=client.getLocalModel().getBoards().size();
            if (playedThisTurn == null)
                playedThisTurn = new ArrayList<>();
            //Assistant Card play phase
            while (!client.isMyTurn()) {
                //Wait for the other players to be done with their turn while I still output their moves...
                waitForTurn();
            }

            if (client.isMyTurn() && client.getLocalModel().getFirstPlayer().equals(client.getNickname())) {
                System.out.println("Drawing from bag...");
                drawFromBag();
            }
            if (client.isMyTurn() && client.getLocalModel().getState().equals(State.PLANNINGPHASE))
               while (true)try {
                    playAssistantCard();
                    break;
                }catch (AssistantCardNotFoundException e)
               {
                   System.out.println("Invalid assistant card! Try again.");
               }


            System.out.println("Waiting for everyone to play an assistant card");
            while(numOfPlayers>1)
            {
                //System.out.println(numOfPlayers);
            }

            //Turn phase
            if (client.getExpertMode()) {

                while (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3)&&!endturn) {
                    while (!client.isMyTurn()) {
                        waitForTurn();
                    }
                    if (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3)&&!endturn) {
                        expertPrintCommandHelp();
                        performActionInTurnExpert();
                    }
                    //System.out.println(client.getLocalModel().getState());
                }
                pickCloud();
            } else {

                while (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3)&&!endturn) {
                    while (!client.isMyTurn()) {
                        waitForTurn();
                    }
                    if (!client.getLocalModel().getState().equals(State.ACTIONPHASE_3)&&!endturn) {
                        printCommandHelp();
                        performActionInTurn();
                    }
                    //System.out.println(client.getLocalModel().getState());
                }
                pickCloud();
            }
        }

    }

    @Override
    public void professorChanged() {
        ArrayList<StrippedBoard> tmp = new ArrayList<>(client.getLocalModel().getBoards());
        for (StrippedBoard s: tmp)
        {
            if (professorsTables.get(s.getOwner()).equals(s.getProfessorsTable()))
            {
                    //No change
            }

            else
            {
                System.out.println(s.getOwner()+ "'s professors changed from ");
                System.out.println(professorsTables.get(s.getOwner()));
                System.out.println(" to ");
                System.out.println(s.getProfessorsTable());
                professorsTables.replace(s.getOwner(),s.getProfessorsTable());
            }

        }

        return;
    }

    @Override
    public void startGame() throws RemoteException {
        if (!client.isInGame()) {
            try {
                client.view = StringNames.INGAME;
                client.startGame();
                client.setInGame(true);
            } catch (NotEnoughPlayersException e) {
                System.out.println(StringNames.ALONE_IN_ROOM);
            } catch (UserNotInRoomException e) {
                System.out.println(StringNames.NOT_IN_ROOM);
            } catch (NotLeaderRoomException e) {
                System.out.println(StringNames.NO_LEADER);
            } catch (RoomNotExistsException | UserNotRegisteredException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void currentPlayer(String s) {
        client.view = StringNames.INGAME;
        System.out.println("It's now " + s + "'s turn!");
    }

    @Override
    public void notifyCloud(PropertyChangeEvent e) {

    }

    @Override
    public void deckChange(String assistantCard) {
        String tmp;
        if (!client.getLocalModel().getCurrentPlayer().equals(client.getNickname()))
            System.out.println(client.getLocalModel().getCurrentPlayer() + " has played " + assistantCard);
        tmp = assistantCard.replaceAll("[^\\d.]", "");
        if (playedThisTurn == null)
            playedThisTurn = new ArrayList<>();
        playedThisTurn.add(Integer.parseInt(tmp));
        numOfPlayers--;
    }

    @Override
    public void assistantCardPlayed(PropertyChangeEvent e) {

    }


    @Override
    public void islandChange(PropertyChangeEvent e) {
        if (!client.getLocalModel().getCurrentPlayer().equals(client.getNickname())) {
            StrippedIsland newIsland;
            newIsland = (StrippedIsland) e.getNewValue();
            System.out.println(newIsland.getName() + " changed to");
            printIsland(newIsland);
        }
    }

    @Override
    public void islandMerged(PropertyChangeEvent e) {
        StrippedIsland oldIsland, newIsland;
        oldIsland = (StrippedIsland) e.getOldValue();
        newIsland = (StrippedIsland) e.getNewValue();
        System.out.println("Islands " + oldIsland.getName() + " and " + newIsland.getName() + " merged!\n");
    }

    @Override
    public void islandConquest(PropertyChangeEvent e) {
        StrippedIsland island = (StrippedIsland) e.getNewValue();
        System.out.println(client.getLocalModel().getCurrentPlayer() + " conquered island " + island.getName() + "!\n");

    }

    @Override
    public void diningChange(PropertyChangeEvent e) {
        String player = (String) e.getOldValue();
        if (!player.equals(client.getNickname())) {
            EnumMap<Colors, Integer> newDining;
            newDining = (EnumMap<Colors, Integer>) e.getNewValue();
            System.out.println("O----------------------------------O");
            System.out.println(e.getOldValue() + " modified their dining room! Here's the new configuration...");
            for (Colors c : newDining.keySet()) {
                System.out.println(c + " students: " + newDining.get(c));
            }
            System.out.println("O----------------------------------O");
        }
    }

    @Override
    public void entranceChanged(PropertyChangeEvent e) {
        EnumMap<Colors, Integer> newBoard;
        String player;
        player = (String) e.getOldValue();
        if (!player.equals(client.getNickname())) {
            newBoard = (EnumMap<Colors, Integer>) e.getNewValue();
            System.out.println("O----------------------------------O");
            System.out.println(player + "'s entrance changed to \n");
            System.out.println("to:");
            for (Colors c : newBoard.keySet()) {
                System.out.println(c + " students: " + newBoard.get(c));
            }
            System.out.println("O----------------------------------O");
        }
    }

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

    @Override
    public void gameOver(String s) {

        System.out.println("Game over! Team " + s + "won! Congratulations!\n");
        client.setInGame(false);
        System.out.println("Do you want to play again? Y/N");
        boolean validInput = false;
        boolean result = false;
        String answer;
        while (!validInput) {
            answer = in.nextLine().toLowerCase(Locale.ROOT);
            switch (answer) {
                case "y": {
                    result = true;
                    validInput = true;
                    break;
                }
                case "n": {
                    result = false;
                    validInput = true;
                    break;
                }
                default:
                    System.out.println("Command not recognized\n");
            }
        }

        if (result) {
            System.out.println("Cool\n");
        } else
            System.out.println("Goodbye!\n");
    }

    @Override
    public void coinsChanged(PropertyChangeEvent e) {

    }

    @Override
    public void roomsAvailable(ArrayList<String> rooms) {
        try {
            getRooms();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void roomJoin(ArrayList<String> players) {
        try {
            getPlayersInRoom();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void getPlayersInRoom() throws RemoteException {
        if (clientRoom != null) {
            ArrayList<String> response;
            try {
                response = client.getNicknamesInRoom();
            } catch (RoomNotExistsException | UserNotInRoomException e) {
                throw new RuntimeException(e);
            }
            sendArrayString(response);
        }
        // System.out.println("You're not in a room, so there are no players to show\n");
    }

    private void getLobbyInfo() throws RemoteException {
        if (clientRoom != null) {
            ArrayList<String> result;
            try {
                result = client.requestLobbyInfo(clientRoom);
            } catch (RoomNotExistsException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Lobby name: " + result.get(0));
            System.out.println("Leader: " + result.get(1));
            System.out.println("Expert mode: " + result.get(2));
        } else
            System.out.println("You're not in a room yet\n");
    }

    private void leaveRoom() throws RemoteException {
        try {
            client.leaveRoom();
        } catch (UserNotInRoomException e) {
            System.out.println("You're not in a room yet\n");
        } catch (UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public void getRooms() throws RemoteException {
        System.out.println("Rooms on the server: ");
        ArrayList<String> response = client.getRooms();
        if (response.isEmpty())
            System.out.println("There are no rooms yet\n");
        else
            sendArrayString(response);
    }

    public void setExpertMode() throws RemoteException, UserNotInRoomException, NotLeaderRoomException {
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
                result = false;
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
            System.out.println(StringNames.NOT_IN_ROOM);
        } catch (NotLeaderRoomException e) {
            System.out.println("You're not this lobby's leader, you can't do that!\n");
        } catch (UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void requestRoomCreation() throws RemoteException {
        System.out.println("Insert room name: \n");
        String nameRoom;
        nameRoom = in.nextLine();
        while (client.getRooms().contains(nameRoom)) {
            System.out.println("Ops, there is another room with the same name! Choose another one please. \n");
            nameRoom = in.nextLine();
        }
        try {
            client.createRoom(nameRoom);
        } catch (UserNotRegisteredException | RoomAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        clientRoom = nameRoom;
    }

    public synchronized void requestRoomJoin() throws RemoteException {
        String requestedRoom;
        System.out.println("Select the room: \n");
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
            if (requestedRoom.equals(clientRoom)) {
                System.out.println("You're already in that room!\n");
            } else {
                try {
                    if (clientRoom != null)
                        leaveRoom();
                    client.requestRoomJoin(requestedRoom);
                } catch (RoomNotExistsException | UserNotRegisteredException e) {
                    throw new RuntimeException(e);
                } catch (RoomFullException | RoomInGameException e) {
                    System.out.println(e.getMessage());
                }
                client.view = StringNames.ROOM;
                clientRoom = requestedRoom;
                System.out.println("You entered room " + clientRoom + " successfully \n");
                System.out.println("Players in this room:");
                try {
                    sendArrayString(client.getNicknamesInRoom());

                } catch (RoomNotExistsException | UserNotInRoomException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // **********************************   Gameplay methods    ***************************************************************

    public void drawFromBag() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        drawFromBagOrder = new DrawFromBagCommand(client.getNickname());
        System.out.println("Drawing from bag...\n");
        client.performGameAction(drawFromBagOrder);
    }

    public synchronized void playAssistantCard() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, LocalModelNotLoadedException {
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
                System.out.println("That's not a number! Try again.\n");
            }
        }
        while (i < 1 || i >= client.getLocalModel().getAssistantDecks().get(playerNumber).getDeck().size() && playedThisTurn.contains(i)) {
            System.out.println("Invalid number, try again\n");
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        }

        //I now have a valid assistant card
        System.out.println("Card played: " + i);
        client.getLocalModel().getBoardOf(client.getNickname()).setMoves(client.getLocalModel().getBoardOf(client.getNickname()).getDeck().get(input).getMove());
        playAssistantCardOrder = new PlayAssistantCard(client.getNickname(), input);
        try {
            client.performGameAction(playAssistantCardOrder);
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        } catch (AssistantCardNotFoundException e) {
            System.out.println("Looks like someone has already played that card this turn! Try again.");
            playAssistantCard();
        }
        client.setMyTurn(false);
    }

    public void performActionInTurn() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, UserNotInRoomException, UserNotRegisteredException, LocalModelNotLoadedException, RoomNotExistsException {
        do {
            //   System.out.println("Press any key to continue\n");
           // in.nextLine();
            System.out.println("Select an action: ");
            String input;
            while (true) {
                input = in.next();
                try {
                    action = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        } while (action < 1 || action > 7);
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
            default:
                System.out.println("Invalid input, try again\n");
        }

    }

    public void performActionInTurnExpert() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, UserNotInRoomException, UserNotRegisteredException, LocalModelNotLoadedException, RoomNotExistsException {
        do {
            //   System.out.println("Press any key to continue\n");
            in.nextLine();
            System.out.println("Select an action: ");
            String input;
            while (true) {
                input = in.next();
                try {
                    action = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        } while (action < 1 || action > 9);
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
            default:
                //TODO: add exception
        }

    }

    public void pickCloud() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("Almost at the end of your turn! Pick a cloud to refill your entrance.\n");
        printClouds();
        int i;
        String input;
        EnumMap<Colors, Integer> emptyEnum= new EnumMap<>(Colors.class);
        emptyEnum=initializeMove(emptyEnum);
        boolean invalidCloud;
        do {
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    invalidCloud=false;
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
            while (i < 0 || i > client.getLocalModel().getClouds().size()) {
                System.out.println("Invalid cloud number! Try again.\n");
                while (true) {
                    input = in.next();
                    try {
                        i = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a number! Try again.\n");
                    }
                }
                if (client.getLocalModel().getClouds().get(i).getStudents().equals(emptyEnum))
                {
                    System.out.println("Someone already picked that cloud! Try another.");
                }
                else {
                    invalidCloud = false;
                    break;
                }


            }
        }while(invalidCloud);

        //Reached a valid cloud color
        pickCloudOrder = new PickCloud(client.getNickname(), "cloud"+i);
       // System.out.println(client.getLocalModel().getState());
        client.performGameAction(pickCloudOrder);
        client.setMyTurn(false);
    }

    public void moveMN() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, LocalModelNotLoadedException {
        if (client.getLocalModel().isCanPlayMN()) {
            System.out.println("Input the number of steps you want Mother Nature to move! The maximum steps according to the card you played are \n " + client.getLocalModel().getBoardOf(client.getNickname()).getMoves());
            String input;
            int i;
            while (true) {
                input = in.next();
                try {
                    i = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
            while (i < 0 || i > client.getLocalModel().getBoardOf(client.getNickname()).getMoves()) {
                System.out.println("That number is not right! Try again.\n");
                while (true) {
                    input = in.next();
                    try {
                        i = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a number! Try again.\n");
                    }
                }
            }
            moveMotherNatureOrder = new MoveMotherNature(client.getNickname(), i);
            try {
                client.performGameAction(moveMotherNatureOrder);

            } catch (IncorrectStateException e) {
                System.out.println("You can't do that yet! Current state:" + e.getMessage());
            } catch (IncorrectArgumentException e) {
                System.out.println("That's not right, try again!\n");
                moveMN();
            }
            //After my turn is over I set the Mother Nature flag to false for the next turn
            client.getLocalModel().setCanPlayMN(false);
            endturn=true;
        } else System.out.println("You can't move Mother Nature yet! First things first: move three students!");
    }

    public void moveStudents() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, RoomNotExistsException, LocalModelNotLoadedException {
        if (!client.getLocalModel().isCanPlayMN()) {
            StrippedBoard myBoard = client.getLocalModel().getBoardOf(client.getNickname());
            // System.out.println("Board owner:"+ myBoard.getOwner());

            EnumMap<Colors, ArrayList<String>> studentsToGame = new EnumMap<>(Colors.class);
            for (Colors c : Colors.values()) {
                studentsToGame.put(c, new ArrayList<>());
            }
            System.out.println("These are the available islands: ");
            if(client.getExpertMode())
            printExpertIslands();
            else
                printIslands();
            System.out.println("These are the students in your entrance: \n");
            printEntrance(myBoard);
            String answer;
            String[] parts;
            String color;
            int value;
            int island;
            int movedStudents = 0;
            boolean isValidInputYN = false;
            boolean doItAgain;
            EnumMap<Colors, Integer> studentsToMove = new EnumMap<>(Colors.class);
            System.out.println("Do you want to move students to the dining room? Y\\N\n");
            in.nextLine();
            do {
                answer = in.nextLine();
                answer = answer.toLowerCase(Locale.ROOT);
                if (answer.equals("y") || answer.equals("n"))
                    isValidInputYN = true;
                else if (answer.equals("\n")) {
                    System.out.println("yolo\n");
                } else
                    System.out.println("Whoops! That's not right. Try again: \n");
            } while (!isValidInputYN);
            //Move students to the dining room
            doItAgain = true;
            isValidInputYN = false;
            if (answer.equals("y")) {
                do {
                    while (true) {
                        System.out.println("Type the students you want to move to the dining room as \"color, number\"");
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
                                //in.nextLine();
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                            //in.nextLine();
                        }

                    }
                    //  System.out.println("The color you chose was "+ color + " the number you picked was "+ value);
                    if (isValidColor(color)) {
                        if (value <= myBoard.getEntrance().get(stringToColor(color))) {
                            studentsToMove = initializeMove(studentsToMove);
                            studentsToMove.put(stringToColor(color), studentsToMove.get(stringToColor(color)) + value);
                            movedStudents += value;
                            studentsToGame = strippedToGame(studentsToMove, studentsToGame, "dining");
                            if (movedStudents < 3) {
                                System.out.println("Do you want to move other students to the dining room?\n");
                                do {
                                    answer = in.nextLine();
                                    answer = answer.toLowerCase(Locale.ROOT);
                                    if (answer.equals("y") || answer.equals("n"))
                                        isValidInputYN = true;
                                    else
                                        System.out.println("Whoops! That's not right. Try again: \n");
                                } while (!isValidInputYN);
                            }
                            //Since a player can only move 3 students in a turn there needs to be a check here too
                            if (answer.equals(("n"))) {
                                doItAgain = false;
                            }
                        } else
                            System.out.println("You don't have enough students of that color! Try again.\n");
                    } else
                        System.out.println("There is no such color as " + color + "! Try again. \n");
                } while (doItAgain && movedStudents < 3);
            }

            //End of dining room move segment
            //Move students to the islands if the player has moved less than 3 students already
            //Resetting destinations array for students to island part
            if (movedStudents < 3) {
                do {
                    while (true) {
                        System.out.println("Type the students you want to move to the island as \"color, number\", then input the island number!");
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
                                //in.nextLine();
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                            //in.nextLine();
                        }
                    }
                    String input;
                    while (true) {
                        input = in.next();
                        try {
                            island = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("That's not a number! Try again.\n");
                        }
                    }
                    doItAgain = true;
                    color = color.toUpperCase(Locale.ROOT);
                    studentsToMove = initializeMove(studentsToMove);
                    //  System.out.println("The color you chose was " + color + "the number you picked was " + value);
                    if (isValidColor(color)) {
                        //    System.out.println("The color you chose was " + color);
                        //  System.out.println("The number of students of that color in your entrance is " + myBoard.getEntrance().get(stringToColor(color)));
                        if (value <= myBoard.getEntrance().get(stringToColor(color))) {
                            //  System.out.println("Number of islands" + client.getLocalModel().getIslands().size());
                            if (island > 0 && island <= client.getLocalModel().getIslands().size()) {

                                studentsToMove.put(stringToColor(color), value);
                                movedStudents += value;
                                if (movedStudents>3)
                                    doItAgain=false;
                                studentsToGame = strippedToGame(studentsToMove, studentsToGame, "island" + island);
                                if (movedStudents < 3) {
                                    System.out.println("Do you want to move other students to the islands?\n");
                                    do {
                                        answer = in.nextLine();
                                        answer = answer.toLowerCase(Locale.ROOT);
                                        if (answer.equals("y") || answer.equals("n"))
                                            isValidInputYN = true;
                                        else
                                            System.out.println("Whoops! That's not right. Try again: \n");
                                    } while (!isValidInputYN);
                                }
                                if (answer.equals("n") || movedStudents == 3) {
                                    doItAgain = false;
                                } else
                                    System.out.println("You still have " + (3 - movedStudents) + " students to move!\n");

                            } else
                                System.out.println("Invalid island number! Try again.\n");
                        } else
                            System.out.println("You don't have enough students of that color! Try again.\n");
                    } else
                        System.out.println("There is no such color as " + color + "! Try again. \n");
                } while (doItAgain);

            } else
                System.out.println("You already moved three students this turn\n");

            moveStudentsOrder = new MoveStudents(client.getNickname(), studentsToGame);
            client.performGameAction(moveStudentsOrder);
            client.getLocalModel().setCanPlayMN(true);
        } else
            System.out.println("You already moved students this turn, you can't do that anymore!\n");

    }

    //End of MoveStudents

    // **********************************   Character card methods    ***************************************************************
    public void playCharacterCard() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, LocalModelNotLoadedException {
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
                System.out.println("That's not a number! Try again.\n");
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
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        }
        tmp = client.getLocalModel().getCharacters().get(i);
        // System.out.println(tmp.getRequirements().getRequirements().toLowerCase(Locale.ROOT));
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
                client.getLocalModel().getBoardOf(client.getNickname()).setMoves(client.getLocalModel().getBoardOf(client.getNickname()).getMoves() + 2);
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
    }

    public void playCharacterA(int id) throws AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("You have chosen a no parameter character! Buckle up, the effects are on the way!\n");
        playCharacterCardAOrder = new PlayCharacterCardA(client.getNickname(), id);
        try {
            client.performGameAction(playCharacterCardAOrder);
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        } catch (NotEnoughCoinsException e) {
            System.out.println(StringNames.NOT_ENOUGH_COINS);
        }

    }

    //This is only one card but it's unique since it calls for a color AND an island.
    public void playCharacterB(int id, StrippedCharacter character) throws AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("You have chosen a student island card\n");
        int student, island;
        System.out.println(character.getDescription());
        printStudentsOnCard(character);
        System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
            System.out.println();
            String input;
            while (true) {
                input = in.next();
                try {
                    student = Integer.parseInt(input);
                    if (student>=0&&student<=5)
                    break;
                    else
                        System.out.println("Invalid number! Try again.");
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.");
                }
            }
        printExpertIslands();
        while (true) {
            input = in.next();
            try {
                island = Integer.parseInt(input);
                if (island>0&&island<client.getLocalModel().getIslands().size())
                break;
                else
                    System.out.println("Invalid island! Try again.");
            } catch (NumberFormatException e) {
                System.out.println("That's not a number! Try again.\n");
            }
        }
        //Now I have a valid island and color choice.
            playCharacterCardBOrder = new PlayCharacterCardB(client.getNickname(), id, student, island);
            try {
                client.performGameAction(playCharacterCardBOrder);
            } catch (UserNotInRoomException | UserNotRegisteredException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
        }
    }

    public void playCharacterC(int id, StrippedCharacter card) throws AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, LocalModelNotLoadedException {
        System.out.println("You have chosen a card that requires two sets of students\n");
        String answer;
        String[] parts;
        String color;
        int value;
        int movedStudents = 0;
        boolean validInput = false;
        System.out.println("These are the students on the card: \n");
        EnumMap<Colors, Integer> students1 = new EnumMap<>(Colors.class), students2 = new EnumMap<>(Colors.class);
        //There's gonna be two cases here
        StrippedBoard myBoard = client.getLocalModel().getBoardOf(client.getNickname());
        if (card.getDescription().equals("Swap 3 of the students on this card with 3 from your Entrance!")) {
            System.out.println(card.getDescription());
            //Students input code
            //Students from entrance
            do {
                while (true) {
                    printEntrance(myBoard);
                    printStudentsOnCard(card);
                    System.out.println("Type the students you want to move from your entrance as \"color, number\"");
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
                            //in.nextLine();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Something went wrong with your input, try again!");
                        //in.nextLine();
                    }
                }
                if (isValidColor(color)) {
                    if (value <= myBoard.getEntrance().get(stringToColor(color))) {
                        students1 = initializeMove(students2);
                        students1.put(stringToColor(color), students1.get(stringToColor(color)) + value);
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
                            //in.nextLine();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Something went wrong with your input, try again!");
                        //in.nextLine();
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

            for (Colors c : myBoard.getDining().keySet()) {
                diningStudents += myBoard.getDining().get(c);
            }
            if (diningStudents >= 2) {
                do {
                    while (true) {
                        printEntrance(myBoard);
                        printDining(myBoard);
                        System.out.println("Type the students you want to move from your entrance as \"color, number\"");
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
                                if (value <= 2)
                                    break;
                                else
                                    System.out.println("You can't move more than 2 students with this card! Try again.");
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                System.out.println("Something went wrong with your input, try again!");
                                //in.nextLine();
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                            //in.nextLine();
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
                    while (true) {
                        printDining(myBoard);
                        System.out.println("Type the students you want to move from your dining room as \"color, number\"");
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
                                if (value <= 2)
                                    break;
                                else
                                    System.out.println("You can't move more than 2 students with this card! Try again.");
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                System.out.println("Something went wrong with your input, try again!");
                                //in.nextLine();
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Something went wrong with your input, try again!");
                            //in.nextLine();
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
            } else
            { System.out.println("Whoops! You don't have enough students in your dining room to play this card!");}
            //**************************** Students from dining room taken **************************************

        }

        if (validInput) {
            playCharacterCardCOrder = new PlayCharacterCardC(client.getNickname(), id, students1, students2);
            try {
                client.performGameAction(playCharacterCardCOrder);
            } catch (UserNotInRoomException | UserNotRegisteredException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
            }
        }
    }

    public void playCharacterD(int id) throws AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        //There are THREE cards that ask for colors (the professors one just takes them all. One of them has students on it, so I have to differentiate.
        //And the No Entry card that wants the island number so it ends up here
        System.out.println(client.getLocalModel().getCharacters().get(id).getDescription());
        if (client.getLocalModel().getCharacters().get(id).getDescription().equals("Choose a color: EVERYONE must return 3 students of that color to the bag!")) {
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
            } catch (UserNotInRoomException | UserNotRegisteredException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
            }
        }
        //It's basically the same but I have to show the player the students on the card first
        else if (client.getLocalModel().getCharacters().get(id).getDescription().equals("Take one student from this card and put them in the dining room, then draw another from the bag and replace them!"))
        {
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
            } catch (UserNotInRoomException | UserNotRegisteredException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
            }

        }
        else if (client.getLocalModel().getCharacters().get(id).getDescription().equals("Calculate the influence on any Island and reap the rewards!"))
            {printExpertIslands();
            System.out.println("Choose an island to resolve and reap the rewards!");
            String input;
            int island;
            while (true) {
                input = in.next();
                try {
                    island = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
            island--;
            while (island < 0 || island > client.getLocalModel().getIslands().size()) {
                System.out.println("That number is not right! Try again.\n");
                while (true) {
                    input = in.next();
                    try {
                        island = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a number! Try again.\n");
                    }
                }
            }
                playCharacterCardDOrder = new PlayCharacterCardD(client.getNickname(), id, island);
                try {
                    client.performGameAction(playCharacterCardDOrder);
                } catch (UserNotInRoomException | UserNotRegisteredException e) {
                    throw new RuntimeException(e);
                } catch (NotEnoughCoinsException e) {
                    System.out.println(StringNames.NOT_ENOUGH_COINS);
                }
        }
        else
        {
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
                    System.out.println("That's not a number! Try again.\n");
                }
            }
            choice--;
            while (choice < 0 || choice > client.getLocalModel().getIslands().size()) {
                System.out.println("That number is not right! Try again.\n");
                while (true) {
                    input = in.next();
                    try {
                        choice = Integer.parseInt(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("That's not a number! Try again.\n");
                    }
                }
            }
            playCharacterCardDOrder = new PlayCharacterCardD(client.getNickname(), id, choice);
            try {
                client.performGameAction(playCharacterCardDOrder);
            } catch (UserNotInRoomException | UserNotRegisteredException e) {
                throw new RuntimeException(e);
            } catch (NotEnoughCoinsException e) {
                System.out.println(StringNames.NOT_ENOUGH_COINS);
            }


        }
    }

    // **********************************   Printer Methods    ***************************************************************
    public void printCommandHelp() {
        System.out.println("O-----------------------------------------------------------------------O\n" +
                "|              The commands available are the following:                |\n" +
                "|\"Press 1 to view everyone's boards                                    |\n" +
                "|\"Press 2 to view every player's name                                  |\n" +
                "|\"Press 3 to view all the islands                                      |\n" +
                "|\"Press 4 to view all the clouds                                       |\n" +
                "|\"Press 5 to move students across the islands and the dining room      |\n" +
                "|\"Press 6 to move mother nature. This will end your turn               |\n" +
                "|\"Press 7 to view this message again                                   |\n" +
                "O-----------------------------------------------------------------------O");
    }

    public void expertPrintCommandHelp() {
        System.out.println("O-----------------------------------------------------------------------O\n" +
                "|              The commands available are the following:                |\n" +
                "|\"Press 1 to view everyone's boards                                    |\n" +
                "|\"Press 2 to view every player's name                                  |\n" +
                "|\"Press 3 to view all the islands                                      |\n" +
                "|\"Press 4 to view all the clouds                                       |\n" +
                "|\"Press 5 to move students across the islands and the dining room      |\n" +
                "|\"Press 6 to move mother nature. This will end your turn               |\n" +
                "|\"Press 7 to see the character cards in the game                       |\n" +
                "|\"Press 8 to play a character card                                     |\n" +
                "|\"Press 9 to view this message again                                   |\n" +
                "O-----------------------------------------------------------------------O");
    }

    public void printPlayerBoards() {
        ArrayList<StrippedBoard> boards = client.getLocalModel().getBoards();
        System.out.println("Player boards:\n");
        for (StrippedBoard s : boards) {
            if( s.getOwner().equals(client.getNickname()))
                System.out.println(ansi().fg(YELLOW).a("This is your board!").reset());
            printPlayerBoard(s);
        }
    }

    public void printPlayerBoard(StrippedBoard board) {

        System.out.println("O----------------------O");
        System.out.println(board.getOwner() + "'s board: ");
        System.out.println("Coins: " + board.getCoins());
        System.out.println("\nDining room configuration: ");
        printDining(board);
        for (Colors c : board.getDining().keySet()) {
            System.out.println(c + " students: " + board.getDining().get(c));
        }
        System.out.println("O----------------------O");
        printEntrance(board);
        for (Colors c : board.getEntrance().keySet()) {
            System.out.println(c + " students: " + board.getEntrance().get(c));
        }
        System.out.println("\nNumber of towers: " + board.getNumberOfTowers());
        System.out.println("\nProfessors table: ");
        for (Colors c : board.getProfessorsTable()) {
            System.out.println(c + "\n");
        }
        System.out.println("O----------------------O");
    }

    public void printPlayerNames() {
        for (StrippedBoard board : client.getLocalModel().getBoards()) {
            System.out.println(board.getOwner() + "\n");
        }
    }

    public void printIslands() {
        ArrayList<StrippedIsland> islands = client.getLocalModel().getIslands();
        int i = 0, motherNature = 0;
        for (StrippedIsland island : islands) {
            if (!island.getName().equals("EMPTY")) {
                printIsland(island);
                i++;
            }
                if (island.hasMotherNature())
            {
                motherNature=i;
            }

        }

        System.out.println("Mother Nature is on isle number " + (motherNature) + "!");

    }

    public void printExpertIslands() {
        ArrayList<StrippedIsland> islands = client.getLocalModel().getIslands();
        int i = 1, motherNature = 0;
        for (StrippedIsland island : islands) {
            if (!island.getName().equals("EMPTY")) {
                printexpertIsland(island);
                i++;
            }
            if (island.hasMotherNature())
            {
                motherNature=i;
            }

        }

        System.out.println("Mother Nature is on isle number " + (motherNature) + "!");
    }

    public void printDining(StrippedBoard board) {
        Integer i;
        int printColumns;
        Ansi.Color color;
        System.out.println("----------------------╗");
        for (Colors c : board.getDining().keySet()) {
            i = board.getDining().get(c);
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

    public void printEntrance(StrippedBoard board) {
        Integer i;
        int rows = 0;
        Ansi.Color color;
        System.out.println("\nEntrance configuration: ");
        System.out.println("O-----O");
        System.out.print("|");
        for (Colors c : board.getEntrance().keySet()) {
            i = board.getEntrance().get(c);
            color = colorsToColor(c);

            while (i > 0) {
                if ((rows % 3) < 2) {
                    if (rows != 6) {
                        System.out.print(ansi().fg(color).a("* ").reset());
                        rows++;
                    } else {
                        System.out.print(ansi().fg(color).a("  *  ").reset());
                        System.out.print("|");
                    }
                } else {
                    System.out.print(ansi().fg(color).a("*").reset());
                    System.out.print("|");
                    System.out.print("\n");
                    System.out.print("|");
                    rows++;
                }
                i--;
            }
        }
        System.out.println();
        System.out.println("O-----O");
    }

    public void printIsland(StrippedIsland island) {
        int i;
        Color color;
        int rows = 0;
        System.out.println("Island: " + island.getName());
        System.out.println("Students on the island: ");
        if (island.hasMotherNature())
            System.out.println(ansi().fg(CYAN).a("* Mother Nature's here!  *").reset());
        System.out.println(" ____________________");
        System.out.println("/                    \\");
        for (Colors c : island.getStudents().keySet()) {
            //System.out.println(c + " students: " + board.getEntrance().get(c));
            i = island.getStudents().get(c);
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
        System.out.println("\\                    /");
        System.out.println(" ____________________");
        int w = 0;
        for (Colors c : island.getStudents().keySet()) {
            if (w % 3 == 0) {
                System.out.print(c + " students: " + island.getStudents().get(c) + "\t");
                w++;
            } else
                System.out.print(c + " students: " + island.getStudents().get(c) + "\n");
        }

        if (island.getNumOfTowers() == 0)
            System.out.println("There are no towers yet on this island!");
        else
            System.out.println("Towers: " + island.getNumOfTowers() + " " + island.getTowersColor() + "towers \n");

    }

    public void printexpertIsland(StrippedIsland island) {
        int i;
        Color color;
        int rows = 0;
        System.out.println("Island: " + island.getName());
        System.out.println("Students on the island: ");
        if (island.hasMotherNature())
            System.out.println(ansi().fg(CYAN).a("* Mother Nature's here!  *").reset());
        if (island.hasNoEnterTile())
            System.out.println(ansi().fg(RED).a("!!! There's a No Entry Tile on this island !!!").reset());
        System.out.println(" ____________________");
        System.out.println("/                    \\");
        for (Colors c : island.getStudents().keySet()) {
            //System.out.println(c + " students: " + board.getEntrance().get(c));
            i = island.getStudents().get(c);
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
        System.out.println("\\                    /");
        System.out.println(" ____________________");
        int w = 0;
        for (Colors c : island.getStudents().keySet()) {
            if (w % 3 == 0) {
                System.out.print(c + " students: " + island.getStudents().get(c) + "\t");
                w++;
            } else
                System.out.print(c + " students: " + island.getStudents().get(c) + "\n");
        }

        if (island.getNumOfTowers() == 0)
            System.out.println("There are no towers yet on this island!\n");
        else
            System.out.println("Towers: " + island.getNumOfTowers() + " " + island.getTowersColor() + "towers \n");


        if (island.hasMotherNature())
            System.out.println("Mother Nature is on this island!");
        else
            System.out.println("Mother Nature is not on this island!");
    }

    public void printClouds() {
        EnumMap<Colors, Integer> students;
        int i;
        Color color;
        int rows = 0;
        for (StrippedCloud cloud : client.getLocalModel().getClouds()) {
            students = cloud.getStudents();
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

    public void printCharacterCards() throws LocalModelNotLoadedException {
        int i = 0;
        ArrayList<StrippedCharacter> temp = client.getLocalModel().getCharacters();
        for (StrippedCharacter c : temp) {
            System.out.println("Character " + (i+1));
            System.out.println("Price: " + c.getPrice() + ", description:  " + c.getDescription());
            i++;
        }
        System.out.println();
        System.out.println("You currently have "+client.getLocalModel().getBoardOf(client.getNickname()).getCoins() + " coins!");
    }

    public void printAssistantCards() throws LocalModelNotLoadedException {
        AssistantCardDeck myDeck = client.getLocalModel().getBoardOf(client.getNickname()).getDeck();
        int i = 0;
        for (AssistantCard a : myDeck.getDeck()) {
            System.out.println("Card number " + a.getImageName() + " Moves: " + a.getMove());
            i++;
        }
    }

    public void printStudentsOnCard(StrippedCharacter card) {
        Integer i;
        int rows = 0;
        Ansi.Color color;
        System.out.println("*-----*");
        for (Colors c : card.getStudents().keySet()) {
            i = card.getStudents().get(c);
            color = colorsToColor(c);

            while (i > 0) {
                if ((rows % 3) < 2) {
                    if (rows != 6) {
                        System.out.print(ansi().fg(color).a("* ").reset());
                        rows++;
                    } else {
                        System.out.print(ansi().fg(color).a("  *  ").reset());
                        System.out.print("|");
                    }
                } else {
                    System.out.print(ansi().fg(color).a("*").reset());
                    System.out.print("|");
                    System.out.print("\n");
                    System.out.print("|");
                    rows++;
                }
                i--;
            }
        }
        System.out.println();
        System.out.println("*-----*");
    }


    // **********************************   Utility Methods    ***************************************************************
    public boolean isValidColor(String input) {
        input = input.toUpperCase(Locale.ROOT);
        for (Colors c : Colors.values()) {
            if (c.name().equals(input))
                return true;
        }
        return false;
    }

    public Colors stringToColor(String input) {
        input = input.toLowerCase(Locale.ROOT);
        switch (input) {
            case "red":
                return Colors.RED;
            case "blue":
                return Colors.BLUE;
            case "yellow":
                return Colors.YELLOW;
            case "green":
                return Colors.GREEN;
            case "pink":
                return Colors.PINK;
            default:
                return Colors.YELLOW;
        }
    }

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

    public EnumMap<Colors, ArrayList<String>> strippedToGame(EnumMap<Colors, Integer> students, EnumMap<Colors, ArrayList<String>> returnStudents, String destination) {
        EnumMap<Colors, Integer> tmp = students;
        for (Colors c : students.keySet()) {
            //I have to count the number of students moved in the stripped class and build myself an enummap which Game can understand
            while (tmp.get(c) > 0) {
                returnStudents.get(c).add(destination);
                // System.out.println("Adding " + destination);
                tmp.put(c, students.get(c) - 1);
            }

        }
       /* System.out.println("Students to game: ");
        for (Colors c : returnStudents.keySet()) {
              System.out.println("Color "+ c);
        }*/

        return returnStudents;
    }

    private synchronized void sendArrayString(ArrayList<String> messageArray) {
        for (String message : messageArray) System.out.println(message);
    }

    public EnumMap<Colors, Integer> initializeMove(EnumMap<Colors, Integer> move) {
        for (Colors c : Colors.values()) {
            move.put(c, 0);
        }
        return move;
    }

    public synchronized void waitForTurn() throws InterruptedException {
        if (!client.isMyTurn()) {
            System.out.println("Waiting ...");
            System.out.println(ansi().eraseScreen());
            Thread.sleep(500);
            System.out.println("Waiting ..");
            System.out.println(ansi().eraseScreen());
            Thread.sleep(500);
            System.out.println("Waiting . .");
            System.out.println(ansi().eraseScreen());
            Thread.sleep(500);
            System.out.println("Waiting .. ");
            System.out.println(ansi().eraseScreen());
            Thread.sleep(500);
        }
    }

    // **********************************   End of Utility Methods    ***************************************************************
}


//TODO: watch all the \n that you print since sometimes they tend to separate things