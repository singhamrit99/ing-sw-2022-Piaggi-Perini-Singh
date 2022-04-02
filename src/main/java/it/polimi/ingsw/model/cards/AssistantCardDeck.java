package it.polimi.ingsw.model.cards;

import java.util.ArrayList;

//Each Player has to have a deck of Assistant Cards, numbered 1 thorough 10.
public class AssistantCardDeck {

    private ArrayList<AssistantCard> assistantCards;
    private AssistantCard AssistantCard;

    public AssistantCard getAssistantCard(int index) {
        int lenght=assistantCards.size();
        int actualposition=0;
        for( int i=0; i<lenght; i++)
        {
            if (assistantCards.get(i).getValue()==index)
            {
                actualposition=i;
            }
        }
        System.out.println("I am playing the card number " + assistantCards.get(actualposition).getValue());

        AssistantCard = assistantCards.get(actualposition);
        assistantCards.remove(actualposition);


        return AssistantCard;


    }


    public void setAssistantCards(ArrayList<it.polimi.ingsw.model.cards.AssistantCard> assistantCards) {
        this.assistantCards = assistantCards;
    }
}
