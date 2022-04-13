package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.cards.Card;

public class CharacterCard extends Card {
    private int startingPrice;
    private int price;
    private String type;
    private String description;

    public CharacterCard(String imageName, int startingPrice, String description) {
        super(imageName);
        this.startingPrice = startingPrice;
        this.description = description;

        this.price = startingPrice;
    }

    public CharacterCard(int startingPrice, String description) {
        this("", startingPrice, description);
    }

    public String getImageName() {
        return super.getImageName();
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
}
