package it.polimi.ingsw.model.cards.charactercard;

import java.io.Serializable;

public class Type implements Serializable {
    private Types name;
    private int value;

    public Type(Types name, int value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name.toString();
    }

    public int getValue(){
        return value;
    }
}