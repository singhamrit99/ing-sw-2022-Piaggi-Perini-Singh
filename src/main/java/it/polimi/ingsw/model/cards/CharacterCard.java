package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

public class CharacterCard extends Card {

    private int characterID;

    private int startingPrice;

    private int price;

    private String power;

    private String description;


    public CharacterCard(int characterID, int startingPrice, String powertype, String powerDescription) {
        this.characterID = characterID;
        this.startingPrice = startingPrice;
        this.price = startingPrice;
        this.power = powertype;
        this.description= powerDescription;

    }


    public int getCharacterID() {
        return characterID;
    }




    public int getStartingPrice() {
        return startingPrice;
    }



    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPower() {
        return power;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /*public void callPower(Player playercaller, int id) throws IncorrectArgumentException {


    }*/


}
