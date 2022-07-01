package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.exceptions.FullDiningException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;

import java.util.EnumMap;

/**
 * Basic character card, which then gets implemented in the specific character card types.
 */
public class CharacterCard extends Card {
    private int status;
    private int price;
    private String description;
    private Type type;
    private Ability ability;
    private Requirements requirements;

    /**
     * Character card builder.
     *
     * @param price       The price of the character card.
     * @param description The description of the character.
     */
    public CharacterCard(int price, String description) {
        this("", price, description);
    }

    /**
     * Character card constructor used for updates
     *
     * @param imageName   Pulled from super Card class, used for both display and identification purposes.
     * @param price       The current character card price.
     * @param description The card's description.
     */
    public CharacterCard(String imageName, int price, String description) {
        super(imageName);
        this.price = price;
        this.description = description;
        status = 0;
    }

    /**
     * Most complete character card constructor. All of these values are pulled from the JSON file.
     *
     * @param imageName     Used for both display and identification purposes.
     * @param startingPrice The specific starting character price.
     * @param description   The character's power description.
     * @param type          The character type, utilized for power activation.
     * @param ability       The ability of the specific character.
     * @param requirements  The resources needed to activate the character card.
     */
    public CharacterCard(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        this(imageName, startingPrice, description);
        this.type = type;
        this.ability = ability;
        this.requirements = requirements;
    }

    /**
     * ImageName field getter method. Inherited from Card class.
     *
     * @return ImageName
     */
    public String getImageName() {
        return super.getImageName();
    }

    /**
     * Price field getter method
     *
     * @return price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Description field getter method
     *
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Type field getter method
     *
     * @return Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Ability field getter method
     *
     * @return Ability
     */
    public Ability getAbility() {
        return ability;
    }

    /**
     * Requirements field getter method.
     *
     * @return Requirements
     */
    public Requirements getRequirements() {
        return requirements;
    }

    /**
     * Method to be overridden in each different character card.
     *
     * @param game The game in which the card is being activated
     * @throws ProfessorNotFoundException If the character card can cause a professor change this is thrown in case of an error.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown in case of invalid values in Character cards implementations.
     */
    public void activate(Game game) throws ProfessorNotFoundException, NegativeValueException, IncorrectArgumentException, FullDiningException {
        status = 1;
    }

    /**
     * Returns status.
     *
     * @return status, whether the card is in play or not
     */
    public int getStatus() {
        return status;
    }

    /**
     * Setter for the status parameter
     *
     * @param status sets card status (in play or not)
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Setter for choiceIndex parameter
     *
     * @param choiceIndex parameter used in selector characters.
     */
    public void setChoiceIndex(int choiceIndex) {
    }

    public int getChoiceIndex() {
        return -1;
    }

    /**
     * Increases character price after activation.
     */
    public void increasePrice() {
        price++;
    }

    /**
     * Method used to set enums for StudentCharacters cards.
     *
     * @param students1 first student enum.
     * @param students2 second student enum.
     */
    public void setEnums(EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) {
    }

    /**
     * Method to be overridden in specific Character Cards.
     *
     * @param student The student, passed as an int (Color)
     * @param island  The island, passed as an int (number)
     */
    public void setChoices(int student, int island) {
    }

    /**
     * Method to be overridden in specific Character Cards
     *
     * @return students in overridden cards.
     */
    public EnumMap<Colors, Integer> getStudents() {
        return null;
    }

    public int getNoTileNumber() {
        return 0;
    }

    public void incrementNoTileNumber() {
    }

    public void decrementNoTileNumber() {
    }

}
