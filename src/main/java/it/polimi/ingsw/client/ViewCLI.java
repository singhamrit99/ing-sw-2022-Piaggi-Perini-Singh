package it.polimi.ingsw.client;

import it.polimi.ingsw.model.stripped.StrippedModel;


public class ViewCLI {
    StrippedModel myModel= new StrippedModel();


    public void standardBehaviour()
    {
        System.out.print("Welcome to the game!\n");
    }
    public void commandHelp()
    {
        System.out.println("The commands available are the following:\n" +
                "1 to check everyone's boards\n 2 to check islands\n");
    }

}
