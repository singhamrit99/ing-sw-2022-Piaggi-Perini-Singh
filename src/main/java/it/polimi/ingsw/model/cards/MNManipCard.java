package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;

//This class of cards' power modifies Mother Nature's behaviour. CharacterID is 13, as it is one lone card.
public class MNManipCard extends CharacterCard {

    public MNManipCard(int characterID, int startingPrice, int price, String powerDescription) {
        super(characterID, startingPrice, price, powerDescription);
    }

    @Override
    public void callPower(Player playercaller, int id) {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.
        AddMovement(playercaller, id);
    }

    public void AddMovement(Player playercaller, int id) {
        System.out.println((this.getPowerDescription()));
        AssistantCard cardPlayed;
        cardPlayed = playercaller.getPlayedCard();

    }
}