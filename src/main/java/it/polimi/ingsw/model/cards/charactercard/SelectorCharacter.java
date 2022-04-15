package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;

public class SelectorCharacter extends CharacterCard {
    public SelectorCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);
    }

    @Override
    public void activate(Game game) {

    }
}
