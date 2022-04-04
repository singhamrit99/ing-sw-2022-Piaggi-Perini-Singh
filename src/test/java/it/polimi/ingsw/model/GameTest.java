package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    public void testPlayersInit() throws IncorrectArgumentException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Amrit");
        nicknames.add("Lore");
        nicknames.add("Tino");
        nicknames.add("Tony Stark");
        Game game = new Game(false, 4, nicknames);
        ArrayList<Player> players = game.getPlayers();
        assertTrue(nicknames.contains(game.getCurrentPlayer().getNickname()));
        assertEquals(players.size(), 4);
    }

    /*
    @Test
    public void testDrawFromBag() throws IncorrectArgumentException{
        Game game = initGame2players();
        game.drawFromBag(game.getCurrentPlayer().getNickname()); //Error in BAG
    }
    */

    public Game initGame2players() throws IncorrectArgumentException{
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Luke Skywalker");
        nicknames.add("Dark Fener");
        return new Game(false, 2, nicknames);
    }

    public Game initGame3players() throws IncorrectArgumentException{
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Bruce Wayne");
        nicknames.add("Joker");
        nicknames.add("Bane");
        return new Game(false, 3, nicknames);
    }

    public Game initGame4players() throws IncorrectArgumentException{
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        return new Game(true, 4, nicknames);
    }

    @Test
    public void testNextPlayer3Players() throws IncorrectArgumentException, IncorrectStateException {
        Game game = initGame3players();
        String oldPlayerNickname = game.getCurrentPlayer().getNickname();
        game.nextPlayer();
        String newPlayerNickname = game.getCurrentPlayer().getNickname();
        assertNotEquals(oldPlayerNickname, newPlayerNickname);
    }

    @Test
    public void testNextPlayer2and4Players() throws IncorrectArgumentException, IncorrectStateException {
        Game game = initGame4players();
        String oldPlayerNickname = game.getCurrentPlayer().getNickname();
        game.nextPlayer();
        assertNotEquals(oldPlayerNickname, game.getCurrentPlayer().getNickname());
    }

    @Test
    void areJSONloadingOk() {
        ArrayList<IslandTile> importingIslands;
        String jsoncontent = "";
        Gson gson = new Gson();
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(FilePaths.ISLAND_TILES_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            jsoncontent = s.hasNext() ? s.next() : "";
            System.out.println(jsoncontent);
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingIslands = gson.fromJson(jsoncontent, new TypeToken<List<IslandTile>>() {
        }.getType());

        for (int i = 0; i < importingIslands.size(); i++) {
            assertEquals("isola" + (i + 1), importingIslands.get(i).getName());
        }
    }

    @Test
    void testImportingTilesJSON() throws IncorrectArgumentException {
        ArrayList<IslandTile> importingIslands;
        ArrayList<CloudTile> importingClouds;
        ArrayList<String> nicknames = new ArrayList<>(Arrays.asList("p1", "p2"));
        Game game = new Game(true, 3, nicknames);
        game.importingTilesJson();
        importingClouds = game.getImportingClouds();
        importingIslands = game.getimportingIslands();
       /* for(int i=0; i<importingIslands.size(); i++) {
            assertEquals("isola" + (i + 1), importingIslands.get(i).getName());
        }
        for(int i=0; i<importingClouds.size()-1; i++) {
            assertEquals("nuvola" + (i + 1), importingClouds.get(i).getName());
        }*/
    }

    @Test
    void playAssistantCard() {
    }

    @Test
    void nextRound() {
    }

    @Test
    void takeStudentsFromCloud() {
    }

    @Test
    void moveStudents() {
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void checkAndPlaceProfessor() {
    }

    @Test
    void findPlayerFromTeam() {
    }

    @Test
    void moveTowersFromTeam() {
    }

    @Test
    void checkUnificationIslands() {
    }

    @Test
    void isGameOver() {
    }

    @Test
    void checkWinner() {
    }
}