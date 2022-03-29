package it.polimi.ingsw.model.cards;


import com.google.gson.stream.JsonReader;
import com.sun.tools.classfile.CharacterRangeTable_attribute;
import it.polimi.ingsw.model.GetPaths;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.google.gson.*;

public class FillCharacterDeck {

    private ArrayList<CharacterCard> deck;


    public void newDeck(ArrayList<CharacterCard> deck){

        loadFromJSON(deck);

    }

    private void loadFromJSON(ArrayList<CharacterCard> deck) {

        Gson gson = new Gson();


        try {
            InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.ASSISTANT_CARDS_LOCATION), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(streamReader);
            String fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.CHARACTER_CARDS_LOCATION)));
            deck = gson.fromJson(jsonReader, AssistantCard.class);

        } catch (Exception e) {
            e.printStackTrace();

        }


    }
    public ArrayList<CharacterCard> getDeck(){
        return deck;
    }









}
