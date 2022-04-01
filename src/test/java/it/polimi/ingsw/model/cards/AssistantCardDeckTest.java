package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.GetPaths;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AssistantCardDeckTest {

@Test
@DisplayName("Am I even getting the path right?")
public void shouldPassFilepathsasStrings()
{
    Assertions.assertEquals(GetPaths.ASSISTANT_CARDS_LOCATION, "InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(pathToJSON), StandardCharsets.UTF_8);\n" +
            "            JsonReader jsonReader = new JsonReader(streamReader);\n" +
            "             fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ASSISTANT_CARDS_LOCATION)));");
    Assertions.assertEquals(GetPaths.CHARACTER_CARDS_LOCATION, "src/main/resources/Characters.json");
    Assertions.assertEquals(GetPaths.ISLAND_TILES_LOCATION, "src/main/resources/IslandTiles.json");
    Assertions.assertEquals(GetPaths.CLOUD_TILES_LOCATION, "src/main/resources/Clouds.json");


}

    private  String expected;
    @Test
    @DisplayName("Should check if JSON loaded correctly")
    public void shouldReadJson()
    {    String fileContent;
        expected="[\n" +
                "  {\n" +
                "    \"name\": \"Assistente(1)\",\n" +
                "    \"value\": 1,\n" +
                "    \"move\" : 1\n" +
                "\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Assistente(2)\",\n" +
                "    \"value\": 2,\n" +
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
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream("/Cards.json")), StandardCharsets.UTF_8);
            //JsonReader jsonReader = new JsonReader(streamReader);
            Scanner s= new Scanner(streamReader).useDelimiter("\\A");
            String result = s.hasNext() ? s.next(): "";


            Assertions.assertEquals(expected, result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

   /* @Test
    @DisplayName("Can I find the files?")
    public void shouldFindFiles()
    {
        URL url = String.class.getResource("src/main/resources/Cards.json");
        URL expected= new URL("src/main/resources/Cards.json");

        Assertions.assertEquals(expected, url);



    }*/
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