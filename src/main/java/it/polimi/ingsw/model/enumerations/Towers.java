package it.polimi.ingsw.model.enumerations;

import java.io.Serializable;

/**
 * @author Amrit
 * Enumeration that lists the three possible Tower colors (Black and White for 2 and 4 players with the addition of Grey for 3 player mode).
 */
public enum Towers implements Serializable {
    BLACK(1),
    GREY(2),
    WHITE(3);

    private final int index;

    Towers(int index) {
        this.index = index;
    }
    /*public int getIndex() {
        return index;
    }*/
}
