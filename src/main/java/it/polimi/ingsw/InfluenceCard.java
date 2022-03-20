package it.polimi.ingsw;

import java.awt.*;

//This class of cards' power tampers in some way with the Influence calculations. CharacterIDs range from 5 to 8
public class InfluenceCard extends CharacterCard{

    int id= this.getCharacterID();
    @Override
    public void callPower(){

        int newprice= this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

        switch (id) {

            case 5:
                IgnoreTowers();
                break;

            case 6:
                AddTwo();
                break;
            case 7:
                //TODO: Add way for user to input valid color
                Colors color= Colors.GREEN;
                System.out.println("Choose the color to ignore in the calculation");

                IgnoreStudent(color);
                break;
            case 8:
                CallOnIsland();

                break;
        }


    }


    public void IgnoreTowers() {

    }

    public void AddTwo() {

    }

    public void IgnoreStudent(Colors color){

    }
    //TODO: Add a way to select a specific island to call
    public void CallOnIsland(){

    }
}


