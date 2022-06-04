package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.stripped.StrippedBoard;
import it.polimi.ingsw.model.stripped.StrippedCharacter;
import it.polimi.ingsw.model.stripped.StrippedCloud;
import it.polimi.ingsw.model.stripped.StrippedIsland;
import it.polimi.ingsw.server.commands.*;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.*;


public class ViewCLI implements UI {
    public static String view;
    Client client;
    String nickName;
    String currentPlayer;
    boolean isANumber;
    ArrayList<Integer> playedThisTurn;
    int playerNumber;
    String clientRoom = null;
    int action;
    int turnMoves;
    MoveMotherNature moveMotherNatureOrder;
    MoveStudents moveStudentsOrder;
    PickCloud pickCloudOrder;
    PlayAssistantCard playAssistantCardOrder;
    PlayCharacterCardA playCharacterCardAOrder;
    PlayCharacterCardB playCharacterCardBOrder;
    PlayCharacterCardC playCharacterCardCOrder;
    PlayCharacterCardD playCharacterCardDOrder;
    DrawFromBagCommand drawFromBagOrder;
    private final Scanner in = new Scanner(System.in);

    public ViewCLI(Client client) {
        this.client = client;
        this.client.setUi(this);
        this.client.view= StringNames.LAUNCHER;
    }

    public void Start() throws RemoteException, UserNotInRoomException, NotLeaderRoomException, NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, UserNotRegisteredException, InterruptedException {

        System.out.println("Welcome to the lobby!\nWhat's your name?");
        while (true) {
            try {
                nickName = in.nextLine();
                client.registerClient(nickName);
                client.view=StringNames.LOBBY;
                break;
            } catch (UserAlreadyExistsException e) {
                System.out.println("That username is already in the game! Try another.\n");
            }
        }
        System.out.println("Possible options: \n JOIN to join a room; \n CREATE to create a new room;\n ROOMS to list rooms;" +
                "\n PLAYERS to list players in current lobby; \n INFO to view your current room's information;\n CHANGE to toggle expert mode for the current lobby;\n " +
                "LEAVE to leave current lobby;\n" +
                "HELP to see this message again.\n" +
                "When you're ready to go and everyone is in the lobby type START to start the game!\n");
        //Main room loop
        while (!client.isInGame()) {

            //codice della lobby
            String command = in.nextLine().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
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
                    setExpertMode();// TODO exception
                    break;
                case "leave":
                    leaveRoom();//fatto
                    break;
                case "start":
                    startGameRequest();
                    break;
                case "help":
                    System.out.println("Possible options: \n JOIN to join a room; \n CREATE to create a new room;\n ROOMS to list rooms;" +
                            "\n PLAYERS to list players in current lobby; \n INFO to view your current room's information;\n " +
                            "CHANGE to toggle expert mode for the current lobby;\n " +
                            "LEAVE to leave current lobby;\n" +
                            "HELP to see this message again.\n" +
                            "When you're ready to go and everyone is in the lobby type START to start the game!\n");
                    break;
                case "\n":
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
        System.out.println("Loading...");

        //Main game loop
        while (client.isInGame()) {
            if (playedThisTurn==null)
            playedThisTurn=new ArrayList<>();
            //Assistant Card play phase
            while (client.getPhase()==0) {
                while (!client.isMyTurn()) {

                    //Wait for the other players to be done with their turn while I still output their moves...
                }
                if (client.isMyTurn()&&!client.isDrawnOut()) {
                    drawFromBag();}
                if (client.isMyTurn()&&client.getPhase()==0)
                    playAssistantCard();

                Thread.sleep(400);
            }

            //Turn phase
            while (client.getPhase()<3) {
                while(!client.isMyTurn())
                {

                }
                performActionInTurn();
                printCommandHelp();
            }
            pickCloud();
        }
    }

    //Room methods
    public void startGameRequest() throws RemoteException
    {
        try {
            client.startGame();
        }catch(NotEnoughPlayersException e){
            System.out.println(StringNames.ALONE_IN_ROOM); //todo TINO to LORE: Lore usa 'StringNames' per i messaggi, aggiungi i tuoi messaggi, cosí il usiamo anche noi .
        }
        catch (UserNotInRoomException e) {
            System.out.println("You're not in a room yet!\n");
        } catch (NotLeaderRoomException e) {
            System.out.println("You're not the leader of this room you can't start the game!\n");
        } catch (RoomNotExistsException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void startGame() throws RemoteException {

    }

    @Override
    public void currentPlayer(String s){
        System.out.println("It's now " + s + "'s turn!\n");
    }

    @Override
    public void notifyCloud(PropertyChangeEvent e) {

        client.setDrawnOut(true);
    }

    @Override
    public void deckChange(String assistantCard) {
        String tmp;
        System.out.println(client.getLocalModel().getCurrentPlayer() + " has played " + assistantCard);
       tmp= assistantCard.replaceAll("[^\\d.]", "");
       if (playedThisTurn==null)
           playedThisTurn= new ArrayList<>();
       playedThisTurn.add(Integer.parseInt(tmp));
    }

    @Override
    public void assistantCardPlayed(PropertyChangeEvent e) {

    }


    @Override
    public void islandChange(PropertyChangeEvent e) {
        StrippedIsland oldIsland, newIsland;
        oldIsland = (StrippedIsland) e.getOldValue();
        newIsland = (StrippedIsland) e.getNewValue();
        System.out.println("Island number" + oldIsland.getName() + " changed from");
        printIsland(oldIsland);
        System.out.println("to");
        printIsland(newIsland);
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
        System.out.println(currentPlayer + "conquered island " + island.getName() + "!\n");

    }

    @Override
    public void diningChange(PropertyChangeEvent e) {
        EnumMap<Colors, Integer> newDining;
        newDining = (EnumMap<Colors, Integer>) e.getNewValue();
        System.out.println(e.getOldValue() + " modified their dining room! Here's the new configuration...");
        for (Colors c : newDining.keySet()) {
            System.out.println(c + " students: "+ newDining.get(c));
        }
    }

    @Override
    public void entranceChanged(PropertyChangeEvent e) {
        EnumMap<Colors,Integer> newBoard;
        String player;
        player= (String) e.getOldValue();
        newBoard = (EnumMap<Colors, Integer>) e.getNewValue();
        System.out.println(player + "'s entrance changed to \n");
        System.out.println("to:");
        for (Colors c : newBoard.keySet()) {
            System.out.println(c + " students: "+ newBoard.get(c));
        }
    }

    @Override
    public void towersEvent(PropertyChangeEvent e) {

        String player = (String) e.getNewValue();
        int i = (int) e.getOldValue();
        System.out.println("Player" + player + "now has " + i + " towers in their board!\n");

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
    public void removedProfessors(PropertyChangeEvent e) {

    }

    @Override
    public void roomsAvailable(ArrayList<String> rooms) {
        try {
            getRooms();
        }catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void roomJoin(ArrayList<String> players){
       try{
           getPlayersInRoom();
       }catch (RemoteException e)
       {
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
            //TODO exception
        } catch (UserNotInRoomException e) {
            System.out.println("You're not in a room now!\n");
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
                }catch(RoomFullException e)
                {
                    System.out.println(e.getMessage());
                }
                client.view=StringNames.ROOM;
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

    public void printCommandHelp() {
        System.out.println("The commands available are the following:\n" +
                "Press 1 to view everyone's boards\n" +
                "Press 2 to view every player's name\n" +
                "Press 3 to view all the islands\n" +
                "Press 4 to move students across the islands and the dining room\n" +
                "Press 5 to move mother nature. This will end your turn\n" +
                "Press 6 to see the character cards in the game\n" +
                "Press 7 to play a character card\n" +
                "Press 8 to view this message again\n");
    }

    //Game methods

    public void drawFromBag() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        drawFromBagOrder = new DrawFromBagCommand(client.getNickname());
        System.out.println("Drawing from bag...\n");
        client.performGameAction(drawFromBagOrder);
    }

    public synchronized void playAssistantCard() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("It's your turn! Pick an assistant card to play. \n");
        printAssistantCards();
        int i;
        String input;
            while (true) {
                input=in.next();
                try{
                    i= Integer.parseInt(input);
                    break;
                }catch(NumberFormatException e)
                {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        while (i < 1 || i >= client.getLocalModel().getAssistantDecks().get(playerNumber).getDeck().size()&&playedThisTurn.contains(i)) {
            System.out.println("Invalid number, try again\n");
            while (true) {
                input=in.next();
                try{
                    i= Integer.parseInt(input);
                    break;
                }catch(NumberFormatException e)
                {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        }

        //I now have a valid assistant card
        System.out.println("Card played: " +i);
        turnMoves=client.getLocalModel().getAssistantDecks().get(playerNumber).get("Assistente"+i).getMove();
        playAssistantCardOrder = new PlayAssistantCard(this.nickName, "Assistente" + i);
        try {
            client.performGameAction(playAssistantCardOrder);
            turnMoves=i;
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }catch(AssistantCardNotFoundException e)
        {
            System.out.println("Looks like someone has already played that card this turn! Try again.");
            playAssistantCard();
        }
        client.setMyTurn(false);
    }

    public void printAssistantCards() {
        AssistantCardDeck myDeck = client.getLocalModel().getAssistantDecks().get(playerNumber);
        int i = 0;
        for (AssistantCard a : myDeck.getDeck()) {
            System.out.println("Card number " + (i+1) + " " + a.getImageName().replaceAll("[^a-zA Z0-9]", "") + " " + a.getMove());
            i++;
        }
    }

    public void performActionInTurn() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException, UserNotInRoomException, UserNotRegisteredException {
        do {
            System.out.println("Press any key to continue\n");
            in.nextLine();
            printCommandHelp();
            System.out.println("Select an action: ");
            String input;
            while (true) {
                input=in.next();
                try{
                    action= Integer.parseInt(input);
                    break;
                }catch(NumberFormatException e)
                {
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
                moveStudents();
                break;
            case 5:
                moveMN();
                break;
            case 6:
                printCharacterCards();
                break;
            case 7:
                playCharacterCard();
                break;
            case 8:
                printCommandHelp();
                break;
            default:
                //TODO: add exception
        }

    }

    public synchronized void waitForTurn() throws InterruptedException {
        System.out.println("Waiting for turn ...");
        System.out.flush();
        Thread.sleep(500);
        System.out.println("Waiting for turn ..");
        System.out.flush();
        Thread.sleep(500);
        System.out.println("Waiting for turn . .");
        System.out.flush();
        System.out.println("Waiting for turn .. ");
        System.out.flush();
        Thread.sleep(500);
    }

    public void printCharacterCards() {
        int i = 0;
        ArrayList<StrippedCharacter> temp = client.getLocalModel().getCharacters();
        for (StrippedCharacter c : temp) {
            System.out.println("Character " + i);
            System.out.println("Price: " + c.getPrice() + ", description:  " + c.getDescription());
            i++;
        }
    }

    public void playCharacterCard() throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        StrippedCharacter tmp;
        System.out.println("Select the character you want to play! You currently have " + client.getLocalModel().getBoards().get(playerNumber).getCoins() + " coins \n");
        printCharacterCards();
        int i;
        String input;
        while (true) {
            input=in.next();
            try{
                i= Integer.parseInt(input);
                break;
            }catch(NumberFormatException e)
            {
                System.out.println("That's not a number! Try again.\n");
            }
        }
        while (i < 0 || i > 2) {
            System.out.println("That's not right! Try again\n");
            while (true) {
                input=in.next();
                try{
                    i= Integer.parseInt(input);
                    break;
                }catch(NumberFormatException e)
                {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        }
        tmp = client.getLocalModel().getCharacters().get(i);
        switch (tmp.getRequirements().getRequirements().toLowerCase(Locale.ROOT)) {
            //TODO: test if this actually works as intended
            case "islands":
                playCharacterB(i);
                break;
            case "colors,islands":
                playCharacterC(i, tmp);
                break;
            default:
                if (tmp.getRequirements().getRequirements().equals(""))
                    //Automatic action card
                    playCharacterA(i);
                else
                    //No resource used but input needed card
                    playCharacterD(i);
                break;
        }
    }

    public void pickCloud() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("Almost at the end of your turn! Pick a cloud to refill your entrance.\n");
        printClouds();
        int i;
        String input;
        while (true) {
            input=in.next();
            try{
                i= Integer.parseInt(input);
                break;
            }catch(NumberFormatException e)
            {
                System.out.println("That's not a number! Try again.\n");
            }
        }
    while (i<0||i>client.getLocalModel().getClouds().size()) {
        System.out.println("Invalid cloud number! Try again.\n");
        while (true) {
            input=in.next();
            try{
                i= Integer.parseInt(input);
                break;
            }catch(NumberFormatException e)
            {
                System.out.println("That's not a number! Try again.\n");
            }
        }
    }
    pickCloudOrder= new PickCloud(nickName, i);
    client.performGameAction(pickCloudOrder);
    }

    public void printClouds()
    {
        EnumMap<Colors, Integer> students;
        for (StrippedCloud cloud: client.getLocalModel().getClouds())
        {
            students= cloud.getStudents();
            System.out.println(("Cloud name:" +cloud.getName()));
            System.out.println("Number of students: ");
            for(Colors c: Colors.values())
            {
                System.out.println(c +" "+students.get(c));
            }
        }
    }

    public void playCharacterA(int id) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("You have chosen a no parameter character! Buckle up, the effects are on the way!\n");
        playCharacterCardAOrder = new PlayCharacterCardA(nickName, id);
        try {
            client.performGameAction(playCharacterCardAOrder);
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public void playCharacterB(int id) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("You have chosen a student island card\n");
        int students = 0, island;
        System.out.println(client.getLocalModel().getCharacters().get(id).getDescription());
        System.out.println();
        String input;
        while (true) {
            input=in.next();
            try{
                island= Integer.parseInt(input);
                break;
            }catch(NumberFormatException e)
            {
                System.out.println("That's not a number! Try again.\n");
            }
        }
        playCharacterCardBOrder = new PlayCharacterCardB(nickName, id, students, island);
        try {
            client.performGameAction(playCharacterCardBOrder);
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public void playCharacterC(int id, StrippedCharacter card) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("You have chosen a card that requires two sets of students\n");
        System.out.println("These are the students on your card: \n");

        EnumMap<Colors, Integer> students1 = null, students2 = null;
        //TODO: add students on card implementation for StrippedCharacters
        playCharacterCardCOrder = new PlayCharacterCardC(nickName, id, students1, students2);
        try {
            client.performGameAction(playCharacterCardCOrder);
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public void playCharacterD(int id) throws NotEnoughCoinsException, AssistantCardNotFoundException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("You have to make a choice\n");
        //There are two cards that only need a color to work, and the effects then take place
        //Globally. Both of them ask for colors
        System.out.println(client.getLocalModel().getCharacters().get(id));
        System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
        int choice;
        String input;
        boolean invalidChoice = true;
        do {
            while (true) {
                input=in.next();
                try{
                    choice= Integer.parseInt(input);
                    break;
                }catch(NumberFormatException e)
                {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
            if (choice<0||choice>4)
            {
                System.out.println("Invalid color! Try again.");
                System.out.println("Choose a color! 0=Yellow, 1=Blue, 2=Green, 3=Red, 4=Pink");
            }
            else
                invalidChoice=false;
        }while(!invalidChoice);

        playCharacterCardDOrder = new PlayCharacterCardD(nickName, id, choice);
        try {
            client.performGameAction(playCharacterCardDOrder);
        } catch (UserNotInRoomException | UserNotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveMN() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        System.out.println("Input the number of steps you want Mother Nature to move! The maximum steps according to the card you played are \n " + turnMoves);
        String input;
        int i;
        while (true) {
            input=in.next();
            try{
                i= Integer.parseInt(input);
                break;
            }catch(NumberFormatException e)
            {
                System.out.println("That's not a number! Try again.\n");
            }
        }
        while (i < 0 || i > turnMoves) {
            System.out.println("That number is not right! Try again.\n");
            while (true) {
                input=in.next();
                try{
                    i= Integer.parseInt(input);
                    break;
                }catch(NumberFormatException e)
                {
                    System.out.println("That's not a number! Try again.\n");
                }
            }
        }
        moveMotherNatureOrder= new MoveMotherNature(nickName, i);
        try {
            client.performGameAction(moveMotherNatureOrder);
        }catch (IncorrectStateException e)
        {
            System.out.println("You can't do that yet!\n");
        }
        catch(IncorrectArgumentException e)
        {
            System.out.println("That's not right, try again!\n");
            moveMN();
        }

        //We now have a valid move for Mother Nature
    }

    public void moveStudents() throws NotEnoughCoinsException, AssistantCardNotFoundException, UserNotInRoomException, NegativeValueException, IncorrectStateException, MotherNatureLostException, ProfessorNotFoundException, UserNotRegisteredException, IncorrectPlayerException, RemoteException, IncorrectArgumentException {
        StrippedBoard myBoard= null;
        ArrayList<StrippedBoard> boards = client.getLocalModel().getBoards();
        int i = 0;
         for (StrippedBoard b : boards) {
            if (b.getOwner().equals(nickName))
                myBoard=b;
        }
         System.out.println("Board owner:"+ myBoard.getOwner());
        System.out.println("These are the students in your entrance: \n");
        System.out.println("\nEntrance configuration: ");
        for (Colors c : myBoard.getEntrance().keySet()) {
            System.out.println(c + " students: " + myBoard.getEntrance().get(c) + "\n");
        }
        EnumMap<Colors ,ArrayList<String>> studentsToGame = new EnumMap<>(Colors.class);
        for (Colors c: Colors.values())
        {
            studentsToGame.put(c, new ArrayList<>());
        }
        String answer;
        String[] parts;
        String color;
        int value;
        int island;
        int movedStudents = 0;
        boolean isValidInputYN = false;
        boolean doItAgain;
        EnumMap<Colors, Integer> studentsToMove = new EnumMap<>(Colors.class);
        ArrayList<StrippedIsland> myIslands = client.getLocalModel().getIslands();
        System.out.println("Do you want to move students to the dining room? Y\\N\n");
        do {
            answer = in.nextLine();
            answer = answer.toLowerCase(Locale.ROOT);
            if (answer.equals("y") || answer.equals("n"))
                isValidInputYN = true;
            else if (answer.equals("\n"))
            {}
            else
            System.out.println("Whoops! That's not right. Try again: \n");
        } while (!isValidInputYN);
        //Move students to the dining room
        doItAgain = true;
        isValidInputYN = false;
        if (answer.equals("y")) {
            do {
                System.out.println("Type the students you want to move to the dining room as \"color, number\"");
                answer = in.nextLine();
                parts = answer.split(",|, | ,");
                color = parts[0];
                color = color.replaceAll("[^a-zA Z0-9]", "");
                parts[1]=parts[1].trim();
                value = Integer.parseInt(parts[1]);
                color = color.toUpperCase(Locale.ROOT);
                System.out.println("The color you chose was "+ color + " the number you picked was "+ value);
                if (isValidColor(color)) {
                    System.out.println("StringToColor output: "+ stringToColor(color));
                    System.out.println("The number of students of that color in your entrance is "+ myBoard.getEntrance().get(stringToColor(color)));
                    if (value<=myBoard.getEntrance().get(stringToColor(color))) {
                        studentsToMove =initializeMove(studentsToMove);
                        studentsToMove.put(stringToColor(color), studentsToMove.get(stringToColor(color)) + value);
                        movedStudents += value;
                        studentsToGame= strippedToGame(studentsToMove,studentsToGame,"dining");
                        if(movedStudents<3) {
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
                System.out.println("Type the students you want to move to the island as \"color, number\", then input the island number");
                answer = in.nextLine();
                parts = answer.split(",|, | ,");
                color = parts[0];
                color = color.replaceAll("[^a-zA Z0-9]", "");
                parts[1]=parts[1].trim();
                value = Integer.parseInt(parts[1]);
                String input;
                while (true) {
                    input=in.next();
                    try{
                        island= Integer.parseInt(input);
                        break;
                    }catch(NumberFormatException e)
                    {
                        System.out.println("That's not a number! Try again.\n");
                    }
                }
                doItAgain=true;
                color = color.toUpperCase(Locale.ROOT);
                studentsToMove = initializeMove(studentsToMove);
                System.out.println("The color you chose was "+ color + "the number you picked was "+ value);
                if (isValidColor(color)) {
                    System.out.println("The color you chose was "+ color);
                    System.out.println("The number of students of that color in your entrance is "+ myBoard.getEntrance().get(stringToColor(color)));
                    if (value<=myBoard.getEntrance().get(stringToColor(color)))  {
                        System.out.println("Number of islands" +client.getLocalModel().getIslands().size());
                        if (island > 0 && island <= client.getLocalModel().getIslands().size()) {

                            studentsToMove.put(stringToColor(color), value);
                            movedStudents += value;
                            studentsToGame= strippedToGame(studentsToMove,studentsToGame,"island"+island);
                            System.out.println("Do you want to move other students to the islands?\n");
                            do {
                                answer = in.nextLine();
                                answer = answer.toLowerCase(Locale.ROOT);
                                if (answer.equals("y") || answer.equals("n"))
                                    isValidInputYN = true;
                                else
                                    System.out.println("Whoops! That's not right. Try again: \n");
                            } while (!isValidInputYN && movedStudents < 3);
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
            moveStudentsOrder = new MoveStudents(nickName, studentsToGame);
            client.performGameAction(moveStudentsOrder);
        } else
            System.out.println("You already moved three students this turn\n");
    }

//End of MoveStudents
public EnumMap<Colors, Integer> initializeMove(EnumMap<Colors, Integer> move)
{
    for (Colors c: Colors.values())
    {
        move.put(c, 0);
    }
    return move;
}
    public void printPlayerBoards() {
        ArrayList<StrippedBoard> boards = client.getLocalModel().getBoards();
        System.out.println("Player boards:\n");
        for (StrippedBoard s : boards) {
            System.out.println(s.getOwner() + "'s board: ");
            System.out.println("Coins: " + s.getCoins());
            System.out.println("\nDining room configuration: ");
            for (Colors c : s.getDining().keySet()) {
                System.out.println(c + " students: " + s.getDining().get(c));
            }
            System.out.println("\nEntrance configuration: ");
            for (Colors c : s.getEntrance().keySet()) {
                System.out.println(c + " students: " + s.getEntrance().get(c) + "\n");
            }
            System.out.println("\nNumber of towers: " + s.getNumberOfTowers());
            System.out.println("\nProfessors table: ");
            for (Colors c : s.getProfessorsTable()) {
                System.out.println(c + "\n");
            }
        }
    }

    public void printPlayerBoard(String playerNickname) {
        ArrayList<StrippedBoard> boards = client.getLocalModel().getBoards();

        int i = 0;
        while (!client.getLocalModel().getBoards().get(i).getOwner().equals(playerNickname)) {
            i++;
        }
        StrippedBoard s = client.getLocalModel().getBoards().get(i);
        System.out.println(s.getOwner() + "'s board: ");
        System.out.println("Coins: " + s.getCoins());
        System.out.println("\nDining room configuration: ");
        for (Colors c : s.getDining().keySet()) {
            System.out.println(c + " students: " + s.getDining().get(c));
        }
        System.out.println("\nEntrance configuration: ");
        for (Colors c : s.getEntrance().keySet()) {
            System.out.println(c + " students: " + s.getEntrance().get(c) + "\n");
        }
        System.out.println("\nNumber of towers: " + s.getNumberOfTowers());
        System.out.println("\nProfessors table: ");
        for (Colors c : s.getProfessorsTable()) {
            System.out.println(c + "\n");
        }

        System.out.println("Player boards:\n");
    }

    public void printPlayerNames() {

        for (StrippedBoard board : client.getLocalModel().getBoards()) {
            System.out.println(board.getOwner() + "\n");
        }
    }

    public void printIslands() {
        ArrayList<StrippedIsland> islands = client.getLocalModel().getIslands();
        int i=0, motherNature=0;
        for (StrippedIsland island : islands) {
            if (island.hasMotherNature)
                motherNature=i;
            System.out.println("Island name: " + island.getName() + "\n");
            System.out.println("Number of towers: " + island.getNumOfTowers() + "\t");
            if (island.isHasNoEnterTile())
            {
                System.out.println("There's a \"no entry\" tile on this island\n");
            }
            else
            {
                System.out.println("There is no \"no entry\" tile on this island\n");
            }
            System.out.println("Students on the island: " + island.getStudents() + "\n");
            System.out.println("Towers: " + island.getTowersColor() + " towers \n");
            i++;
        }
        System.out.println("Mother Nature is on isle number "+ (motherNature+1)+"!");

    }

    public void printIsland(StrippedIsland island) {
        System.out.println("Number of towers: " + island.getNumOfTowers() + "\n");
        System.out.println("Has no entry tile: " + island.isHasNoEnterTile() + "\n");
        System.out.println("Students on the island: ");
        System.out.println(" students: " + island.getStudents() + "\n");
        System.out.println("Towers: " + island.getNumOfTowers() + island.getTowersColor() + "towers \n");
    }

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
        int a;
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

    public EnumMap<Colors, ArrayList<String>> strippedToGame(EnumMap<Colors, Integer> students, EnumMap<Colors, ArrayList<String>> returnStudents, String destination) {
        EnumMap<Colors, Integer> tmp= students;
        for (Colors c : students.keySet()) {
            //I have to count the number of students moved in the stripped class and build myself an enummap which Game can understand
                while(tmp.get(c)>0) {
                    returnStudents.get(c).add(destination);
                    System.out.println("Adding " + destination);
                    tmp.put(c,students.get(c)-1);
                }

        }
       // System.out.println("Students to game: ");
        for (Colors c : returnStudents.keySet())
        {
          //  System.out.println("Color "+ c);
            for (String s : returnStudents.get(c))
                System.out.println(s);
        }

        return returnStudents;

    }

    private synchronized void sendArrayString(ArrayList<String> messageArray) {
        for (String message : messageArray) System.out.println(message);
    }
}
