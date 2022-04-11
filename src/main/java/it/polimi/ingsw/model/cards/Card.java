package it.polimi.ingsw.model.cards;

public abstract class Card {
    private String imageName;

    public Card(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
