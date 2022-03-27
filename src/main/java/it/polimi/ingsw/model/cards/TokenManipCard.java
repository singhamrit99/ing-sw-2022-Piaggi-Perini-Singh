package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumerations.Colors;

//This class of cards' power manipulates student tokens and professor behaviour connected to them. CharacterIDs range from 9 to 11
public class TokenManipCard extends CharacterCard {


    int id = this.getCharacterID();

    @Override
    public void callPower() {

        int newprice = this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

        switch (id) {
            case 9:
                TieBreaker();
                break;
            case 10:
                SwitchOwnStudents();
                break;
            case 11:
                //TODO: Add way for user to input valid color
                Colors color= Colors.GREEN;
                System.out.println("Choose the color of student to discard!");
                DiscardOpponentStudents(color);
                break;
        }
    }

    public void DiscardOpponentStudents(Colors color) {
    }

    public void SwitchOwnStudents() {
    }

    public void TieBreaker() {
    }
}
