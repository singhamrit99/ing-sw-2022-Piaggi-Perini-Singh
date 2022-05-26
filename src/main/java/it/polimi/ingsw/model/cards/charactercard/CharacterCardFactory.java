package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.model.enumerations.Types;

import java.io.Serializable;

/**
 * @author Amrit
 */
public class CharacterCardFactory implements Serializable {
    public CharacterCard getCard(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) throws IncorrectArgumentException, NegativeValueException {
        if (type.getName() == null) {
            return null;
        }
        if (type.getName().equalsIgnoreCase(Types.STUDENT.toString())) {
            return new StudentCharacter(imageName, startingPrice, description, type, ability, requirements);
        } else if (type.getName().equalsIgnoreCase(Types.CONTROL.toString())) {
            return new ControlCharacter(imageName, startingPrice, description, type, ability, requirements);

        } else if (type.getName().equalsIgnoreCase(Types.SELECTOR.toString())) {
            return new SelectorCharacter(imageName, startingPrice, description, type, ability, requirements);
        }

        return null;
    }
}
