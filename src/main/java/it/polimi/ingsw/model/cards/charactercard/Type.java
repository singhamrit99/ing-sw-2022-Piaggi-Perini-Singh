package it.polimi.ingsw.model.cards.charactercard;

import it.polimi.ingsw.model.enumerations.Types;

import java.io.Serializable;

public class Type implements Serializable {
    private Types name;
    private int value;

    /**
     * Type class constructor for Character classes.
     * @param name Name of the type
     * @param value Initial value if required.
     */
    public Type(Types name, int value){
        this.name = name;
        this.value = value;
    }

    /**
     * Name getter method.
     * @return name
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Value getter method
     * @return value
     */
    public int getValue(){
        return value;
    }
}