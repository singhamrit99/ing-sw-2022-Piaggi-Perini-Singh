package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.Requirements;
import it.polimi.ingsw.model.cards.charactercard.Type;
import it.polimi.ingsw.model.enumerations.Actions;
import it.polimi.ingsw.model.enumerations.Colors;

import java.io.Serializable;
import java.util.EnumMap;

public class StrippedCharacter implements Serializable {
    private int price;
    final private String description;
    final private Type type;
    final private Requirements requirements;
    private int characterID;
    private int noEntryTiles;
    private EnumMap<Colors, Integer> students;

    public StrippedCharacter(CharacterCard card) {
        this.price = card.getPrice();
        this.description = card.getDescription();
        this.type = card.getType();
        this.requirements = card.getRequirements();
        this.characterID = Integer.parseInt(card.getImageName());

        //If there are no students this should just return null
        setStudents(card.getStudents());
        //If this card is the right one we get no entry tiles, otherwise we don't
        if (card.getAbility().getAction().equals(Actions.NO_ENTRY_TILE))
            setNoEntryTiles(4);
        else
            setNoEntryTiles(0);
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
        if (this.characterID==other.getCharacterID()) return true;
        return description.equals(other.description) && type.getName().equals(other.type.getName())
                && requirements.equals(other.getRequirements());
    }

    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
