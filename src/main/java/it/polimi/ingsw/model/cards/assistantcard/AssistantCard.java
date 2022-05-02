package it.polimi.ingsw.model.cards.assistantcard;

import it.polimi.ingsw.model.cards.Card;

/**
 * @author Amrit
 */
public class AssistantCard extends Card {
    private int move;
    private String wizard;
    private boolean hasPlayed;

    public AssistantCard(String imageName, int move) {
        super(imageName);
        this.move = move;
        this.wizard = "";
        this.hasPlayed = false;
    }

    public AssistantCard(int value, int move, String wizard) {
        this("", move);
    }

    public int getMove() {
        return move;
    }

    public String getWizard() {
        return wizard;
    }

    public void setWizard(String wizard) {
        this.wizard = wizard;
    }

    public boolean getHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }
}
