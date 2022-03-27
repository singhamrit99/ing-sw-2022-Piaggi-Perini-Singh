package it.polimi.ingsw.model.cards;
//This class of cards' power modifies Mother Nature's behaviour. CharacterID is 13, as it is one lone card.
public class MNManipCard extends CharacterCard{


    @Override
    public void callPower() {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.
        AddMovement();
    }

    public void AddMovement(){

    }
}