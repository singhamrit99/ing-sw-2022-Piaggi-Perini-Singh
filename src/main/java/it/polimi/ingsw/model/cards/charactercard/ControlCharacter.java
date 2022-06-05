package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.ProfessorNotFoundException;
import it.polimi.ingsw.model.enumerations.Actions;

import java.io.Serializable;

/**
 * @author Amrit
 */
public class ControlCharacter extends CharacterCard implements Serializable {
    public ControlCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);
    }

    @Override
    public void activate(Game game) throws ProfessorNotFoundException {
        Actions action = super.getAbility().getAction();
        if (action.equals(Actions.TAKE_PROFESSORS)) {
            game.checkAndPlaceProfessor();
        }
    }
}
