package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.NegativeValueException;

public class SelectorCharacter extends CharacterCard {
    private int noTileNumber;
    private int choiceIndex;

    public SelectorCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);

        if (this.getAbility().getAction().equals(Ability.Actions.NO_ENTRY_TILE)) {
            noTileNumber = 4;
        } else {
            noTileNumber = -1;
        }
    }

    @Override
    public void activate(Game game) throws NegativeValueException {
        Ability.Actions action = this.getAbility().getAction();
        switch (action) {
            case CALCULATE_INFLUENCE:
                game.resolveMotherNature(choiceIndex);
                break;
            case NO_ENTRY_TILE:
                game.getIsland(choiceIndex).setHasNoEntryTile(true);
                setStatus(2);
                break;
            case RETURN_STUDENT:
                game.returnStudentsEffect(choiceIndex);
                break;
            default:
                break;
        }
    }

    public void setChoiceIndex(int choiceIndex) {
        this.choiceIndex = choiceIndex;
    }
}
