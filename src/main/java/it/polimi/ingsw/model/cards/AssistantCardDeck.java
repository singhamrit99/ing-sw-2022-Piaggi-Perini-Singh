package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.FilePaths;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//Each Player has to have a deck of Assistant Cards, numbered 1 thorough 10.
public class AssistantCardDeck {
    private ArrayList<AssistantCard> deck;

    public AssistantCardDeck() {
        deck = new ArrayList<>();
    }

    public void newDeck() throws NullPointerException {
        this.deck = new ArrayList<>();
        loadFromJSON();
    }

    private void loadFromJSON() throws NullPointerException {
        Gson gson = new Gson();
        InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(FilePaths.getAssistantCardLocation())), StandardCharsets.UTF_8);

        Scanner s = new Scanner(streamReader).useDelimiter("\\A");
        String JSONContent = s.hasNext() ? s.next() : "";
        deck = gson.fromJson(JSONContent, new TypeToken<List<AssistantCard>>() {
        }.getType());
    }

    public AssistantCard getAssistantCard(int index) {
        AssistantCard returnCard;

        returnCard = deck.get(index);
        deck.remove(index);
        return returnCard;
    }

    public ArrayList<AssistantCard> getDeck() {
        return deck;
    }
}
