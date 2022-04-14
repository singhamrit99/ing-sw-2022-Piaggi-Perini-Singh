package it.polimi.ingsw.model.deck.characterdeck;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckGenerator;
import it.polimi.ingsw.model.deck.FileJSONPath;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CharacterCardDeck implements Deck<CharacterCard> {
    private ArrayList<CharacterCard> characterCards;

    public CharacterCardDeck() {
        characterCards = new ArrayList<>();
    }

    public ArrayList<CharacterCard> getDeck() {
        return characterCards;
    }

    @Override
    public void fillDeck() {
        Type collectionType = new TypeToken<ArrayList<CharacterCard>>() {
        }.getType();
        DeckGenerator<CharacterCard> deckGenerator = new DeckGenerator<>(FileJSONPath.CHARACTER_CARDS_JSON, collectionType);
        characterCards = deckGenerator.getDeck();
    }

    @Override
    public CharacterCard get(int index) {
        return characterCards.get(index);
    }

    @Override
    public ArrayList<CharacterCard> getAllCards() {
        return characterCards;
    }

    @Override
    public void discardCard(int index) {
        characterCards.remove(index);
    }
}
