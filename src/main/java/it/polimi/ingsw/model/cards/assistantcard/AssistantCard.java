package it.polimi.ingsw.model.cards.assistantcard;

import it.polimi.ingsw.model.cards.Card;

import java.io.Serializable;

/**
 * @author Amrit
 */
public class AssistantCard extends Card implements Serializable{
    private final int move;

    /**
     * Constructs an assistantCard given an imageName and moves loaded from the AssistantCards json file.
     * @param imageName The name of the image relative to this Assistant Card. Used both for image fetching purposes and game purposes (choosing your card)
     * @param move The number of moves associated with this Assistant Card.
     */
    public AssistantCard(String imageName, int move) {
        super(imageName);
        this.move = move;
    }

    /**
     * Move getter
     * @return move
     */
    public int getMove() {
        return move;
    }
}
