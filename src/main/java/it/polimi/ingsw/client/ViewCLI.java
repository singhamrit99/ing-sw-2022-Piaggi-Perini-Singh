package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.stripped.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;


public class ViewCLI {
    Controller controller;
    private Scanner in = new Scanner(System.in);
    boolean isGameOver = false;

    public ViewCLI(Controller controller) {
        this.controller = controller;
    }

    public void gameStart() {

        System.out.print("Welcome to the game!\n");
        printCommandHelp();
        //Main game loop with messages
        while (!isGameOver) {

            //If it's not your turn the game just displays some game info and updates when it changes
            /*
            while (not your turn)
            displayBoardIdle(strippedboard);
             */


        }


    }

    public void printCommandHelp() {
        System.out.println("The commands available are the following:\n" +
                "Press B to view everyone's boards\n" +
                "Press P to view every player's name\n" +
                "Press I to view all the islands\n" +
                "Press S to move students across the islands\n" +
                "Press M to move mother nature\n" +
                "Press C to play a character card\n" +
                "Press H to view this message again\n");
    }


    public void performAction(LocalModel localModel) {

    }

    public void sendMessage(int messageID) {

    }

    public void displayBoardIdle(LocalModel localModel) {


    }

    public void printPlayerBoards(LocalModel localModel) {
        ArrayList<StrippedBoard> boards = localModel.getBoards();
        System.out.println("Player boards:\n");
        for (StrippedBoard s : boards) {
            //System.out.println(s.getPlayer()+ "'s board: ");
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

    public void printPlayerNames(LocalModel localModel) {

        for (StrippedBoard board : localModel.getBoards()) {
            System.out.println(board.getOwner() + "\n");
        }
    }

    public void printIslands(LocalModel localModel) {
        StrippedIslands islands = localModel.getIslands();

        for (StrippedIsland island : islands.getStrippedIslands()) {
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

}
