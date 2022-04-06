package it.polimi.ingsw.model.cards;

//This is the least specific Card in the game.
public abstract class Card {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
