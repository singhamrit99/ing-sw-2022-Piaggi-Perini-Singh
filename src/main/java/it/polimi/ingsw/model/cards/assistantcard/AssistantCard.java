package it.polimi.ingsw.model.cards.assistantcard;

import it.polimi.ingsw.model.cards.Card;

/**
 * @author Amrit
 */
public class AssistantCard extends Card {
    private final int move;
    private boolean hasPlayed;

    public AssistantCard(String imageName, int move) {
        super(imageName);
        this.move = move;
        this.hasPlayed = false;
    }

    public int getMove() {
        return move;
    }

    public boolean getHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }
}
