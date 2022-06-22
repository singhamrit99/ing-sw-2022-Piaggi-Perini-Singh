package it.polimi.ingsw.model.deck.assistantcard;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exceptions.AssistantCardNotFoundException;
import it.polimi.ingsw.model.cards.assistantcard.AssistantCard;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckGenerator;
import it.polimi.ingsw.model.deck.FileJSONPath;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Amrit
 */
public class AssistantCardDeck implements Deck<AssistantCard>, Serializable {
    private ArrayList<AssistantCard> assistantCards;

    private String owner;

    /**
     * AssistantCardDeck constructor: the class is simply an ArrayList of Assistant cards with the player's name.
     * @param owner The player that owns this deck.
     */
    public AssistantCardDeck(String owner) {
        this.owner = owner;
        assistantCards = new ArrayList<>();
    }

    /**
     * Getter method for the deck of assistant cards.
     * @return Assistant cards arrayList.
     */
    public ArrayList<AssistantCard> getDeck() {
        return assistantCards;
    }
    /**
     * Getter method for the player name.
     * @return player name.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Method that fills the deck with information coming from the AssistantCards JSON file.
     */
    @Override
    public void fillDeck() {
        Type collectionType = new TypeToken<ArrayList<AssistantCard>>() {
        }.getType();
        DeckGenerator<AssistantCard> deckGenerator = new DeckGenerator<>(FileJSONPath.ASSISTANT_CARDS_JSON, collectionType);
        assistantCards = deckGenerator.getDeck();
    }

    /**
     * Getter method for single card in the deck. Essential for play.
     * @param cardName requested card.
     * @return requested card
     * @throws AssistantCardNotFoundException Thrown when the requested Assistant Card is not found in the deck.
     */
    public AssistantCard get(String cardName) throws AssistantCardNotFoundException {
        for (AssistantCard card : assistantCards) {
            if (card.getImageName().equals(cardName)) {
                int indexToRemove = assistantCards.indexOf(card);
                AssistantCard returnedCard = new AssistantCard(card.getImageName(), card.getMove());
                assistantCards.remove(indexToRemove);
                return returnedCard;
            }
        }
        throw new AssistantCardNotFoundException();
    }

    /**
     * Returns all cards in the deck.
     * @return assistantcards.
     */
    public ArrayList<AssistantCard> getAllCards() {
        return assistantCards;
    }

}
