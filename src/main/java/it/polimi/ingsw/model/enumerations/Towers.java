package it.polimi.ingsw.model.enumerations;

import java.io.Serializable;

/**
 * @author Amrit
 */
public enum Towers implements Serializable {
    BLACK(1),
    GREY(2),
    WHITE(3);

    private final int index;

    Towers(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
