package it.polimi.ingsw.model.stripped;

import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.Requirements;
import it.polimi.ingsw.model.cards.charactercard.Type;

import java.util.Objects;

public class StrippedCharacter {
    private int price;
    final private String description;
    final private Type type;
    final private Requirements requirements;
    private int characterID;
    public StrippedCharacter(CharacterCard card) {
        this.price = card.getPrice();
        this.description = card.getDescription();
        this.type = card.getType();
        this.requirements = card.getRequirements();
    }

    public int getCharacterID() {
        return characterID;
    }

    public void setCharacterID(int characterID) {
        this.characterID = characterID;
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

    public boolean sameCard(StrippedCharacter other) {
        if (this == other) return true;
        return description.equals(other.description) && type.getName().equals(other.type.getName())
                && requirements.equals(other.getRequirements());
    }

}
