package it.polimi.ingsw.model.cards.charactercard;

public class Requirements {
    private Resources resource;
    private int value;

    public Requirements(Resources resource, int value) {
        this.resource = resource;
        this.value = value;
    }

    public enum Resources {
        COLORS, ISLANDS;
    }

    public String getRequirements() {
        return resource.toString();
    }

    public int getValue() {
        return value;
    }
}