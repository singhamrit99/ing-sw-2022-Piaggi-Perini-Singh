package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.tiles.IslandTile;

//This class of cards has a resource counter on it, thus needing to be initialized at the beginning of the game. CharacterID is 1.
public class SetupCard extends CharacterCard {

    //This is the only setup card that has no students on it, therefore an integer is sufficent for resource-tracking purposes.
    //Every power call starts with a brief description of the power itself, the same the player saw when choosing it

    public SetupCard(int characterID, int startingPrice, int price, String powerDescription, int resourceOnCard, int id) {
        super(characterID, startingPrice, price, powerDescription);
        this.resourceOnCard = resourceOnCard;
        this.id = id;
    }

    private int resourceOnCard;
    int id = this.getCharacterID();

    @Override
    public void callPower(Player playercaller, int id) {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);

        System.out.println((this.getPowerDescription()));
        //Player inputs a valid number

    }
}




