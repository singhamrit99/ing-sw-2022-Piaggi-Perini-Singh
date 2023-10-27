package it.polimi.ingsw.model.cards;

import java.io.Serializable;

public abstract class Card implements Serializable {
    private String imageName;

    /**
     * Constuctor of the Card class, with field imageName that is then inherited by all cards classes.
     *
     * @param imageName the name of the card image file.
     */
    public Card(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Getter method for imageName.
     *
     * @return imageName.
     */
    public String getImageName() {
        return imageName;
    }
}
