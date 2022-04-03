package it.polimi.ingsw.model.cards;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
//import com.sun.tools.classfile.CharacterRangeTable_attribute;
import it.polimi.ingsw.model.FilePaths;
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

public class FillCharacterDeck {

    private ArrayList<AssistantCard> deck;
    private String fileContent;
    public ArrayList<AssistantCard> newDeck(ArrayList<AssistantCard> deck) {
        this.deck = new ArrayList<>();
        deck= loadFromJSON(deck);
        return deck;
    }

    private ArrayList<AssistantCard> loadFromJSON( ArrayList<AssistantCard> deck) {
        Gson gson = new Gson();

        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(FilePaths.CHARACTER_CARDS_LOCATION)), StandardCharsets.UTF_8);

            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            String jsoncontent = s.hasNext() ? s.next() : "";
            deck = gson.fromJson(jsoncontent, new TypeToken<List<AssistantCard>>() {
            }.getType());

        }
        catch(NullPointerException nil)
        {
            System.out.println("JSON card file not found\n");
        }
        return deck;
    }


}
