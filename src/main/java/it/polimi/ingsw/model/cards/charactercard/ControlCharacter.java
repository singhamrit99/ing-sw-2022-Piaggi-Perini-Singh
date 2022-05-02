package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.ProfessorNotFoundException;

public class ControlCharacter extends CharacterCard {
    public ControlCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);
    }

    @Override
    public void activate(Game game) throws ProfessorNotFoundException {
        Ability.Actions action = this.getAbility().getAction();

        if (action.equals(Ability.Actions.TAKE_PROFESSORS)) {
            game.checkAndPlaceProfessor();
        }
    }
}
