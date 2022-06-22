package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.enumerations.Actions;

/**
 * @author Amrit
 */
public class Ability {
    private Actions action;
    private int value;

    /**
     * Constructor for the Ability class, used in Character Cards creation.
     * @param action The action of the singular card.
     * @param value An integer value attached to it, such as students or No Entry cards.
     */
    public Ability(Actions action, int value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Returns Action field.
     * @return action field from Ability class.
     */
    public Actions getAction() {
        if (action == null) {
            return null;
        }
        return action;
    }

    /**
     * Returns Value field.
     * @return value vield from Ability class.
     */
    public int getValue() {
        return value;
    }
}