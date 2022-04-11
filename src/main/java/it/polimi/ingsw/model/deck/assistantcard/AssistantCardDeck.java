package it.polimi.ingsw.model.deck.assistantcard;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckGenerator;
import it.polimi.ingsw.model.deck.FileJSONPath;

import java.lang.reflect.Type;
import java.util.ArrayList;

//Each Player has to have a deck of Assistant Cards, numbered 1 thorough 10.
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

    @Override
    public int getDeckSize() {
        return assistantCards.size();
    }

    @Override
    public AssistantCard get(int index) {
        return assistantCards.get(index);
    }

    @Override
    public ArrayList<AssistantCard> getAllCards() {
        return assistantCards;
    }

    @Override
    public void addCard(AssistantCard card) {
        assistantCards.add(card);
    }

    @Override
    public void addCards(ArrayList<AssistantCard> cards) {
        assistantCards.addAll(cards);
    }

    @Override
    public void discardCard(AssistantCard card) {
        assistantCards.remove(card);
    }

    @Override
    public void discardCards(ArrayList<AssistantCard> cards) {
        assistantCards.removeAll(cards);
    }
}
