package it.polimi.ingsw.model.cards.charactercard;

public class Type {
    private Types name;
    private int value;

    public Type(Types name, int value){
        this.name = name;
        this.value = value;
    }

    public enum Types {
        STUDENT, CONTROL, SELECTOR
    }

    public String getName() {
        return name.toString();
    }

    public int getValue(){
        return value;
    }
}