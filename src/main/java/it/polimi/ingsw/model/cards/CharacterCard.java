package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

public class CharacterCard extends Card {

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


    /*public void callPower(Player playercaller, int id) throws IncorrectArgumentException {


    }*/


}
