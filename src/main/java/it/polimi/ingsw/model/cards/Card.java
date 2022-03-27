package it.polimi.ingsw.model.cards;

//This is the least specific Card in the game, as it refers both to Assistant Cards and Character Chards.
public abstract class Card {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
