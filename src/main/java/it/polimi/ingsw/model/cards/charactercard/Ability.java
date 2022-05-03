package it.polimi.ingsw.model.cards.charactercard;

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

    public enum Actions {
        ADD_ISLAND,
        TAKE_PROFESSORS,
        CALCULATE_INFLUENCE,
        MOVE_MOTHER_NATURE,
        NO_ENTRY_TILE,
        TOWER_INFLUENCE,
        SWAP_ENTRANCE,
        ADD_POINTS,
        AVOID_COLOR_INFLUENCE,
        SWAP_ENTRANCE_DINING,
        ADD_DINING,
        RETURN_STUDENT
    }


    public Actions getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }
}