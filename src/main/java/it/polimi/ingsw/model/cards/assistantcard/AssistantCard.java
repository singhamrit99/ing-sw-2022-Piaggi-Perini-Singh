package it.polimi.ingsw.model.cards.assistantcard;

import it.polimi.ingsw.model.cards.Card;

/*The Assistant Card is the only card that can be played. It is defined mechanically by an integer 1 through 10 and a
 * number of tiles Mother Nature can move, dependant on the card. There are 4 copies of each card, the only difference being the wizard
 * depicted on the back.*/

/**
 * @author Amrit 
 */
public class AssistantCard extends Card {
    private int value;
    private int move;
    private String wizard;
    private boolean hasPlayed;

    public AssistantCard(String imageName, int value, int move) {
        super(imageName);
        this.value = value;
        this.move = move;
        this.wizard = "";
        this.hasPlayed = false;
    }

    public AssistantCard(int value, int move, String wizard) {
        this("", value, move);
    }

    public int getValue() {
        return value;
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
