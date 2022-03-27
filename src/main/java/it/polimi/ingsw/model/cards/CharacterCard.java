package it.polimi.ingsw.model.cards;

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
    public void callPower() {}


}
