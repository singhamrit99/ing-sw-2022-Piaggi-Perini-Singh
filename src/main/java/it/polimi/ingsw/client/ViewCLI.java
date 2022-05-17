package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.CommandInvoker;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.stripped.*;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Scanner;


public class ViewCLI {
    Controller controller;
    String nickName;
    private Scanner in = new Scanner(System.in);
    boolean isGameOver = false;
    StrippedModel localModel;
    int playerNumber;
    boolean isMyTurn;
    int action;
    int turnMoves;
    CommandInvoker invoker= new CommandInvoker(controller);
    MoveMotherNature moveMotherNatureOrder;
    MoveStudents moveStudentsOrder;
    PickCloud pickCloudOrder;
    PlayAssistantCard playAssistantCardOrder;
    PlayCharacterCardA playCharacterCardAOrder;
    PlayCharacterCardB playCharacterCardBOrder;
    PlayCharacterCardC playCharacterCardCOrder;
    PlayCharacterCardD playCharacterCardDOrder;
    public ViewCLI(Controller controller, String nickName) {
        this.controller = controller;
        this.nickName = nickName;

    }
    public void gameStart() {

        System.out.print("Welcome to the game!\n");

        //Calculates the player number through the nickname, handy further down in the code
        playerNumber = 0;
        while (!localModel.getBoards().get(playerNumber).getOwner().equals(nickName)) {
            playerNumber++;
        }

        printCommandHelp();
        //Main game loop with messages
        while (!isGameOver) {

            //If it's not your turn the game just displays some game info and updates when it changes

            /*while (isMyTurn)
            {

            }*/


        }


    }

    public void printCommandHelp() {
        System.out.println("The commands available are the following:\n" +
                "Press 1 to view everyone's boards\n" +
                "Press 2 to view every player's name\n" +
                "Press 3 to view all the islands\n" +
                "Press 4 to move students across the islands and the dining room\n" +
                "Press 5 to move mother nature\n" +
                "Press 6 to play a character card\n" +
                "Press 7 to view this message again\n");
    }

    public void playAssistantCard(){

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

    public void sendMessage(int messageID) {

    }

    public void displayBoard(String owner) {


    }


    public void playCharacterCard() {
        StrippedCharacter tmp;
        System.out.println("Select the character you want to play! You currently have " +localModel.getBoards().get(playerNumber).getCoins()+  " coins \n");
        int i=0;
        for (StrippedCharacter c : localModel.getCharacters())
        {
            System.out.println("Character "+ i);
            System.out.println("Price: " + c.getPrice() + ", description:  "+ c.getDescription());
        }
        i=in.nextInt();
        while(i<0||i>2)
        {
            System.out.println("That's not right! Try again\n");
            i=in.nextInt();
        }
        tmp=localModel.getCharacters().get(i);

        switch (tmp.getRequirements().getRequirements().toLowerCase(Locale.ROOT))
        {
            //TODO: test if this actually works as intended
            case "islands":
                playCharacterB(i);
                break;
            case "colors,islands":
                playCharacterC(i, tmp);
                break;
            default:
                if(tmp.getRequirements().getRequirements().equals(""))
                    //Automatic action card
                playCharacterA(i);
                else
                    //No resource used but input needed card
                playCharacterD(i);
                break;

        }

    }

    public void playCharacterA(int id)
    {
        System.out.println("You have chosen a no parameter character! Buckle up, the effects are on the way!\n");
        playCharacterCardAOrder= new PlayCharacterCardA(id);
        invoker.executeCommand(playCharacterCardAOrder);

    }
    public void playCharacterB(int id)
    {
        System.out.println("You have chosen a student island card\n");

    }
    public void playCharacterC(int id, StrippedCharacter card)
    {
        System.out.println("You have chosen a card that requires two sets of students\n");
        System.out.println("These are the students on your card: \n");

        //TODO: add students on card implementation for StrippedCharacters
        for (Colors c: Colors.values())
        {


        }

    }
    public void playCharacterD(int id)
    {
        System.out.println("You have to make a choice, for in determination we find strength\n");

    }

    public void moveMN() {
        System.out.println("Input the number of steps you want Mother Nature to move!\n ");
        int input= in.nextInt();
        while (input<0||input>turnMoves)
        {
            System.out.println("That number is not right! Try again.\n");
            input= in.nextInt();
        }
        //We now have a valid move for Mother Nature
        moveMotherNatureOrder= new MoveMotherNature(input);
        invoker.executeCommand(moveMotherNatureOrder);
    }

    public void moveStudents() {
        EnumMap<Colors, ArrayList<String>> studentsToMove = new EnumMap<>(Colors.class);
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
        Colors pickColor;
        int value;
        int island;
        int movedStudents = 0;
        boolean isValidInputYN = false;
        boolean doItAgain;
        EnumMap<Colors, Integer> moveStudents=null;
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
                            movedStudents+=value;

                            System.out.println("Do you want to move other students to the islands?\n");
                            do {
                                answer = in.nextLine();
                                answer = answer.toLowerCase(Locale.ROOT);
                                if (answer.equals("y") || answer.equals("n"))
                                    isValidInputYN = true;
                                else
                                    System.out.println("Whoops! That's not right. Try again: \n");
                            } while (!isValidInputYN && movedStudents < 3);
                            if (answer.equals("n")&&movedStudents==3)
                            {
                                doItAgain=false;
                            }
                            else
                                System.out.println("You still have "+(3-movedStudents)+" students to move!\n");

                        } else
                            System.out.println("Invalid island number! Try again.\n");
                    } else
                        System.out.println("You don't have enough students of that color! Try again.\n");
                } else
                    System.out.println("There is no such color as " + color + "! Try again. \n");


            } while (doItAgain);

        } else
            System.out.println("You already moved three students this turn\n");


        moveStudentsOrder= new MoveStudents(strippedToDining(moveStudents));
        invoker.executeCommand(moveStudentsOrder);
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
        input=input.toUpperCase(Locale.ROOT);
        for (Colors c : Colors.values()) {
            if (c.name().equals(input))
                return true;
        }
        return false;
    }

    public Colors stringToColor(String input) {
        input=input.toLowerCase(Locale.ROOT);
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

    public EnumMap<Colors, ArrayList<String>> strippedToDining(EnumMap<Colors, Integer> students)
    {
        EnumMap<Colors, ArrayList<String>> returnedStudents = null;
        ArrayList<String> destinations = new ArrayList<>();
        for (Colors c: students.keySet())
        {
           //I have to count the number of students moved in the stripped class and build myself an enummap which Game can understand
            int i=students.get(c);
            while (i>0) {
                destinations.add("dining");
                returnedStudents.put(c,destinations);
            //FIXME: this doesn't really work but I'm doing other work to keep my sanity
            }

        }
        
        return returnedStudents;
        
    }




}
