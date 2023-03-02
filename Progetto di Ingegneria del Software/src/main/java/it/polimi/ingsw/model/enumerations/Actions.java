package it.polimi.ingsw.model.enumerations;

import java.io.Serializable;

/**
 * Enumeration that lists the 12 different Character Card powers.
 */
public enum Actions implements Serializable {
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