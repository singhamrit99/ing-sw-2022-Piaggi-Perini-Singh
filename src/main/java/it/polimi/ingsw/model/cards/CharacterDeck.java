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

    public void newDeck(String resourcefilepath) throws FileNotFoundException{
        this.deck = new ArrayList<>();
        loadFromJSON(resourcefilepath);
        //System.out.println(deck.get(0).getDescription());
    }

    private void loadFromJSON(String filepaths) throws NullPointerException {
        Gson gson = new Gson();

            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(CharacterCard.class.getResourceAsStream(filepaths)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            String jsoncontent = s.hasNext() ? s.next() : "";
            deck = gson.fromJson(jsoncontent, new TypeToken<List<CharacterCard>>() {
            }.getType());



    }

    public ArrayList<CharacterCard> getDeck(){
        return deck;
    }


}
