package it.polimi.ingsw.model.cards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

public class CardsTest {

    String RemoveSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }

    //Card testing
    @Test
    public void testSetName() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
        String nome = "Menisco";
        a.setName(nome);
        assertEquals(a.getName(), nome);

    }


    //AssistantCard Testing
    @Test
    public void testAssistantCardConstructor() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
        assertEquals(0, a.getValue());
        assertEquals(0, a.getMove());
        assertEquals("none", a.getWizard());
        assertFalse(a.isHasPlayed());
    }

    @Test
    public void testAssistantCardGetWizard() {

        final AssistantCard a = new AssistantCard(0, 0, "none", false);
        String wizard = "Silt";
        a.setWizard(wizard);
        assertEquals(wizard, a.getWizard());

    }

    @Test
    public void testAssistantCardGetMove() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
        final int move = a.getMove();
        assertEquals(move, a.getMove());
    }

    @Test
    public void testAssistantCardSetWizard() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
        String wizard = "Silt";
        a.setWizard(wizard);
        assertEquals(a.getWizard(), wizard);
    }

    @Test
    public void testSetHasPlayed() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
        boolean played = true;
        a.setHasPlayed(played);
        assertTrue(a.isHasPlayed());
    }

    @Test
    public void testisHasPlayed() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
    }

    @Test
    public void shouldPassFilepathsasStrings() {
        Assertions.assertEquals(GetPaths.ASSISTANT_CARDS_LOCATION, "/Cards.json");
        Assertions.assertEquals(GetPaths.ISLAND_TILES_LOCATION, "/IslandTiles.json");
        Assertions.assertEquals(GetPaths.CLOUD_TILES_LOCATION, "/Clouds.json");


    }

    private String expected;


    //AssistantCardDeck testing
    @Test
    public void testgetAssistantCardInOrder() {
        FillDeck deckfiller = new FillDeck();
        ArrayList<AssistantCard> testDeck = new ArrayList<>();
        AssistantCardDeck newdeck = new AssistantCardDeck();
        newdeck.setAssistantCards(deckfiller.newDeck());


        //Drawing in order
        assertEquals("Assistente(1)", newdeck.getAssistantCard(1).getName());
        assertEquals("Assistente(2)", newdeck.getAssistantCard(2).getName());
        assertEquals("Assistente(3)", newdeck.getAssistantCard(3).getName());
        assertEquals("Assistente(4)", newdeck.getAssistantCard(4).getName());
        assertEquals("Assistente(5)", newdeck.getAssistantCard(5).getName());
        assertEquals("Assistente(6)", newdeck.getAssistantCard(6).getName());
        assertEquals("Assistente(7)", newdeck.getAssistantCard(7).getName());
        assertEquals("Assistente(8)", newdeck.getAssistantCard(8).getName());
        assertEquals("Assistente(9)", newdeck.getAssistantCard(9).getName());
        assertEquals("Assistente(10)", newdeck.getAssistantCard(10).getName());
    }

    @Test
    public void testGetAssistantCardOutOfOrder() {
        FillDeck deckfiller = new FillDeck();
        ArrayList<AssistantCard> testDeck = new ArrayList<>();
        AssistantCardDeck newdeck = new AssistantCardDeck();
        newdeck.setAssistantCards(deckfiller.newDeck());


        //Drawing in order
        assertEquals("Assistente(5)", newdeck.getAssistantCard(5).getName());
        assertEquals("Assistente(3)", newdeck.getAssistantCard(3).getName());
        assertEquals("Assistente(4)", newdeck.getAssistantCard(4).getName());
        assertEquals("Assistente(7)", newdeck.getAssistantCard(7).getName());
        assertEquals("Assistente(8)", newdeck.getAssistantCard(8).getName());
        assertEquals("Assistente(10)", newdeck.getAssistantCard(10).getName());
        assertEquals("Assistente(1)", newdeck.getAssistantCard(1).getName());
        assertEquals("Assistente(2)", newdeck.getAssistantCard(2).getName());
        assertEquals("Assistente(6)", newdeck.getAssistantCard(6).getName());
        assertEquals("Assistente(9)", newdeck.getAssistantCard(9).getName());


    }

    //FillDeck testing

    @Test
    @DisplayName("Should check if JSON loaded correctly")
    public void shouldReadJson() {
        String fileContent;
        expected = "[\n" +
                "  {\n" +
                "    \"type\": \"assistant\",\n" +
                "    \"name\": \"Assistente(1)\",\n" +
                "    \"value\": 1,\n" +
                "    \"move\" : 1,\n" +
                "    \"wizard\": \"none\",\n" +
                "    \"hasplayed\": false\n" +
                "\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"assistant\",\n" +
                "    \"name\": \"Assistente(2)\",\n" +
                "    \"value\": 2,\n" +
                "    \"move\" : 1,\n" +
                "    \"wizard\": \"none\",\n" +
                "    \"hasplayed\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(3)\",\n" +
                "  \"value\": 3,\n" +
                "  \"move\" : 2,\n" +
                "    \"wizard\": \"none\",\n" +
                "    \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(4)\",\n" +
                "  \"value\": 4,\n" +
                "  \"move\" : 2,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(5)\",\n" +
                "  \"value\": 5,\n" +
                "  \"move\" : 3,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(6)\",\n" +
                "  \"value\": 6,\n" +
                "  \"move\" : 3,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(7)\",\n" +
                "  \"value\": 7,\n" +
                "  \"move\" : 4,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(8)\",\n" +
                "  \"value\": 8,\n" +
                "  \"move\" : 4,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(9)\",\n" +
                "  \"value\": 9,\n" +
                "  \"move\" : 5,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(10)\",\n" +
                "  \"value\": 10,\n" +
                "  \"move\" : 5,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}\n" +
                "\n" +
                "]";
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream("/Cards.json")), StandardCharsets.UTF_8);
            //JsonReader jsonReader = new JsonReader(streamReader);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";


            Assertions.assertEquals(RemoveSpaces(expected), RemoveSpaces(result));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testFillDeck() {
        String jsoncontent;
        FillDeck test = new FillDeck();
        AssistantCardDeck deck = new AssistantCardDeck();
        /*jsoncontent = "[\n" +
                "  {\n" +
                "    \"type\": \"assistant\",\n" +
                "    \"name\": \"Assistente(1)\",\n" +
                "    \"value\": 1,\n" +
                "    \"move\" : 1,\n" +
                "    \"wizard\": \"none\",\n" +
                "    \"hasplayed\": false\n" +
                "\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"assistant\",\n" +
                "    \"name\": \"Assistente(2)\",\n" +
                "    \"value\": 2,\n" +
                "    \"move\" : 1,\n" +
                "    \"wizard\": \"none\",\n" +
                "    \"hasplayed\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(3)\",\n" +
                "  \"value\": 3,\n" +
                "  \"move\" : 2,\n" +
                "    \"wizard\": \"none\",\n" +
                "    \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(4)\",\n" +
                "  \"value\": 4,\n" +
                "  \"move\" : 2,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(5)\",\n" +
                "  \"value\": 5,\n" +
                "  \"move\" : 3,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(6)\",\n" +
                "  \"value\": 6,\n" +
                "  \"move\" : 3,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(7)\",\n" +
                "  \"value\": 7,\n" +
                "  \"move\" : 4,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(8)\",\n" +
                "  \"value\": 8,\n" +
                "  \"move\" : 4,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(9)\",\n" +
                "  \"value\": 9,\n" +
                "  \"move\" : 5,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}, {\n" +
                "  \"type\": \"assistant\",\n" +
                "  \"name\": \"Assistente(10)\",\n" +
                "  \"value\": 10,\n" +
                "  \"move\" : 5,\n" +
                "  \"wizard\": \"none\",\n" +
                "  \"hasplayed\": false\n" +
                "}\n" +
                "\n" +
                "]";/*
        /*Gson gson = new Gson();
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream("/Cards.json")), StandardCharsets.UTF_8);
            //JsonReader jsonReader = new JsonReader(streamReader);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            jsoncontent = s.hasNext() ? s.next() : "";


        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<AssistantCard> TestArray = gson.fromJson(jsoncontent, new TypeToken<List<AssistantCard>>() {
        }.getType());
        */
        deck.setAssistantCards(test.newDeck());
        Assertions.assertEquals(1, deck.getdeck().get(0).getValue());
        Assertions.assertEquals("Assistente(1)", deck.getdeck().get(0).getName());
        Assertions.assertEquals("Assistente(10)", deck.getdeck().get(9).getName());


    }


}

