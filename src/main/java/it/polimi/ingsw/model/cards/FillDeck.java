package it.polimi.ingsw.model.cards;


import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.GetPaths;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.*;

public class FillDeck {

    private ArrayList<AssistantCard> deck;
    private String fileContent;
    public ArrayList<AssistantCard> newDeck(String pathToJSON, ArrayList<AssistantCard> deck) {
        this.deck = new ArrayList<>();
        loadFromJSON();
        return deck;
    }

    private void loadFromJSON() {
        Gson gson = new Gson();

        try {
            InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.ASSISTANT_CARDS_LOCATION), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(streamReader);
             fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ASSISTANT_CARDS_LOCATION)));
            deck = gson.fromJson(fileContent, ArrayList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<AssistantCard> getDeck() {
        return deck;
    }
    public String getjson(){ return fileContent;}
}
