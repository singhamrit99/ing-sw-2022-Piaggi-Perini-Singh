package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudentDisc;

import java.util.HashMap;

//This class of cards has a resource counter on it, thus needing to be initialized at the beginning of the game. CharacterIDs range from 1 to 4
public class StudentSetupCard extends CharacterCard{

    private HashMap<StudentDisc, Integer> studentsonCard = new HashMap<>();
    int id= this.getCharacterID();
    @Override
    public void callPower(Player playercallerint, int id){

        int newprice= this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

        switch (id) {

            case 1:
                CardStudentToIsle();
                break;
            case 2:
                ExchangeStudents();
                break;
            case 3:
                CardStudentToDining();
                break;
        }




    }

    public void ExchangeStudents() {
    }

    public void NegateEffect() {
    }

    public void CardStudentToDining() {
    }

    public void CardStudentToIsle() {
        System.out.println("Choose the student to swap from your card!\n");
        //Player picks student

    }


}
