package it.polimi.ingsw.client;

import it.polimi.ingsw.exceptions.UserAlreadyExistsException;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.stripped.StrippedBoard;
import it.polimi.ingsw.model.stripped.StrippedCharacter;
import it.polimi.ingsw.model.stripped.StrippedIsland;
import it.polimi.ingsw.model.stripped.StrippedModel;
import it.polimi.ingsw.server.commands.*;

import java.rmi.RemoteException;
import java.util.*;


public class ViewCLI {
    Client client;
    String nickName;
    boolean hasGameStarted = false;
    StrippedModel localModel;
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
    private final Scanner in = new Scanner(System.in);

    public ViewCLI(Client client) {
        this.client = client;
    }

    public void Start() throws RemoteException {


        System.out.println("Welcome to the lobby!\nWhat's your name?");

        while (true) {
            try {
                nickName = in.nextLine();
                client.registerClient(nickName);
                break;
            } catch (UserAlreadyExistsException e) {
                System.out.println("That username is already in the game! Try another.\n");
            }
        }


        System.out.println("Possible options: \n JOIN to join a room; \n CREATE to create a new room;\n ROOMS to list rooms;" +
                "\n PLAYERS to list players in current lobby; \n INFO to view your current room's information;\n CHANGE to toggle expert mode for the current lobby;\n " +
                "HELP to see this message again.\n" +
                "When you're ready to go and everyone is in the lobby type START to start the game!\n");


        //Main game loop with messages
        while (!hasGameStarted) {

            //codice della lobby
            String command = in.nextLine().toLowerCase(Locale.ROOT);
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
                    System.out.println("Possible options: \n JOIN to join a room; \n CREATE to create a new room;\n ROOMS to list rooms;" +
                            "\n PLAYERS to list players in current lobby; \n INFO to view your current room's information;\n CHANGE to toggle expert mode for the current lobby;\n " +
                            "HELP to see this message again.\n" +
                            "When you're ready to go and everyone is in the lobby type START to start the game!\n");
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
    }

    private void startGame() {

    }

    private void getPlayersInRoom() throws RemoteException {
        if (clientRoom != null) {
            ArrayList<String> response = client.getNicknamesInRoom(clientRoom);
            sendArrayString(response);
        } else
            System.out.println("You're not in a room, so there are no players to show\n");
    }

    private void getLobbyInfo() throws RemoteException {
        if (clientRoom != null)
            sendArrayString(client.requestLobbyInfo(clientRoom));
        else
            System.out.println("You're not in a room yet\n");
    }

    private void leaveRoom() throws RemoteException {
        if (clientRoom == null)
            System.out.println("You're not in a room yet, you can't leave!\n");
        else {
            client.leaveRoom(nickName, clientRoom);
            clientRoom = null;
        }
    }

    public void getRooms() throws RemoteException {
        ArrayList<String> response = client.getRooms();
        sendArrayString(response);
    }

    public void setExpertMode() throws RemoteException {
        if (clientRoom != null) {
            if (client.getNicknamesInRoom(clientRoom).get(0).equals(nickName)) {
                boolean result = false;
                System.out.println("Do you want to play in expert mode? Y/N");
                String answer;
                answer = in.nextLine().toLowerCase(Locale.ROOT);
                switch (answer) {
                    case "y": {
                        result = true;
                        System.out.println("Expert mode enabled!\n");
                        break;
                    }
                    case "n": {
                        result = false;
                        System.out.println("Expert mode disabled|\n");
                        break;
                    }
                    default:
                        System.out.println("Command not recognized\n");

                }
                client.setExpertMode(nickName, clientRoom, result);
            } else
                System.out.println("You're not this lobby's leader, you can't do that!\n");
        } else
            System.out.println("You're not in a room now!\n");
    }

    public synchronized void requestRoomCreation() throws RemoteException {
        System.out.println("Insert room name: \n");
        String nameRoom;
        nameRoom = in.nextLine();
        while (client.getRooms().contains(nameRoom)) {
            System.out.println("Ops, there is another room with the same name! Choose another one please. \n");
            nameRoom = in.nextLine();
        }
        client.createRoom(nickName, nameRoom);
        clientRoom = nameRoom;
    }

    public synchronized void requestRoomJoin() throws RemoteException {
        String requestedRoom;
        System.out.println("Select the room: \n");
        if (client.getRooms().isEmpty()) System.out.println("There are no rooms, you can only create a new one");
        else {
            sendArrayString(client.getRooms());
            requestedRoom = in.nextLine();
            while (!client.getRooms().contains(requestedRoom)) {
                System.out.println("Ops, there are no rooms with that name: try again\n");
                requestedRoom = in.nextLine();
            }
            if (requestedRoom.equals(clientRoom)) {
                System.out.println("You're already in that room!\n");
            } else {
                client.requestRoomJoin(nickName, requestedRoom);
                clientRoom = requestedRoom;
                System.out.println("You entered room " + clientRoom + " successfully \n");
                System.out.println("Players in this room:");
                sendArrayString(client.getNicknamesInRoom(clientRoom));
            }
        }
    }

    public void printCommandHelp() {
        System.out.println("The commands available are the following:\n" +
                "Press 1 to view everyone's boards\n" +
                "Press 2 to view every player's name\n" +
                "Press 3 to view all the islands\n" +
                "Press 4 to move students across the islands and the dining room\n" +
                "Press 5 to move mother nature\n" +
                "Press 6 to see the character cards in the game\n" +
                "Press 7 to play a character card\n" +
                "Press 8 to view this message again\n");
    }

    public void playAssistantCard() {

        System.out.println("It's your turn! Play an assistant card! \n");


    }

    public void performActionInTurn() {
        do {
            printCommandHelp();
            System.out.println("Select an action: ");
            String s = in.nextLine();
            action = Integer.parseInt(s);
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
                playCharacterCard();
                break;
            case 7:
                printCommandHelp();
                break;

            default:
                //TODO: add exception
        }

    }

    public void printAssistantCards() {

    }

    public void playCharacterCard() {
        StrippedCharacter tmp;
        System.out.println("Select the character you want to play! You currently have " + localModel.getBoards().get(playerNumber).getCoins() + " coins \n");
        int i = 0;
        for (StrippedCharacter c : localModel.getCharacters()) {
            System.out.println("Character " + i);
            System.out.println("Price: " + c.getPrice() + ", description:  " + c.getDescription());
        }
        i = in.nextInt();
        while (i < 0 || i > 2) {
            System.out.println("That's not right! Try again\n");
            i = in.nextInt();
        }
        tmp = localModel.getCharacters().get(i);

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

    public void playCharacterA(int id) {
        System.out.println("You have chosen a no parameter character! Buckle up, the effects are on the way!\n");
        //playCharacterCardAOrder = new PlayCharacterCardA(id);


    }

    public void playCharacterB(int id) {
        System.out.println("You have chosen a student island card\n");


    }

    public void playCharacterC(int id, StrippedCharacter card) {
        System.out.println("You have chosen a card that requires two sets of students\n");
        System.out.println("These are the students on your card: \n");

        //TODO: add students on card implementation for StrippedCharacters
        for (Colors c : Colors.values()) {


        }

    }

    public void playCharacterD(int id) {
        System.out.println("You have to make a choice, for in determination we find strength\n");
        //There are two cards that only need a color to work, and the effects then take place
        //Globally.
    }

    public void moveMN() {
        System.out.println("Input the number of steps you want Mother Nature to move!\n ");
        int input = in.nextInt();
        while (input < 0 || input > turnMoves) {
            System.out.println("That number is not right! Try again.\n");
            input = in.nextInt();
        }
        //We now have a valid move for Mother Nature
        moveMotherNatureOrder = new MoveMotherNature(nickName, input);
    }

    public void moveStudents() {
        StrippedBoard myBoard;
        int i = 0;
        while (!localModel.getBoards().get(i).getOwner().equals(nickName)) {
            i++;
        }
        myBoard = localModel.getBoards().get(i);
        System.out.println("These are the students in your entrance: \n");
        System.out.println("\nEntrance configuration: ");
        for (Colors c : myBoard.getEntrance().keySet()) {
            System.out.println(c + " students: " + myBoard.getEntrance().get(c) + "\n");
        }

        String answer;
        String[] parts;
        String color;
        int value;
        int island;
        int movedStudents = 0;
        boolean isValidInputYN = false;
        boolean doItAgain;
        EnumMap<Colors, Integer> moveStudents = null;
        ArrayList<StrippedIsland> myIslands = localModel.getIslands();
        System.out.println("Do you want to move students to the dining room? Y\\N\n");
        do {
            answer = in.nextLine();
            answer = answer.toLowerCase(Locale.ROOT);
            if (answer.equals("y") || answer.equals("n"))
                isValidInputYN = true;
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
                parts = answer.split(" ");
                color = parts[0];
                color = color.replaceAll("[^a-zA Z0-9]", "");
                value = Integer.parseInt(parts[1]);
                color = color.toUpperCase(Locale.ROOT);
                if (isValidColor(color)) {
                    if (myBoard.getEntrance().get(stringToColor(color)) <= value) {
                        moveStudents = myBoard.getDining();
                        moveStudents.put(stringToColor(color), moveStudents.get(stringToColor(color)) + value);
                        movedStudents += value;

                        System.out.println("Do you want to move other students to the dining room?\n");
                        do {
                            answer = in.nextLine();
                            answer = answer.toLowerCase(Locale.ROOT);
                            if (answer.equals("y") || answer.equals("n"))
                                isValidInputYN = true;
                            else
                                System.out.println("Whoops! That's not right. Try again: \n");
                        } while (!isValidInputYN && movedStudents < 3);
                        //Since a player can only move 3 students in a turn there needs to be a check here too
                        if (answer.equals(("n"))) {
                            doItAgain = false;
                            myBoard.setDining(moveStudents);
                        }
                    } else
                        System.out.println("You don't have enough students of that color! Try again.\n");
                } else
                    System.out.println("There is no such color as " + color + "! Try again. \n");
            } while (doItAgain && movedStudents < 3);

        }
        //End of dining room move segment
        moveStudentsOrder = new MoveStudents(nickName, strippedToDining(moveStudents));

        //Move students to the islands if the player has moved less than 3 students already
        if (movedStudents < 3) {
            do {
                System.out.println("Type the students you want to move to the island as \"color, number, number of island\" (for example, \"RED, 1, 5)\"");
                answer = in.nextLine();
                parts = answer.split(" ");
                color = parts[0];
                color = color.replaceAll("[^a-zA Z0-9]", "");
                value = Integer.parseInt(parts[1]);
                island = Integer.parseInt(parts[2]);
                color = color.toUpperCase(Locale.ROOT);

                moveStudents = myIslands.get(island).getStudents();

                if (isValidColor(color)) {
                    if (myBoard.getEntrance().get(stringToColor(color)) <= value) {
                        if (island > 0 && island < localModel.getIslands().size()) {

                            moveStudents.put(stringToColor(color), myIslands.get(island).getStudents().get(stringToColor(color)) + value);
                            movedStudents += value;

                            System.out.println("Do you want to move other students to the islands?\n");
                            do {
                                answer = in.nextLine();
                                answer = answer.toLowerCase(Locale.ROOT);
                                if (answer.equals("y") || answer.equals("n"))
                                    isValidInputYN = true;
                                else
                                    System.out.println("Whoops! That's not right. Try again: \n");
                            } while (!isValidInputYN && movedStudents < 3);
                            if (answer.equals("n") && movedStudents == 3) {
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


    }

//End of MoveStudents

    public void printPlayerBoards() {
        ArrayList<StrippedBoard> boards = localModel.getBoards();
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
        ArrayList<StrippedBoard> boards = localModel.getBoards();

        int i = 0;
        while (!localModel.getBoards().get(i).getOwner().equals(playerNickname)) {
            i++;
        }
        StrippedBoard s = localModel.getBoards().get(i);
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

        for (StrippedBoard board : localModel.getBoards()) {
            System.out.println(board.getOwner() + "\n");
        }
    }

    public void printIslands() {
        ArrayList<StrippedIsland> islands = localModel.getIslands();

        for (StrippedIsland island : islands) {
            System.out.println("Island name: " + island.getName() + "\n");
            System.out.println("Number of towers: " + island.getNumOfTowers() + "\n");
            System.out.println("Has no entry tile: " + island.isHasNoEnterTile() + "\n");
            System.out.println("Students on the island: ");
            for (Colors c : island.getStudents().keySet()) {
                System.out.println(c + " students: " + island.getStudents() + "\n");
            }
            System.out.println("Towers: " + island.getNumOfTowers() + island.getTowersColor() + "towers \n");
        }
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
        }
        return Colors.BLUE;
    }

    public EnumMap<Colors, ArrayList<String>> strippedToDining(EnumMap<Colors, Integer> students) {
        EnumMap<Colors, ArrayList<String>> returnedStudents = null;
        ArrayList<String> destinations = new ArrayList<>();
        for (Colors c : students.keySet()) {
            //I have to count the number of students moved in the stripped class and build myself an enummap which Game can understand
            int i = students.get(c);
            while (i > 0) {
                destinations.add("dining");
                returnedStudents.put(c, destinations);
                //FIXME: this doesn't really work, have to come back to this
            }

        }

        return returnedStudents;

    }

    private synchronized void sendArrayString(ArrayList<String> messageArray) {
        for (String message : messageArray) System.out.println(message);
    }
}
