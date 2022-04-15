package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;

public class CharacterCard extends Card {
    private final int startingPrice;
    private int price;
    private String description;
    private Type type;
    private Ability ability;
    private Requirements requirements;

    public CharacterCard(int startingPrice, String description) {
        this("", startingPrice, description);
    }

    public CharacterCard(String imageName, int startingPrice, String description) {
        super(imageName);
        this.startingPrice = startingPrice;
        this.description = description;

        this.price = startingPrice;
    }

    public CharacterCard(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        this(imageName, startingPrice, description);

        this.type = type;
        this.ability = ability;
        this.requirements = requirements;
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

    public Type getType() {
        return type;
    }

    public Ability getAbility() {
        return ability;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public void activate(Game game) {
    }
}
