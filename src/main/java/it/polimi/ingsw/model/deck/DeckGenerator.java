package it.polimi.ingsw.model.deck;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DeckGenerator<T> {
    private ArrayList<T> deck;
    private final String JSONFilePath;

    /**
     * Constructor for DeckGenerator class. Used to determine the file to load from and the Type of the collection.
     * @param JSONFilePath The file the generator references to build the deck.
     * @param collectionType the Type of the deck to build.
     */
    public DeckGenerator(String JSONFilePath, Type collectionType) {
        this.deck = new ArrayList<>();
        this.JSONFilePath = JSONFilePath;
        loadDeckFromJSON(collectionType);
    }

    /**
     * Create deck reading a json file that contains all T-type cards properties
     *
     * @param collectionType type of data structure used to create the deck
     */
    private void loadDeckFromJSON(Type collectionType) {
        Gson gson = new Gson();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(DeckGenerator.class.getResourceAsStream(JSONFilePath), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(inputStreamReader);
            deck = gson.fromJson(jsonReader, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the deck containing all cards read from json file
     */
    public ArrayList<T> getDeck() {
        return deck;
    }
}
