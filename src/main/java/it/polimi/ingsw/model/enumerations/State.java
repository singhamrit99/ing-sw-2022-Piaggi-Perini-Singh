package it.polimi.ingsw.model.enumerations;

/**
 * Enum that contains the various phases of the player turn.
 */
public enum State {
    PLANNINGPHASE,
    ACTIONPHASE_1, //moving 3 students
    ACTIONPHASE_2, //moving Mother Nature
    ACTIONPHASE_3, //choosing a Cloud Tile
    ENDTURN,
    END
}
