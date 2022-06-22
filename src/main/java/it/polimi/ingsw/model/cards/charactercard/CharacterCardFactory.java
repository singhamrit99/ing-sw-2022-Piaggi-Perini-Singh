package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.exceptions.NegativeValueException;
import it.polimi.ingsw.model.enumerations.Types;

import java.io.Serializable;

/**
 * @author Amrit
 */
public class CharacterCardFactory implements Serializable {
    /**
     * Main character card builder method
     * @param imageName Name of the image from the JSON, used for both displaying and identification purposes.
     * @param startingPrice The starting price of the character card.
     * @param description The description of the character card.
     * @param type Character card type.
     * @param ability Ability description.
     * @param requirements Character power activation requirements.
     * @return New character card of the correct subtype (Student, Control or Selector Characters)
     * @throws IncorrectArgumentException If any of the values are not valid to build a character card this is thrown.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public CharacterCard getCard(String imageName, int startingPrice, String description, Type type, Ability ability, Requirements requirements) throws IncorrectArgumentException, NegativeValueException {
        if (type.getName() == null) {
            return null;
        } else if (type.getName().equalsIgnoreCase(Types.STUDENT.toString())) {
            return new StudentCharacter(imageName, startingPrice, description, type, ability, requirements);
        } else if (type.getName().equalsIgnoreCase(Types.CONTROL.toString())) {
            return new ControlCharacter(imageName, startingPrice, description, type, ability, requirements);
        } else if (type.getName().equalsIgnoreCase(Types.SELECTOR.toString())) {
            return new SelectorCharacter(imageName, startingPrice, description, type, ability, requirements);
        }

        return null;
    }
}
