package it.polimi.ingsw.model.cards.assistantcard;

import it.polimi.ingsw.model.cards.Card;

import java.io.Serializable;

/**
 * @author Amrit
 */
public class AssistantCard extends Card implements Serializable{
    private final int move;

    public AssistantCard(String imageName, int move) {
        super(imageName);
        this.move = move;
    }

    public int getMove() {
        return move;
    }

}
