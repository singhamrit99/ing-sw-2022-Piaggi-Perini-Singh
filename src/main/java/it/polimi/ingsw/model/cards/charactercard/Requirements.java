package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.enumerations.Resources;

import java.io.Serializable;

public class Requirements implements Serializable {
    private Resources resource;
    private int value;

    public Requirements() {
        resource = null;
        value = -1;
    }

    /**
     * Requirements constructor, created from a Resource and a related value (such as number of students).
     * @param resource Resource field.
     * @param value Value field.
     */
    public Requirements(Resources resource, int value) {
        this.resource = resource;
        this.value = value;
    }

    /**
     * Requirements getter method, returns null if there are no requirements.
     * @return Requirements.
     */
    public String getRequirements() {
        if (resource == null) return null;
        return resource.toString();
    }

    /**
     * Value field getter.
     * @return value.
     */
    public int getValue() {
        return value;
    }
}