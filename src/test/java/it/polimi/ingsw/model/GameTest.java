package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void areJSONloadingOk() {
        ArrayList<IslandTile> importingIslands;
        String jsoncontent="";
        Gson gson = new Gson();
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(FilePaths.ISLAND_TILES_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
           jsoncontent = s.hasNext() ? s.next() : "";
           System.out.println(jsoncontent);
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingIslands = gson.fromJson(jsoncontent, new TypeToken<List<IslandTile>>() {}.getType());

        for(int i=0; i<importingIslands.size(); i++) {
            assertEquals("isola" + (i + 1), importingIslands.get(i).getName());
        }
    }

    @Test
    void testImportingTilesJSON() throws IncorrectArgumentException {
        ArrayList<IslandTile> importingIslands;
        ArrayList <CloudTile> importingClouds;
        ArrayList<String> nicknames= new ArrayList<>(Arrays.asList("p1","p2"));
        Game game = new Game(3, nicknames);
        game.importingTilesJson();
        importingClouds=game.getImportingClouds();
        importingIslands=game.getimportingIslands();
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