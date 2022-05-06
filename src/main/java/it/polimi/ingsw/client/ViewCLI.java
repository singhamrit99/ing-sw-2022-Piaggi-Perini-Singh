package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.Controller;


public class ViewCLI {
    Controller controller;

    public ViewCLI(Controller controller) {
        this.controller = controller;
    }

    public void standardBehaviour()
    {

        System.out.print("Welcome to the game!\n");


    }
    public void commandHelp()
    {
        System.out.println("The commands available are the following:\n" +
                "1 to check everyone's boards\n 2 to check islands\n 3 to check characters currently in play\n" +
                "When it's your turn, use \n 1 to move students across the islands\n2 to move students between your entrance and dining room\n" +
                "3 to move mother nature: this will end your turn!\n");
    }

}
