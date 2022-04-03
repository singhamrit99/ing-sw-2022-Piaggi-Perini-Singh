package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;

/*This is the most general character card possible, and acts as foundation for all the others.
I think it makes sense to divide the cards in numerically ascending order based on their powers.
Setup Cards: 1 through 4
Influence Cards: 5 through 9
Token Manipulation Cards:10 through 12
Mother Nature Manipulation Card: 13
*/
public class CharacterCard extends Card{

    private int characterID;

    private int startingPrice;

    private int price;

    private String power;

    public CharacterCard(int characterID, int startingPrice, String powerDescription) {
        this.characterID = characterID;
        this.startingPrice = startingPrice;
        this.price = startingPrice;
        this.power = powerDescription;
    }


    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    //Puppet method that gets overridden in each specific card class.
    public void callPower(Player playercaller, int id) {



    }



    public String getPowerDescription() {
        return power;
    }

    public void setPowerDescription(String powerDescription) {
        this.power = powerDescription;
    }
}
