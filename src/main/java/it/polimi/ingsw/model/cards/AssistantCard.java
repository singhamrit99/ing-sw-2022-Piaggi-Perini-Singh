package it.polimi.ingsw.model.cards;

/*The Assistant Card is the only card that can be played. It is defined mechanically by an integer 1 through 10 and a
 * number of tiles Mother Nature can move, dependant on the card. There are 4 copies of each card, the only difference being the wizard
 * depicted on the back.*/
public class AssistantCard extends Card {

    private int value;
    private int move;
    private String wizard;
    private boolean hasPlayed;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

    }

    public AssistantCard(int value, int move, String wizard, boolean hasPlayed) {
        this.value = value;
        this.move = move;
        this.wizard = wizard;
        this.hasPlayed = hasPlayed;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public String getWizard() {
        return wizard;
    }

    public void setWizard(String wizard) {
        this.wizard = wizard;
    }

    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }


}
