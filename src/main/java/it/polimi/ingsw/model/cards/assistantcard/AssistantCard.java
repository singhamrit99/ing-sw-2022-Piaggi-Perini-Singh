package it.polimi.ingsw.model.cards.assistantcard;

import it.polimi.ingsw.model.cards.Card;

/**
 * @author Amrit
 */
public class AssistantCard extends Card {
    private final int move;

    public AssistantCard(String imageName, int move) {
        super(imageName);
        this.move = move;
    }

    public int getMove() {
        return move;
    }

}
