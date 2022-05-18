package it.polimi.ingsw.model.deck.assistantcard;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exceptions.AssistantCardNotFound;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckGenerator;
import it.polimi.ingsw.model.deck.FileJSONPath;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Amrit
 */
public class AssistantCardDeck implements Deck<AssistantCard> {
    private ArrayList<AssistantCard> assistantCards;

    public AssistantCardDeck() {
        assistantCards = new ArrayList<>();
    }

    public ArrayList<AssistantCard> getDeck() {
        return assistantCards;
    }

    @Override
    public void fillDeck() {
        Type collectionType = new TypeToken<ArrayList<AssistantCard>>() {
        }.getType();
        DeckGenerator<AssistantCard> deckGenerator = new DeckGenerator<>(FileJSONPath.ASSISTANT_CARDS_JSON, collectionType);
        assistantCards = deckGenerator.getDeck();
    }

    public AssistantCard get(String cardName) throws AssistantCardNotFound {
        for (AssistantCard card : assistantCards) {
                if(card.getImageName().equals(cardName)){
                    int indexToRemove = assistantCards.indexOf(card);
                    AssistantCard returnedCard = new AssistantCard(card.getImageName(),card.getMove());
                    assistantCards.remove(indexToRemove);
                    return returnedCard;
            }
        }
        throw new AssistantCardNotFound();
    }

    public ArrayList<AssistantCard> getAllCards(){
        return assistantCards;
    }

}
