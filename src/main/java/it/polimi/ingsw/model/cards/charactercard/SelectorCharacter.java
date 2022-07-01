package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.model.enumerations.Actions;

import java.io.Serializable;

public class SelectorCharacter extends CharacterCard implements Serializable {
    private int noTileNumber;
    private int choiceIndex;

    /**
     * SelectorCharacter constructor
     *
     * @param imageName     Taken from father CharacterCard in CharacterCardFactory.
     * @param startingPrice Taken from father CharacterCard in CharacterCardFactory.
     * @param description   Taken from father CharacterCard in CharacterCardFactory.
     * @param type          Taken from father CharacterCard in CharacterCardFactory.
     * @param ability       Taken from father CharacterCard in CharacterCardFactory.
     * @param requirements  Taken from father CharacterCard in CharacterCardFactory.
     */
    public SelectorCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);

        if (this.getAbility().getAction().equals(Actions.NO_ENTRY_TILE)) {
            noTileNumber = 4;
        } else {
            noTileNumber = -1;
        }
    }

    /**
     * Activate override in Selector Character
     *
     * @param game The game in which the card is being activated
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException Thrown if the professor assignment method fails to find the related professor, either because of a color
     *                                    mismatch or other internal error.
     */
    @Override
    public void activate(Game game) throws NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException {
        Actions action = this.getAbility().getAction();
        switch (action) {
            case CALCULATE_INFLUENCE:
                game.resolveMotherNature(choiceIndex);
                setStatus(2);
                break;
            case NO_ENTRY_TILE:
                game.setNoEntryTile(choiceIndex);
                break;
            case RETURN_STUDENT:
                game.returnStudentsEffect(choiceIndex);
                setStatus(2);
                break;
            default:
                break;
        }
    }

    /**
     * Choice index setter method.
     *
     * @param choiceIndex parameter used in selector characters.
     */
    public void setChoiceIndex(int choiceIndex) {
        this.choiceIndex = choiceIndex;
    }

    /**
     * Getter for No Entry Tiles value.
     *
     * @return No Entry Tiles left on the card.
     */
    public int getNoTileNumber() {
        return noTileNumber;
    }

    public void incrementNoTileNumber() {
        noTileNumber++;
    }

    public void decrementNoTileNumber() {
        noTileNumber--;
    }

    public int getChoiceIndex() {
        return choiceIndex;
    }
}
