package it.polimi.ingsw.network.server.stripped;

import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.Requirements;
import it.polimi.ingsw.model.cards.charactercard.Type;
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

    /**
     * Constructor metohd for the Stripped Character class. Uses a full Character Card in Room.
     * @param card the Character card this Stripped Character represents.
     */
    public StrippedCharacter(CharacterCard card) {
        this.price = card.getPrice();
        this.description = card.getDescription();
        this.type = card.getType();
        this.requirements = card.getRequirements();
        this.characterID = Integer.parseInt(card.getImageName());

        //If there are no students this should just return null
        setStudents(card.getStudents());
        //If there are no tiles this should just put 0
        setNoEntryTiles(card.getNoTileNumber());
    }

    /**
     * Getter method for ID field.
     * @return this Stripped Character's ID.
     */
    public int getCharacterID() {
        return characterID;
    }

    /**
     * Setter method for the ID field. Only accessed at character creation.
     * @param characterID the character ID of this card.
     */
    public void setCharacterID(int characterID) {
        this.characterID = characterID;
    }

    /**
     * Getter method for price field.
     * @return int price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Setter method for price field.
     * @param price the updated price of the card.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Getter method for the card's description.
     * @return the card's description (string)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the Type field.
     * @return the card's Type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Getter for the Requirements field.
     * @return the requirement needed for character activation.
     */
    public Requirements getRequirements() {
        return requirements;
    }

    public boolean sameCard(StrippedCharacter other) {
        return this.characterID == other.getCharacterID();
    }

    /**
     *Getter for the NoEntryTiles field.
     * @return no entry tiles (int)
     */
    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    /**
     *Setter for the NoEntryTiles field.
     * @param noEntryTiles the updated number of no Entry tiles on the card.
     */
    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }

    /**
     *Getter for the Students field.
     * @return the students on the card (if any)
     */
    public EnumMap<Colors, Integer> getStudents() {
        return students;
    }

    /**
     *Setter for the Students field.
     * @param students the students the card needs to be set to.
     */
    public void setStudents(EnumMap<Colors, Integer> students) {
        this.students = students;
    }
}
