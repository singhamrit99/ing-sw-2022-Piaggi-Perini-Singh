package it.polimi.ingsw.model.cards;

import com.google.gson.reflect.TypeToken;
//import com.sun.tools.classfile.CharacterRangeTable_attribute;
import it.polimi.ingsw.model.FilePaths;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.google.gson.*;

public class CharacterDeck {
    private ArrayList<CharacterCard> deck;

    public CharacterDeck(ArrayList<CharacterCard> deck) {
        this.deck = deck;
    }

    public void newDeck() throws NullPointerException {
        this.deck = new ArrayList<>();
        loadFromJSON();
    }

    private void loadFromJSON() throws NullPointerException {
        Gson gson = new Gson();

        InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(CharacterCard.class.getResourceAsStream(FilePaths.getCharacterCardsLocation())), StandardCharsets.UTF_8);
        Scanner s = new Scanner(streamReader).useDelimiter("\\A");
        String JSONContent = s.hasNext() ? s.next() : "";
        deck = gson.fromJson(JSONContent, new TypeToken<List<CharacterCard>>() {
        }.getType());
    }

    public ArrayList<CharacterCard> getDeck() {
        return deck;
    }
}
