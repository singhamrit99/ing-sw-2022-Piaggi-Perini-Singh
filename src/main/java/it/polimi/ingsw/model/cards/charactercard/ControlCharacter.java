package it.polimi.ingsw.model.cards.charactercard;

public class ControlCharacter extends CharacterCard {
    public ControlCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);
    }

    @Override
    public void activate() {

    }
}
