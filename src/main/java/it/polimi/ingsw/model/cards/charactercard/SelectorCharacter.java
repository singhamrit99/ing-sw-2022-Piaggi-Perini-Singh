package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.exceptions.ProfessorNotFoundException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.model.enumerations.Actions;

import java.io.Serializable;

public class SelectorCharacter extends CharacterCard implements Serializable {
    private int noTileNumber;
    private int choiceIndex;

    public SelectorCharacter(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) {
        super(imageName, startingPrice, description, type, ability, requirements);

        if (this.getAbility().getAction().equals(Actions.NO_ENTRY_TILE)) {
            noTileNumber = 4;
        } else {
            noTileNumber = -1;
        }
    }

    @Override
    public void activate(Game game) throws NegativeValueException, ProfessorNotFoundException {
        Actions action = this.getAbility().getAction();
        switch (action) {
            case CALCULATE_INFLUENCE:
                game.resolveMotherNature(choiceIndex);
                break;
            case NO_ENTRY_TILE:
                if (!game.getIsland(choiceIndex).hasNoEntryTile()) {
                    game.getIsland(choiceIndex).setHasNoEntryTile(true);
                    setStatus(2);
                }
                setStatus(0);
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
