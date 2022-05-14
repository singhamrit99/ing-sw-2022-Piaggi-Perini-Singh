package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.stripped.*;

import javax.swing.event.ChangeEvent;
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
    boolean isMyTurn;
    int action;

    public ViewCLI(Controller controller, String nickName) {
        this.controller = controller;
        this.nickName= nickName;

    }

    public void gameStart() {

        System.out.print("Welcome to the game!\n");
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


    public void performAction() {
        do {
            printCommandHelp();
            System.out.println("Select an action: ");
            String s= in.nextLine();
            action= Integer.parseInt(s);
        }while(action<1||action>7);

        switch(action){
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



        }




    }

    public void sendMessage(int messageID) {

    }

    public void displayBoard(String owner) {



    }


    public void playCharacterCard()
    {

    }
    public void moveMN()
    {

    }
    public void moveStudents() {
        EnumMap<Colors, ArrayList<String>> studentsToMove = new EnumMap<Colors, ArrayList<String>>(Colors.class);
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
        int movedStudents=0;
        boolean isValidInputYN = false;
        boolean doItAgain;
        System.out.println("Do you want to move students to the dining room? Y\\N\n");
        do {
            answer = in.nextLine();
            answer= answer.toLowerCase(Locale.ROOT);
            if (answer.equals("y")||answer.equals("n"))
                isValidInputYN=true;
            else
                System.out.println("Whoops! That's not right. Try again: \n");
        }while (!isValidInputYN);

        //Move students to the dining room
        doItAgain=true;
        isValidInputYN= false;
        if (answer.equals("y")) {
            do {
                System.out.println("Type the students you want to move to the dining room as \"color, number\"");
                answer = in.nextLine();
                parts = answer.split(" ");
                color = parts[0];
                color = color.replaceAll("[^a-zA Z0-9]", "");
                value = Integer.parseInt(parts[1]);
                color = color.toUpperCase(Locale.ROOT);
                //TODO: replace incorrect checks on string value for color
                if (isValidColor(color)) {
                    if (myBoard.getEntrance().get(stringToColor(color)) <= value) {
                        //Move the students to the dining room
                        movedStudents+=value;
                        System.out.println("Do you want to move other students to the dining room?\n");
                        do {
                            answer = in.nextLine();
                            answer= answer.toLowerCase(Locale.ROOT);
                            if (answer.equals("y")||answer.equals("n"))
                                isValidInputYN=true;
                            else
                                System.out.println("Whoops! That's not right. Try again: \n");
                        }while (!isValidInputYN&&movedStudents<3);
                        //Since a player can only move 3 students in a turn there needs to be a check here too
                        if(answer.equals(("n")))
                            doItAgain = false;


                    } else
                        System.out.println("You don't have enough students of that color! Try again.\n");
                } else
                    System.out.println("There is no such color as " + color + "! Try again. \n");
            } while (doItAgain&&movedStudents<3);

        }
        //Move students to the islands if the player has moved less than 3 students already
       if (movedStudents<3)
       {
           System.out.println("Type the students you want to move to the island as \"color, number, number of island\" (for example, \"RED, 1, 5)\"");
           answer = in.nextLine();
           parts = answer.split(" ");
           color = parts[0];
           color = color.replaceAll("[^a-zA Z0-9]", "");
           value = Integer.parseInt(parts[1]);
           island= Integer.parseInt(parts[2]);
           color = color.toUpperCase(Locale.ROOT);

           do {
               if (isValidColor(color)) {
                   if (myBoard.getEntrance().get(stringToColor(color)) <= value) {
                       if(island>0&& island<localModel.getIslands().size())
                       {
                           //...
                           doItAgain= false;
                           //...
                       }
                       else
                           System.out.println("Invalid island number! Try again.\n");
                   }
                   else
                       System.out.println("You don't have enough students of that color! Try again.\n");
               } else
                   System.out.println("There is no such color as " + color + "! Try again. \n");
           }while (doItAgain);

       }
       else
           System.out.println("You already moved three students this turn\n");


       //TODO: add send event
       }



    public void printPlayerBoards() {
        ArrayList<StrippedBoard> boards = localModel.getBoards();
        System.out.println("Player boards:\n");
        for (StrippedBoard s : boards) {
            System.out.println(s.getOwner()+ "'s board: ");
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

    public boolean isValidColor(String input)
    {
        for (Colors c: Colors.values())
        {
            if (c.name().equals(input))
                return true;
        }
        return false;
    }
    public Colors stringToColor(String input)
    {
        switch (input){
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

}
