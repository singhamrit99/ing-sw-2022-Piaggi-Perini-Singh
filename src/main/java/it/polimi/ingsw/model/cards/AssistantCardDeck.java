package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.FilePaths;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//Each Player has to have a deck of Assistant Cards, numbered 1 thorough 10.
public class AssistantCardDeck {

    private ArrayList<AssistantCard> deck;


    public AssistantCardDeck(ArrayList<AssistantCard> deck) {
        this.deck = deck;
    }

    public void newDeck(String resourcefilepath) throws NullPointerException{
        this.deck = new ArrayList<>();
         loadFromJSON(resourcefilepath);
    }

    private void loadFromJSON(String filepath) throws  NullPointerException {
        Gson gson = new Gson();


            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(filepath)), StandardCharsets.UTF_8);

            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            String jsoncontent = s.hasNext() ? s.next() : "";
            deck = gson.fromJson(jsoncontent, new TypeToken<List<AssistantCard>>() {
            }.getType());

    }

    public AssistantCard getAssistantCard(int index) {
        AssistantCard returncard;
        int lenght=deck.size();
       // System.out.println("I am playing the card number " + deck.get(actualposition).getValue());

        returncard= deck.get(index);
        deck.remove(index);
        return returncard;





    }


    public void setdeck(ArrayList<it.polimi.ingsw.model.cards.AssistantCard> deck) {
        this.deck = deck;
    }
    public ArrayList<AssistantCard> getdeck()
    {
        return deck;
    }
}
