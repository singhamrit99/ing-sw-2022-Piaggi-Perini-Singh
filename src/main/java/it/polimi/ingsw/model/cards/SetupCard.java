package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.tiles.IslandTile;

//This class of cards has a resource counter on it, thus needing to be initialized at the beginning of the game. CharacterIDs range from 1 to 4
public class SetupCard extends CharacterCard{

    //This is the only setup card that has no students on it, therefore an integer is sufficent for resource-tracking purposes.
    private int resourceOnCard;
    int id= this.getCharacterID();
    @Override
    public void callPower(Player playercaller, int id) {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);

        System.out.println("Choose an island to block off!\n");
        //Player inputs a valid number

    }
}




