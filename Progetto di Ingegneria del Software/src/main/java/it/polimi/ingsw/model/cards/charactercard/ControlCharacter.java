package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;
import it.polimi.ingsw.model.enumerations.Actions;

import java.io.Serializable;

/**
 * @author Amrit
 */
public class ControlCharacter extends CharacterCard implements Serializable {
    /**
     * ControlCharacter constructor
     * @param imageName Taken from father CharacterCard in CharacterCardFactory.
     * @param startingPrice Taken from father CharacterCard in CharacterCardFactory.
     * @param description Taken from father CharacterCard in CharacterCardFactory.
     * @param type Taken from father CharacterCard in CharacterCardFactory.
     * @param ability Taken from father CharacterCard in CharacterCardFactory.
     * @param requirements Taken from father CharacterCard in CharacterCardFactory.
     */
    public ControlCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);
    }

    /**
     * Overriding of activate method for TAKE_PROFESSORS card.
     * @param game The game in which the card is being activated.
     * @throws ProfessorNotFoundException Thrown in case of professor error.
     */
    @Override
    public void activate(Game game) throws ProfessorNotFoundException {
        Actions action = super.getAbility().getAction();
        if (action.equals(Actions.TAKE_PROFESSORS)) {
            game.checkAndPlaceProfessor();
        }
    }
}
