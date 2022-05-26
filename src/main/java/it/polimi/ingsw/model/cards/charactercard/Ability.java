package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.enumerations.Actions;

/**
 * @author Amrit
 */
public class Ability {
    private Actions action;
    private int value;

    public Ability(Actions action, int value) {
        this.action = action;
        this.value = value;
    }

    public Actions getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }
}