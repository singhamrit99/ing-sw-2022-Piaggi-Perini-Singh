package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.GetPaths;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardDeckTest {



    private final String expected ="[\\n\" +\n" +
            "                 \"  {\\n\" +\n" +
            "                 \"    \\\"name\\\": \\\"Assistente(1)\\\",\\n\" +\n" +
            "                 \"    \\\"value\\\": 1,\\n\" +\n" +
            "                 \"    \\\"move\\\" : 1\\n\" +\n" +
            "                 \"\\n\" +\n" +
            "                 \"  },\\n\" +\n" +
            "                 \"  {\\n\" +\n" +
            "                 \"    \\\"name\\\": \\\"Assistente(2)\\\",\\n\" +\n" +
            "                 \"    \\\"value\\\": 1,\\n\" +\n" +
            "                 \"    \\\"move\\\" : 1\\n\" +\n" +
            "                 \"  },\\n\" +\n" +
            "                 \"  {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(3)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 3,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 2\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(4)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 4,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 2\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(5)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 5,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 3\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(6)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 6,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 3\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(7)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 7,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 4\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(8)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 8,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 4\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(9)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 9,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 5\\n\" +\n" +
            "                 \"}, {\\n\" +\n" +
            "                 \"  \\\"name\\\": \\\"Assistente(10)\\\",\\n\" +\n" +
            "                 \"  \\\"value\\\": 10,\\n\" +\n" +
            "                 \"  \\\"move\\\" : 5\\n\" +\n" +
            "                 \"}\\n\" +\n" +
            "                 \"\\n\" +\n" +
            "                 \"]";

    @Test
    @DisplayName("Should check if JSON loaded correctly")
    void shouldReadJson()
    {   FillDeck deckfiller = new FillDeck();
        ArrayList<AssistantCard> deck= new ArrayList<>();
        deckfiller.newDeck(GetPaths.ASSISTANT_CARDS_LOCATION, deck);
        String expected ="[\\n\" +\n" +
                "                 \"  {\\n\" +\n" +
                "                 \"    \\\"name\\\": \\\"Assistente(1)\\\",\\n\" +\n" +
                "                 \"    \\\"value\\\": 1,\\n\" +\n" +
                "                 \"    \\\"move\\\" : 1\\n\" +\n" +
                "                 \"\\n\" +\n" +
                "                 \"  },\\n\" +\n" +
                "                 \"  {\\n\" +\n" +
                "                 \"    \\\"name\\\": \\\"Assistente(2)\\\",\\n\" +\n" +
                "                 \"    \\\"value\\\": 1,\\n\" +\n" +
                "                 \"    \\\"move\\\" : 1\\n\" +\n" +
                "                 \"  },\\n\" +\n" +
                "                 \"  {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(3)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 3,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 2\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(4)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 4,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 2\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(5)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 5,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 3\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(6)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 6,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 3\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(7)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 7,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 4\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(8)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 8,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 4\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(9)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 9,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 5\\n\" +\n" +
                "                 \"}, {\\n\" +\n" +
                "                 \"  \\\"name\\\": \\\"Assistente(10)\\\",\\n\" +\n" +
                "                 \"  \\\"value\\\": 10,\\n\" +\n" +
                "                 \"  \\\"move\\\" : 5\\n\" +\n" +
                "                 \"}\\n\" +\n" +
                "                 \"\\n\" +\n" +
                "                 \"]";

        Assertions.assertEquals(expected,deckfiller.getjson() );



    }
   /* void shouldFillDeck(){
        String jsoncontent= new String;
         jsoncontent= "[\n" +
                 "  {\n" +
                 "    \"name\": \"Assistente(1)\",\n" +
                 "    \"value\": 1,\n" +
                 "    \"move\" : 1\n" +
                 "\n" +
                 "  },\n" +
                 "  {\n" +
                 "    \"name\": \"Assistente(2)\",\n" +
                 "    \"value\": 1,\n" +
                 "    \"move\" : 1\n" +
                 "  },\n" +
                 "  {\n" +
                 "  \"name\": \"Assistente(3)\",\n" +
                 "  \"value\": 3,\n" +
                 "  \"move\" : 2\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(4)\",\n" +
                 "  \"value\": 4,\n" +
                 "  \"move\" : 2\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(5)\",\n" +
                 "  \"value\": 5,\n" +
                 "  \"move\" : 3\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(6)\",\n" +
                 "  \"value\": 6,\n" +
                 "  \"move\" : 3\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(7)\",\n" +
                 "  \"value\": 7,\n" +
                 "  \"move\" : 4\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(8)\",\n" +
                 "  \"value\": 8,\n" +
                 "  \"move\" : 4\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(9)\",\n" +
                 "  \"value\": 9,\n" +
                 "  \"move\" : 5\n" +
                 "}, {\n" +
                 "  \"name\": \"Assistente(10)\",\n" +
                 "  \"value\": 10,\n" +
                 "  \"move\" : 5\n" +
                 "}\n" +
                 "\n" +
                 "]";
        Gson gson = new Gson();
        ArrayList<AssistantCard> TestArray = gson.fromJson(jsoncontent, AssistantCard.class);

    }*/



}