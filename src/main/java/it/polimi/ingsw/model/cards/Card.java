package it.polimi.ingsw.model.cards;

import java.io.Serializable;

public abstract class Card implements Serializable {
    private String imageName;

    public Card(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
