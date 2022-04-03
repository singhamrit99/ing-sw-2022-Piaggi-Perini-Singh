package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;



//This class of cards' power tampers in some way with the Influence calculations. CharacterIDs range from 5 to 8
public class InfluenceCard extends CharacterCard {

    public InfluenceCard(int characterID, int startingPrice, int price, String powerDescription, int id) {
        super(characterID, startingPrice, price, powerDescription);
        this.id = id;
    }

    int id = this.getCharacterID();

    @Override
    public void callPower(Player playercaller, int id) {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

    if (id == 5) {
        IgnoreTowers(playercaller);
    } else if (id == 6) {
        AddTwo(playercaller);
    } else if (id == 7) {
        //TODO: Add way for user to input valid color
       /* System.out.println("Choose the color to ignore in the calculation");
        IgnoreStudent(playercaller,color );*/
    }
    else
    {
        throw new java.lang.Error("Card Mismatch");
    }


    }

    //Every power call starts with a brief description of the power itself, the same the player saw when choosing it
    public void IgnoreTowers(Player playercaller) {
        System.out.println((this.getPowerDescription()));


    }

    public void AddTwo(Player playercaller) {
        System.out.println((this.getPowerDescription()));


    }

    /*public void IgnoreStudent(Player playercaller, Colors color) {
        System.out.println((this.getPowerDescription()));
    }*/

    //TODO: Add a way to select a specific island to call
    public void CallOnIsland(Player playercaller) {
        System.out.println((this.getPowerDescription()));

    }
}


