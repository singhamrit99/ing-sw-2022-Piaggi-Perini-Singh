package it.polimi.ingsw;

import java.io.Serializable;

/**
 * @author Amrit
 */
public enum Colors implements Serializable {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4),
    BLACK(5),
    GREY(6),
    WHITE(7);

    private final int index;

    Colors(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Colors getColor(int index) throws IncorrectArgumentException {
        for(Colors color: values()){
            if(color.getIndex() == index) return color;
        }
        throw new IncorrectArgumentException("Index not associated with any color");
    }
}
