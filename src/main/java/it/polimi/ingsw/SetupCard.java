package it.polimi.ingsw;
//This class of cards has a resource counter on it, thus needing to be initialized at the beginning of the game. CharacterIDs range from 1 to 4
public class SetupCard extends CharacterCard{

    private int resourceOnCard;
    int id= this.getCharacterID();
    @Override
    public void callPower(){

        int newprice= this.getPrice();
        newprice++;
        this.setPrice(newprice);
        //Updating the price of the ability, which always happens.

        switch (id) {

            case 1:
                CardStudentToIsle();
                break;

            case 2:
                NegateEffect();
                break;
            case 3:
                ExchangeStudents();
                break;
            case 4:
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
    }


}
