package it.polimi.ingsw.model.cards.charactercard;

import java.io.Serializable;

public class Requirements implements Serializable {
    private Resources resource;
    private int value;

    public Requirements() {
        resource = null;
        value = -1;
    }

    public Requirements(Resources resource, int value) {
        this.resource = resource;
        this.value = value;
    }

    public enum Resources {
        COLORS, ISLANDS
    }

    public String getRequirements() {
        return resource.toString();
    }

    public int getValue() {
        return value;
    }
}