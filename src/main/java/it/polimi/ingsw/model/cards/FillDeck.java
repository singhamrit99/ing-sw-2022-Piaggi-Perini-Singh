package it.polimi.ingsw.model.cards;


import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.GetPaths;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.google.gson.*;

public class FillDeck {

    private ArrayList<AssistantCard> deck;
    private String fileContent;
    public ArrayList<AssistantCard> newDeck(ArrayList<AssistantCard> deck) {
        this.deck = new ArrayList<>();
        deck= loadFromJSON(deck);
        return deck;
    }

    private ArrayList<AssistantCard> loadFromJSON( ArrayList<AssistantCard> deck) {
        Gson gson = new Gson();


            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream("/Cards.json")), StandardCharsets.UTF_8);

            Scanner s= new Scanner(streamReader).useDelimiter("\\A");
            String jsoncontent = s.hasNext() ? s.next(): "";
            deck  = gson.fromJson(jsoncontent, new TypeToken<List<AssistantCard>>(){}.getType());

        return deck;
    }

}
