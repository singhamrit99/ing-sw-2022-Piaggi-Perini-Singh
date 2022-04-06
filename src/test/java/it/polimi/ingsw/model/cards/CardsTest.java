package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.FilePaths;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class CardsTest {
    private String expected;

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
    public void testHasPlayed() {
        final AssistantCard a = new AssistantCard(0, 0, "none", false);
    }

    @Test
    public void shouldPassFilePathsAsStrings() {
        Assertions.assertEquals(FilePaths.getAssistantCardLocation(), "/Cards.json");
        Assertions.assertEquals(FilePaths.getIslandTilesLocation(), "/IslandTiles.json");
        Assertions.assertEquals(FilePaths.getCloudTilesLocation(), "/Clouds.json");
    }

    //AssistantCardDeck testing
    @Test
    public void testGetAssistantCard() {
        AssistantCardDeck newDeck = new AssistantCardDeck();
        newDeck.newDeck();
        int i;
        Random rand = new Random();
        i = rand.nextInt(newDeck.getDeck().size());
        assertEquals(i + 1, newDeck.getAssistantCard(i).getValue());
    }

    @Test
    @DisplayName("Should check if JSON loaded correctly")
    public void shouldReadJson() {
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

    //Assistant card deck testing
    @Test
    public void testNewDeck() {
        AssistantCardDeck deck = new AssistantCardDeck();
        deck.newDeck();

        Assertions.assertEquals(1, deck.getDeck().get(0).getValue());
        Assertions.assertEquals("Assistente(1)", deck.getDeck().get(0).getName());
        Assertions.assertEquals("Assistente(10)", deck.getDeck().get(9).getName());
    }


    //CharacterCard testing
    @Test
    public void testCharacterCardConstructor() {
        final CharacterCard a = new CharacterCard(0, 0, "none", "something cool");
        assertEquals(0, a.getCharacterID());
        assertEquals(0, a.getStartingPrice());
        assertEquals("none", a.getPower());
        assertEquals("something cool", a.getDescription());
    }

    @Test
    public void testSetPrice() {
        final CharacterCard a = new CharacterCard(0, 0, "none", "something cool");
        final int newPrice = 5;

        a.setPrice(newPrice);
        assertEquals(newPrice, a.getPrice());
    }

    @Test
    public void testSetDescription() {
        final CharacterCard a = new CharacterCard(0, 0, "none", "something cool");
        final String newDescription = "A VERY cool new description!";
        a.setDescription(newDescription);
        assertEquals(newDescription, a.getDescription());
    }
    //FillCharacterCardDeck testing

    @Test
    public void testNewCharacterDeck() throws NullPointerException {
        ArrayList<CharacterCard> arrayDeck = new ArrayList<>();
        CharacterDeck deck = new CharacterDeck(arrayDeck);
        deck.newDeck();
        assertEquals(1, deck.getDeck().get(0).getCharacterID());
        assertEquals("Move students on the character card to any isle you wish!", deck.getDeck().get(0).getDescription());
        assertEquals(1, deck.getDeck().get(0).getPrice());
        assertEquals("setup", deck.getDeck().get(0).getPower());
    }
}




