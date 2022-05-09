package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.cards.charactercard.Ability;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.Requirements;
import it.polimi.ingsw.model.cards.charactercard.Type;

public class StrippedCharacter {

    private int price;
    private String description;
    private Type type;
    private Requirements requirements;

    public StrippedCharacter(int price, String description, Type type, Requirements requirements) {
        this.price = price;
        this.description = description;
        this.type = type;
        this.requirements = requirements;
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

    public Requirements getRequirements() {
        return requirements;
    }

}
